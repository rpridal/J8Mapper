package cz.rpridal.j8mapper.transformer;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class EnumTransformer<SourceData extends Enum<SourceData>, TargetData extends Enum<TargetData>>
		implements Transformer<SourceData, TargetData> {

	private static final Logger LOGGER = Logger.getLogger(EnumTransformer.class.getName());

	private final Class<SourceData> sourceClass;
	private final Class<TargetData> targetClass;

	public EnumTransformer(Class<SourceData> sourceClass, Class<TargetData> targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	@Override
	public TargetData transform(SourceData data) {
		String textData = data.name();
		TargetData[] targetEnums = targetClass.getEnumConstants();
		if (targetEnums == null) {
			return null;
		}
		Optional<TargetData> findFirst = Arrays.stream(targetEnums).filter(s -> s.name().equals(textData)).findFirst();
		if (findFirst.isPresent()) {
			return findFirst.get();
		}
		LOGGER.severe("Can't find '" + textData + "' in target enum '" + targetClass.getName() + "'");
		return null;
	}
}
