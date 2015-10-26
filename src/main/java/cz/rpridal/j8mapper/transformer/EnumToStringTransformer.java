package cz.rpridal.j8mapper.transformer;

public class EnumToStringTransformer<SourceData extends Enum<SourceData>> implements Transformer<SourceData, String> {

	private final Class<SourceData> sourceClass;

	public EnumToStringTransformer(Class<SourceData> sourceClass) {
		this.sourceClass = sourceClass;
	}

	@Override
	public String transform(SourceData data) {
		return data.name();
	}
}
