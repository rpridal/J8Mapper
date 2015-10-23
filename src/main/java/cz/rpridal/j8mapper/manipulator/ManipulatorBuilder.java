package cz.rpridal.j8mapper.manipulator;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.Semaphore;
import cz.rpridal.j8mapper.getter.MethodGetter;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperBuilder;
import cz.rpridal.j8mapper.mapper.MapperStorage;
import cz.rpridal.j8mapper.setter.MethodSetter;
import cz.rpridal.j8mapper.transformer.EnumTransformer;
import cz.rpridal.j8mapper.transformer.IdentityTransformer;
import cz.rpridal.j8mapper.transformer.MapperTransformer;
import cz.rpridal.j8mapper.transformer.Transformer;

public class ManipulatorBuilder<SourceType, TargetType> {

	private static class Methods {
		private final Method getter;
		private final Method setter;

		public Methods(Method getter, Method setter) {
			super();
			this.getter = getter;
			this.setter = setter;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getter == null) ? 0 : getter.hashCode());
			result = prime * result + ((setter == null) ? 0 : setter.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Methods other = (Methods) obj;
			if (getter == null) {
				if (other.getter != null)
					return false;
			} else if (!getter.equals(other.getter))
				return false;
			if (setter == null) {
				if (other.setter != null)
					return false;
			} else if (!setter.equals(other.setter))
				return false;
			return true;
		}
	}

	private MapperManipulatorBuilder<SourceType, TargetType> objectManipulatorBuilder = new MapperManipulatorBuilder<>();
	private final MapperStorage storage;
	private static final Map<Methods, Manipulator<?, ?>> manipulators = Collections.synchronizedMap(new HashMap<>());
	private static final Semaphore<Methods> semaphore = new Semaphore<>(3);

	public ManipulatorBuilder(Class<SourceType> source, Class<TargetType> target, MapperStorage storage) {
		this.storage = storage;
	}

	public static <SourceType, TargetType> ManipulatorBuilder<SourceType, TargetType> start(Class<SourceType> source,
			Class<TargetType> target, MapperStorage storage) {
		return new ManipulatorBuilder<SourceType, TargetType>(source, target, storage);
	}

	public Manipulator<SourceType, TargetType> getManipulator(Method getter, Method setter) {
		Methods methods = new Methods(getter, setter);
		if (!manipulators.containsKey(methods)) {
			if (ManipulatorBuilder.semaphore.isLocked(methods)) {
				return new IdentityCacheManipulator<SourceType, TargetType>(getter, setter);
			}
			ManipulatorBuilder.semaphore.lock(methods);
			Manipulator<SourceType, TargetType> manipulator = createManipulator(getter, setter);
			if (manipulator != null) {
				manipulators.put(methods, manipulator);
			}
			ManipulatorBuilder.semaphore.unlock(methods);
		}
		return ((Manipulator<SourceType, TargetType>) manipulators.get(methods));

	}

	public <SourceDataType, TargetDataType> Manipulator<SourceType, TargetType> createManipulator(Method getter,
			Method setter) {
		Class<SourceDataType> sourceClass = (Class<SourceDataType>) getter.getReturnType();
		Class<TargetDataType> targetClass = (Class<TargetDataType>) setter.getParameterTypes()[0];
		SimpleMethodManipulator<SourceType, TargetType> methodManipulator = new SimpleMethodManipulator<>(getter,
				setter);
		if (methodManipulator.isApplicable()) {
			return methodManipulator;
		} else if (Collection.class.isAssignableFrom(sourceClass) && List.class.isAssignableFrom(targetClass)) {
			return processCollection(getter, setter, ArrayList::new);
		} else if (Collection.class.isAssignableFrom(sourceClass) && Set.class.isAssignableFrom(targetClass)) {
			return processCollection(getter, setter, HashSet::new);
		} else if (Enum.class.isAssignableFrom(sourceClass) && Enum.class.isAssignableFrom(targetClass)) {
			return processEnum(getter, setter, (Class<Enum>) sourceClass, (Class<Enum>) targetClass);
		} else {
			return getMapperManipulator(getter, setter, sourceClass, targetClass);
		}
	}

	private <SourceDataType extends Enum<SourceDataType>, TargetDataType extends Enum<TargetDataType>> Manipulator<SourceType, TargetType> processEnum(
			Method getter, Method setter, Class<SourceDataType> sourceClass, Class<TargetDataType> targetClass) {
		return new TransformerManipulator<SourceType, TargetType, SourceDataType, TargetDataType>(
				new MethodGetter<>(getter), new MethodSetter<>(setter),
				new EnumTransformer<SourceDataType, TargetDataType>(sourceClass, targetClass));
	}

	@SuppressWarnings("unchecked")
	private <TargetDataType, SourceDataType, SourceDataCollection extends Collection<SourceDataType>, TargetDataCollection extends Collection<TargetDataType>> Manipulator<SourceType, TargetType> processCollection(
			Method getter, Method setter, Supplier<TargetDataCollection> supplier) {
		Class<SourceDataType> listSourceClass = (Class<SourceDataType>) getFirstGenericTypeFromMethod(
				getter.getGenericReturnType());
		Class<TargetDataType> listTargetClass = (Class<TargetDataType>) getFirstGenericTypeFromMethod(
				setter.getGenericParameterTypes()[0]);
		return new CollectionManipulator<SourceType, TargetType, SourceDataCollection, TargetDataCollection, SourceDataType, TargetDataType>(
				new MethodGetter<>(getter), new MethodSetter<>(setter),
				getTransformer(listSourceClass, listTargetClass), supplier);
	}

	private <SourceDataType, TargetDataType> Transformer<SourceDataType, TargetDataType> getTransformer(
			Class<SourceDataType> listSourceClass, Class<TargetDataType> listTargetClass) {
		if (listSourceClass.equals(listTargetClass)) {
			return new IdentityTransformer<SourceDataType, TargetDataType>();
		}
		return new MapperTransformer<>(new ClassSupplier<>(listTargetClass),
				getMapper(listSourceClass, listTargetClass));
	}

	private Class<?> getFirstGenericTypeFromMethod(Type genericReturnType) {
		if (genericReturnType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericReturnType;
			Type[] fieldArgTypes = aType.getActualTypeArguments();
			if (fieldArgTypes.length > 0) {
				return (Class<?>) fieldArgTypes[0];
			}
		}
		return null;
	}

	private Manipulator<SourceType, TargetType> getMapperManipulator(Method getter, Method setter, Class<?> sourceClass,
			Class<?> targetClass) {
		ClassDefinition<?, ?> classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		if (!storage.hasMapper(classDefinition)) {
			Mapper<?, ?> mapper = MapperBuilder.start(sourceClass, targetClass).automatic().build();
			storage.store(mapper);
		}

		this.objectManipulatorBuilder.setStorage(this.storage);
		Manipulator<SourceType, TargetType> manipulator = this.objectManipulatorBuilder.getManipulator(getter, setter);
		return manipulator;
	}

	private <MapperSourceType, MapperTargetType> Mapper<MapperSourceType, MapperTargetType> getMapper(
			Class<MapperSourceType> sourceClass, Class<MapperTargetType> targetClass) {
		ClassDefinition<MapperSourceType, MapperTargetType> classDefinition = new ClassDefinition<>(sourceClass,
				targetClass);
		if (!storage.hasMapper(classDefinition)) {
			Mapper<?, ?> mapper = MapperBuilder.start(sourceClass, targetClass).automatic().build();
			storage.store(mapper);
		}
		return storage.get(classDefinition);
	}
}