package cz.rpridal.j8mapper.transformer;

import java.util.function.Function;

@FunctionalInterface
public interface Transformer<SourceDataType, TargetDataType> extends Function<SourceDataType, TargetDataType> {
	TargetDataType transform(SourceDataType data);

	@Override
	default TargetDataType apply(SourceDataType t) {
		return transform(t);
	}

}
