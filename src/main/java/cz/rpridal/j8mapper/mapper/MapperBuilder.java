package cz.rpridal.j8mapper.mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.SimpleMapperStorage;
import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.manipulator.Manipulator;
import cz.rpridal.j8mapper.manipulator.ManipulatorBuilder;
import cz.rpridal.j8mapper.setter.Setter;

/**
 * Builder for mapper
 * 
 * @author rpridal
 *
 * @param <S>
 *            source type
 * @param <T>
 *            target type
 */
public class MapperBuilder<S, T> {

	private LambdaMapper<S, T> lambdaMapper;
	private ClassDefinition<S, T> classDefinition;
	private MethodMapper<S, T> methodMapper = null;
	private MapperStorage storage = new SimpleMapperStorage();

	private MapperBuilder(Class<S> sourceClass, Class<T> targetClass) {
		this.classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		this.lambdaMapper = new LambdaMapper<>(this.classDefinition);
	}

	/**
	 * this method initialize mapper - it is for specification of types
	 * MapperBuilder
	 * 
	 * @param souce
	 *            class eg. Source.class
	 * @param target
	 *            class eg. Target.class
	 * @return new MapperBuilder
	 */
	public static <S, T> MapperBuilder<S, T> start(Class<S> sourceClass, Class<T> targetClass) {
		return new MapperBuilder<S, T>(sourceClass, targetClass);
	}

	/**
	 * get mapper with mappings
	 * 
	 * @return mapper
	 */
	public Mapper<S, T> build() {
		if (this.methodMapper != null) {
			CompositeMapper<S, T> compositeMapper = new CompositeMapper<>(this.classDefinition);
			compositeMapper.registrateMapper(this.methodMapper);
			compositeMapper.registrateMapper(this.lambdaMapper);
			return compositeMapper;
		}
		return lambdaMapper;
	}

	/**
	 * Add mapping for one field
	 * 
	 * @param getter
	 *            - gets data from source object
	 * @param setter
	 *            - sets data to target object
	 * @return instance of mapperbuilder itself
	 */
	public <D> MapperBuilder<S, T> addMapping(Getter<S, D> getter, Setter<T, D> setter) {
		registerMapping(getter, setter);
		return this;
	}

	/**
	 * Add static mapping for one field
	 * 
	 * @param data
	 *            - data for setting to target object
	 * @param setter
	 *            - sets data to target object
	 * @return instance of MapperBuilder itself
	 */
	public <D> MapperBuilder<S, T> addMapping(D data, Setter<T, D> setter) {
		registerMapping(s -> data, setter);
		return this;
	}

	public MapperBuilder<S, T> automatic() {
		if(!this.storage.hasMapper(this.classDefinition)){
			initMapper();
			processManipulators(scanGetters(classDefinition.getSourceClass()),
					scanSetters(classDefinition.getTargetClass()));			
		}
		return this;
	}

	private <D> void registerMapping(Getter<S, D> getter, Setter<T, D> setter) {
		lambdaMapper.registerMapping(getter, setter);
	}

	private void processManipulators(Map<String, Method> getters, Map<String, Method> setters) {
		for (String methodName : getters.keySet()) {
			Method getter = getters.get(methodName);
			Method setter = setters.get(methodName);
			if (getter != null && setter != null) {
				Manipulator<S, T> manipulator = ManipulatorBuilder
						.start(this.classDefinition.getSourceClass(), this.classDefinition.getTargetClass(), storage)
						.getManipulator(getter, setter);
				if(manipulator!= null){
					methodMapper.addManipulator(manipulator);
				}
			}
		}
	}

	private Map<String, Method> scanSetters(Class<? extends T> clazz) {
		Map<String, Method> result = new HashMap<>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				Method method = propertyDescriptor.getWriteMethod();
				if (method != null) {
					result.put(propertyDescriptor.getName(), method);
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Map<String, Method> scanGetters(Class<? extends S> clazz) {
		Map<String, Method> result = new HashMap<>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				Method method = propertyDescriptor.getReadMethod();
				result.put(propertyDescriptor.getName(), method);
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return result;
	}

	public MapperBuilder<S, T> addMapper(Mapper<?, ?> subMapper) {
		this.storage.store(subMapper);
		return this;
	}

	private void initMapper() {
		if (this.methodMapper == null) {
			this.methodMapper = new MethodMapper<>(this.classDefinition);
		}
	}

	public void setStorage(MapperStorage storage) {
		this.storage = storage;
	}

}
