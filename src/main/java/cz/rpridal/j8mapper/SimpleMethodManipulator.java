package cz.rpridal.j8mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.rpridal.j8mapper.manipulator.Manipulator;

public class SimpleMethodManipulator<S, T> implements Manipulator<S, T> {

	private static final Logger LOGGER = Logger.getLogger(SimpleMethodManipulator.class.getName());
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
			LOGGER.log(Level.FINE, "IllegalAccessException", e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.FINE, "IllegalArgumentException", e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.FINE, "IllegalArgumentException", e);
		}
	}

	public boolean isApplicable() {
		Class<?>[] parameterTypes = targetMethod.getParameterTypes();
		if(parameterTypes.length != 1){
			return false;
		}
		Class<?> sourceClass = sourceMethod.getReturnType();
		Class<?> targetClass = parameterTypes[0];
		if (!sourceClass.equals(targetClass)) {
			if(sourceClass.isPrimitive() || targetClass.isPrimitive()){
				return isRelated(sourceClass, targetClass);
			}
			return false;
		}
		TypeVariable<?>[] sourceTypeParameters = sourceClass.getTypeParameters();
		TypeVariable<?>[] targetTypeParameters = targetMethod.getReturnType().getTypeParameters();
		if(sourceTypeParameters == null && targetTypeParameters == null){
			return true;
		}
		
		return Arrays.equals(sourceTypeParameters,
				targetTypeParameters);
	}
	
	private static Map<Class<?>, Class<?>> primitivesToBoxed = new HashMap<>();

	static {
		primitivesToBoxed.put(Integer.TYPE, Integer.class);
		primitivesToBoxed.put(Long.TYPE, Long.class);
		primitivesToBoxed.put(Double.TYPE, Double.class);
		primitivesToBoxed.put(Float.TYPE, Float.class);
		primitivesToBoxed.put(Boolean.TYPE, Boolean.class);
		primitivesToBoxed.put(Character.TYPE, Character.class);
		primitivesToBoxed.put(Byte.TYPE, Byte.class);
		primitivesToBoxed.put(Short.TYPE, Short.class);
	}

	protected static <S, T> boolean isRelated(Class<S> class1, Class<T> class2) {
		boolean result = primitivesToBoxed.get(class1).equals(class2);
		if(result){
			return true;
		} else {
			return primitivesToBoxed.get(class2).equals(class1);
		}
	}	

}
