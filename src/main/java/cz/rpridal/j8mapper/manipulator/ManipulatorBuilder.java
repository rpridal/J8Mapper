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
import cz.rpridal.j8mapper.SimpleMethodManipulator;
import cz.rpridal.j8mapper.getter.MethodGetter;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperBuilder;
import cz.rpridal.j8mapper.mapper.MapperStorage;
import cz.rpridal.j8mapper.setter.MethodSetter;
import cz.rpridal.j8mapper.transformer.IdentityTransformer;
import cz.rpridal.j8mapper.transformer.MapperTransformer;
import cz.rpridal.j8mapper.transformer.Transformer;

public class ManipulatorBuilder<S, T> {

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
	
	private MapperManipulatorBuilder<S, T> objectManipulatorBuilder = new MapperManipulatorBuilder<>();
	private final MapperStorage storage;
	private static final Map<Methods, Manipulator<?, ?>> manipulators = Collections.synchronizedMap(new HashMap<>());
	private static final Semaphore<Methods> semaphore = new Semaphore<>(3);
	
	
	public ManipulatorBuilder(Class<S> source, Class<T> target, MapperStorage storage) {
		this.storage = storage;
	}

	public static <S, T> ManipulatorBuilder<S, T> start(Class<S> source, Class<T> target, MapperStorage storage) {
		return new ManipulatorBuilder<S, T>(source, target, storage);
	}

	@SuppressWarnings("unchecked")
	public Manipulator<S, T> getManipulator(Method getter, Method setter) {
		Methods methods = new Methods(getter, setter);
		if(!manipulators.containsKey(methods)){
			if(ManipulatorBuilder.semaphore.isLocked(methods)){
				return new IdentityCacheManipulator<S, T>(getter, setter);
			}
			ManipulatorBuilder.semaphore.lock(methods);
			manipulators.put(methods, createManipulator(getter, setter));
			ManipulatorBuilder.semaphore.unlock(methods);
		}
		return ((Manipulator<S, T>) manipulators.get(methods));
		
	}
	
	@SuppressWarnings("unchecked")
	public <SD, TD> Manipulator<S, T> createManipulator(Method getter, Method setter) {
		Class<SD> sourceClass = (Class<SD>) getter.getReturnType();
		Class<TD> targetClass = (Class<TD>) setter.getParameterTypes()[0];
		SimpleMethodManipulator<S, T> methodManipulator = new SimpleMethodManipulator<>(getter, setter);
		if (methodManipulator.isApplicable()) {
			return methodManipulator;
		} else if (List.class.isAssignableFrom(sourceClass) && List.class.isAssignableFrom(targetClass)) {
			return processCollection(getter, setter, ArrayList::new);
		} else if (Set.class.isAssignableFrom(sourceClass) && Set.class.isAssignableFrom(targetClass)) {
			return processCollection(getter, setter, HashSet::new);
		} else {
			return getMapperManipulator(getter, setter, sourceClass, targetClass);
		}
	}

	@SuppressWarnings("unchecked")
	private <TD, SD, SDL extends Collection<SD>, TDL extends Collection<TD>> Manipulator<S, T> processCollection(Method getter, Method setter, Supplier<TDL> supplier) {
		Class<SD> listSourceClass = (Class<SD>) getFirstGenericTypeFromMethod(getter.getGenericReturnType());
		Class<TD> listTargetClass = (Class<TD>) getFirstGenericTypeFromMethod(setter.getGenericParameterTypes()[0]);
		System.out.println(listSourceClass);
		System.out.println(listTargetClass);
		return new CollectionManipulator<S, T, SDL, TDL, SD, TD>(
				new MethodGetter<>(getter), 
				new MethodSetter<>(setter), 
				getTransformer(listSourceClass, listTargetClass),
				supplier
				);
	}

	private <SD, TD> Transformer<SD, TD> getTransformer(Class<SD> listSourceClass, Class<TD> listTargetClass) {
		if(listSourceClass.equals(listTargetClass)){
			return new IdentityTransformer<SD, TD>();
		}
		return new MapperTransformer<>(
				new ClassSupplier<>(listTargetClass), 
				getMapper(listSourceClass, listTargetClass)
		);
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

	private Manipulator<S, T> getMapperManipulator(Method getter, Method setter, Class<?> sourceClass,
			Class<?> targetClass) {
		ClassDefinition<?, ?> classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		if (!storage.hasMapper(classDefinition)) {
			Mapper<?, ?> mapper = MapperBuilder.start(sourceClass, targetClass).automatic().build();
			storage.store(mapper);
		}

		this.objectManipulatorBuilder.setStorage(this.storage);
		Manipulator<S, T> manipulator = this.objectManipulatorBuilder.getManipulator(getter, setter);
		return manipulator;
	}

	private <XS, XT> Mapper<XS, XT> getMapper(Class<XS> sourceClass, Class<XT> targetClass) {
		ClassDefinition<XS, XT> classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		if (!storage.hasMapper(classDefinition)) {
			Mapper<?, ?> mapper = MapperBuilder.start(sourceClass, targetClass).automatic().build();
			storage.store(mapper);
		}
		return storage.get(classDefinition);
	}
}