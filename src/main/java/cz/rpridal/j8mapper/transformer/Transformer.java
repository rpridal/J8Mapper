package cz.rpridal.j8mapper.transformer;

import java.util.function.Function;

@FunctionalInterface
public interface Transformer<SD, TD> extends Function<SD, TD> {
	TD transform(SD data);
	
	@Override
	default TD apply(SD t) {
		return transform(t);
	}
	
	
}
