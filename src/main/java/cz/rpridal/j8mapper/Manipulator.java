package cz.rpridal.j8mapper;

/**
 * Object for connection getters and setters 
 * @author rpridal
 *
 * @param <S> source type
 * @param <T> target type
 * @param <D> data type
 */
class Manipulator<S, T, D> {
	private Getter<S, D> getter;
	private Setter<T, D> setter;

	public Manipulator(Getter<S, D> getter, Setter<T, D> setter) {
		super();
		this.getter = getter;
		this.setter = setter;
	}

	public Getter<S, D> getGetter() {
		return getter;
	}

	public Setter<T, D> getSetter() {
		return setter;
	}

	public void map(S source, T target) {
		D data = getter.apply(source);
		setter.set(target, data);
	}
}