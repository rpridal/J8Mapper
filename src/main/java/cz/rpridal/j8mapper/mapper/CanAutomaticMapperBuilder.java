package cz.rpridal.j8mapper.mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import cz.rpridal.j8mapper.SimpleMapperStorage;
import cz.rpridal.j8mapper.manipulator.Manipulator;
import cz.rpridal.j8mapper.manipulator.ManipulatorBuilder;

public class CanAutomaticMapperBuilder<S, T> extends MapperBuilder<S, T> {
	
	private static final Logger LOGGER = Logger.getLogger(CanAutomaticMapperBuilder.class.getName());
	
	private MapperStorage storage = new SimpleMapperStorage();
	
	protected CanAutomaticMapperBuilder(Class<S> sourceClass, Class<T> targetClass) {
		super(sourceClass, targetClass);
	}
	
	
	public MapperBuilder<S, T> automatic() {
		if (!this.storage.hasMapper(this.classDefinition)) {
			initMapper();
			processManipulators(scanGetters(classDefinition.getSourceClass()),
					scanSetters(classDefinition.getTargetClass()));
		}
		return this;
	}
	
	private void processManipulators(Map<String, Method> getters, Map<String, Method> setters) {
		for (String methodName : getters.keySet()) {
			Method getter = getters.get(methodName);
			Method setter = setters.get(methodName);
			if (getter != null && setter != null) {
				Manipulator<S, T> manipulator = ManipulatorBuilder
						.start(this.classDefinition.getSourceClass(), this.classDefinition.getTargetClass(), storage)
						.getManipulator(getter, setter);
				if (manipulator != null) {
					methodMapper.addManipulator(manipulator);
				}
			} else {
				if (getter != null) {
					LOGGER.severe("Getter '" + getter.getName() + "' in class '" + getter.getDeclaringClass().getName()
							+ "' doesn't have corresponding setter in the target class '"
							+ classDefinition.getTargetClass().getName() + "'");
				}
			}
		}
	}
	
	private void initMapper() {
		if (this.methodMapper == null) {
			this.methodMapper = new MethodMapper<>(this.classDefinition);
		}
	}
	
	private Map<String, Method> scanGetters(Class<? extends S> clazz) {
		Map<String, Method> result = new HashMap<>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				if (!this.exclusionFields.contains(propertyDescriptor.getName())) {
					Method method = propertyDescriptor.getReadMethod();
					if (method != null) {
						result.put(propertyDescriptor.getName(), method);
					} else {
						LOGGER.severe("Field '" + propertyDescriptor.getName() + "' in class '" + clazz.getName()
								+ "' doesnt have setter");
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private Map<String, Method> scanSetters(Class<? extends T> clazz) {
		Map<String, Method> result = new HashMap<>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				if (!this.exclusionFields.contains(propertyDescriptor.getName())) {
					Method method = propertyDescriptor.getWriteMethod();
					if (method != null) {
						result.put(propertyDescriptor.getName(), method);
					} else {
						LOGGER.severe("Field '" + propertyDescriptor.getName() + "' in class '" + clazz.getName()
								+ "' doesnt have getter");
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return result;
	}

	public CanAutomaticMapperBuilder<S, T> addMapper(Mapper<?, ?> subMapper) {
		this.storage.store(subMapper);
		return this;
	}

	public void setStorage(MapperStorage storage) {
		this.storage = storage;
	}

	/**
	 * Exclude field from processing
	 * 
	 * @param fieldName
	 * @return MapperBuilder
	 */
	public CanAutomaticMapperBuilder<S, T> excludeField(String fieldName) {
		this.exclusionFields.add(fieldName);
		return this;
	}
}
