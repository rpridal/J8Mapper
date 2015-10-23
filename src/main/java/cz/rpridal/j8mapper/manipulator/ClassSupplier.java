package cz.rpridal.j8mapper.manipulator;

import java.util.function.Supplier;

public final class ClassSupplier<TargetData> implements Supplier<TargetData> {

	private final Class<TargetData> clazz;

	public ClassSupplier(Class<TargetData> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public TargetData get() {
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