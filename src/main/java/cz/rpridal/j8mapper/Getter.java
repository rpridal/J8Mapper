package cz.rpridal.j8mapper;

import java.util.function.Function;

public interface Getter<S, D> extends Function<S, D> {
	D get(S source);

	@Override
    public default D apply(S t){
    	return get(t);
    }
}