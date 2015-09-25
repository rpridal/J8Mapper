package cz.rpridal.j8mapper.manipulator;

import java.util.function.Supplier;

public final class ClassSupplier<TD> implements Supplier<TD> {
	
	private final Class<TD> clazz;
	
	public ClassSupplier(Class<TD> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public TD get() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
}