package cz.rpridal.j8mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import cz.rpridal.j8mapper.manipulator.Manipulator;

public class SimpleMethodManipulator<S, T> implements Manipulator<S, T> {

	private final Method sourceMethod;
	private final Method targetMethod;

	public SimpleMethodManipulator(Method sourceMethod, Method targetMethod) {
		super();
		this.sourceMethod = sourceMethod;
		this.targetMethod = targetMethod;
	}

	@Override
	public void map(S source, T target) {
		try {
			Object data = sourceMethod.invoke(source);
			targetMethod.invoke(target, data);
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

	public boolean isApplicable() {
		Class<?>[] parameterTypes = targetMethod.getParameterTypes();
		if(parameterTypes.length != 1){
			return false;
		}
		if (!sourceMethod.getReturnType().equals(parameterTypes[0])) {
			return false;
		}
		TypeVariable<?>[] sourceTypeParameters = sourceMethod.getReturnType().getTypeParameters();
		TypeVariable<?>[] targetTypeParameters = targetMethod.getReturnType().getTypeParameters();
		if(sourceTypeParameters == null && targetTypeParameters == null){
			return true;
		}
		return Arrays.equals(sourceTypeParameters,
				targetTypeParameters);
	}

}
