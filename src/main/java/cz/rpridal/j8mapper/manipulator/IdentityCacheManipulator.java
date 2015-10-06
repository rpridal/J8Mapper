package cz.rpridal.j8mapper.manipulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentityCacheManipulator<S, T> implements Manipulator<S, T> {
	
	private static final Logger LOGGER = Logger.getLogger(IdentityCacheManipulator.class.getName());
	private final Method getter;
	private final Method setter;

	public IdentityCacheManipulator(Method getter, Method setter) {
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void map(S source, T target) {
		try {
			Object sourceData = getter.invoke(source);
			if(sourceData == null){
				return;
			}
			Object targetObject = ObjectCache.getTargetObject(sourceData);
			if(targetObject == null){
				return;
			}
			setter.invoke(target, targetObject);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.FINE, "IllegalAccessException", e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.FINE, "IllegalArgumentException", e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.FINE, "InvocationTargetException", e);
		}
	}

}
