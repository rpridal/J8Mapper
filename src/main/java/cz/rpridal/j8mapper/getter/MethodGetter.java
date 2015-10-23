package cz.rpridal.j8mapper.getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MethodGetter<SourceType, DataType> implements Getter<SourceType, DataType> {
	private static final Logger LOGGER = Logger.getLogger(MethodGetter.class.getName());
	private final Method method;

	public MethodGetter(Method method) {
		super();
		this.method = method;
		if (method.getParameters().length != 0) {
			throw new IllegalArgumentException("Getter method must have 0 parameter");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataType get(SourceType source) {
		DataType data = null;
		try {
			data = (DataType) method.invoke(source);
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
		return data;
	}
}
