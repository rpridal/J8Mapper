package cz.rpridal.j8mapper.getter;

import java.util.function.Function;

/**
 * Marker interface for Function interface
 * @author rpridal
 *
 * @param <S> source type
 * @param <D> data type
 */
public interface Getter<S, D> extends Function<S, D> {
	D get(S source);

	@Override
    public default D apply(S t){
    	return get(t);
    }
}