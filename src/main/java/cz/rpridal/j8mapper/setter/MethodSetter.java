package cz.rpridal.j8mapper.setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MethodSetter<TargetType, DataType> implements Setter<TargetType, DataType> {
	private static final Logger LOGGER = Logger.getLogger(MethodSetter.class.getName());
	private final Method method;

	public MethodSetter(Method method) {
		super();
		this.method = method;
		if (method.getParameters().length != 1) {
			throw new IllegalArgumentException("Setter method must have 1 parameter");
		}
	}

	@Override
	public void set(TargetType target, DataType data) {
		if (data == null) {
			return;
		}
		try {
			method.invoke(target, data);
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
