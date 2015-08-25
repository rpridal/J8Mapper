package cz.rpridal.j8mapper.setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodSetter<T, D> implements Setter<T, D> {
	private final Method method;

	public MethodSetter(Method method) {
		super();
		this.method = method;
		if(method.getParameters().length!=1){
			throw new IllegalArgumentException("Setter method must have 1 parameter");
		}
	}
	
	@Override
	public void set(T target, D data) {
		if(data == null){
			return;
		}
		try {
			method.invoke(target, data);
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
	}
}
