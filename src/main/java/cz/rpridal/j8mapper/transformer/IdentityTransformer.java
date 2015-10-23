package cz.rpridal.j8mapper.transformer;

public class IdentityTransformer<SourceType, TargetType> implements Transformer<SourceType, TargetType> {

	@SuppressWarnings("unchecked")
	@Override
	public TargetType transform(SourceType data) {
		return (TargetType) data;
	}

}
