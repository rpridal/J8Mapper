package cz.rpridal.j8mapper.getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodGetter<S, D> implements Getter<S, D> {
	private final Method method;

	public MethodGetter(Method method) {
		super();
		this.method = method;
		if(method.getParameters().length!=0){
			throw new IllegalArgumentException("Getter method must have 0 parameter");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public D get(S source) {
		D data = null;
		try {
			data = (D) method.invoke(source);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
