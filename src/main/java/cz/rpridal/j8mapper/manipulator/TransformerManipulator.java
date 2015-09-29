package cz.rpridal.j8mapper.manipulator;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.Transformer;

public class TransformerManipulator<S, T, SD, TD> implements Manipulator<S, T> {

	private final Getter<S, SD> getter;
	private final Setter<T, TD> setter;
	private final Transformer<SD, TD> transformer;

	public TransformerManipulator(Getter<S, SD> getter, Setter<T, TD> setter, Transformer<SD, TD> transformer) {
		super();
		if (getter == null || setter == null || transformer == null) {
			throw new NullPointerException();
		}
		this.getter = getter;
		this.setter = setter;
		this.transformer = transformer;
	}

	@Override
	public void map(S source, T target) {
		if (source == null) {
			return;
		}
		SD data = getter.get(source);
		if (data == null) {
			return;
		}
		TD transformedData = transformer.transform(data);
		if (transformedData == null) {
			return;
		}
		setter.set(target, transformedData);
	}
}
