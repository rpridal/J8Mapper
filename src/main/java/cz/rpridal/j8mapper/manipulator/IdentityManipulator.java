package cz.rpridal.j8mapper.manipulator;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;

/**
 * Object for connection getters and setters 
 * @author rpridal
 *
 * @param <S> source type
 * @param <T> target type
 * @param <D> data type
 */
public class IdentityManipulator<S, T, D> extends TransformerManipulator<S, T, D, D> implements Manipulator<S, T> {

	public IdentityManipulator(Getter<S, D> getter, Setter<T, D> setter) {
		super(getter, setter, d -> d);
	}
}