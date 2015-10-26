package cz.rpridal.j8mapper.transformer;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class StringToEnumTransformer<TargetData extends Enum<TargetData>> implements Transformer<String, TargetData> {

	private static final Logger LOGGER = Logger.getLogger(StringToEnumTransformer.class.getName());

	private final Class<TargetData> targetClass;

	public StringToEnumTransformer(Class<TargetData> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public TargetData transform(String data) {
		TargetData[] targetEnums = targetClass.getEnumConstants();
		if (targetEnums == null) {
			return null;
		}
		Optional<TargetData> findFirst = Arrays.stream(targetEnums).filter(s -> s.name().equals(data)).findFirst();
		if (findFirst.isPresent()) {
			return findFirst.get();
		}
		LOGGER.severe("Can't find '" + data + "' in target enum '" + targetClass.getName() + "'");
		return null;
	}
}
