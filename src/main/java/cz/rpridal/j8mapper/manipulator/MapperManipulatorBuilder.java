package cz.rpridal.j8mapper.manipulator;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.MethodGetter;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperStorage;
import cz.rpridal.j8mapper.setter.MethodSetter;
import cz.rpridal.j8mapper.transformer.MapperTransformer;

public class MapperManipulatorBuilder<S, T> {
	private MapperStorage storage;

	@SuppressWarnings("unchecked")
	public <SD, TD> Manipulator<S, T> getManipulator(Method getter, Method setter) {
		Class<?>[] parameterTypes = setter.getParameterTypes();
		if(parameterTypes.length != 1){
			return null;
		}
		Class<SD> sourceClass = (Class<SD>) getter.getReturnType();
		Class<TD> targetClass = (Class<TD>) parameterTypes[0];
		ClassDefinition<SD, TD> classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		Mapper<SD, TD> mapper = storage.get(classDefinition);
		if(mapper!= null){
			Supplier<TD> supplier = new ClassSupplier<TD>(classDefinition.getTargetClass());
			Manipulator<S, T> objectManipulator = new CompositeManipulator<S, T, SD, TD>(new MethodGetter<S, SD>(getter), new MethodSetter<>(setter), new MapperTransformer<>(supplier, mapper));
			return objectManipulator;
		}
		return null;
	}
	public void setStorage(MapperStorage storage) {
		this.storage = storage;
	}
}
