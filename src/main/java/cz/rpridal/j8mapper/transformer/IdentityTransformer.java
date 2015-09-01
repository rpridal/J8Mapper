package cz.rpridal.j8mapper.transformer;

public class IdentityTransformer<S, T> implements Transformer<S, T> {

	@SuppressWarnings("unchecked")
	@Override
	public T transform(S data) {
		return (T) data;
	}
 
}
