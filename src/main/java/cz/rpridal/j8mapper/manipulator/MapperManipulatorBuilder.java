package cz.rpridal.j8mapper.manipulator;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.MethodGetter;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperStorage;
import cz.rpridal.j8mapper.setter.MethodSetter;
import cz.rpridal.j8mapper.transformer.MapperTransformer;

public class MapperManipulatorBuilder<SourceType, TargetType> {
	private MapperStorage storage;

	@SuppressWarnings("unchecked")
	public <SourceDataType, TargetDataType> Manipulator<SourceType, TargetType> getManipulator(Method getter,
			Method setter) {
		Class<?>[] parameterTypes = setter.getParameterTypes();
		if (parameterTypes.length != 1) {
			return null;
		}
		Class<SourceDataType> sourceClass = (Class<SourceDataType>) getter.getReturnType();
		Class<TargetDataType> targetClass = (Class<TargetDataType>) parameterTypes[0];
		ClassDefinition<SourceDataType, TargetDataType> classDefinition = new ClassDefinition<>(sourceClass,
				targetClass);
		Mapper<SourceDataType, TargetDataType> mapper = storage.get(classDefinition);
		if (mapper != null) {
			Supplier<TargetDataType> supplier = new ClassSupplier<TargetDataType>(classDefinition.getTargetClass());
			Manipulator<SourceType, TargetType> objectManipulator = new TransformerManipulator<SourceType, TargetType, SourceDataType, TargetDataType>(
					new MethodGetter<SourceType, SourceDataType>(getter), new MethodSetter<>(setter),
					new MapperTransformer<>(supplier, mapper));
			return objectManipulator;
		}
		return null;
	}

	public void setStorage(MapperStorage storage) {
		this.storage = storage;
	}
}
