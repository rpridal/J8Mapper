package cz.rpridal.j8mapper.transformer;

import java.util.Arrays;
import java.util.Optional;

public class EnumTransformer<SD extends Enum<SD>, TD extends Enum<TD>> implements Transformer<SD, TD> {

	private final Class<SD> sourceClass;
	private final Class<TD> targetClass;

	public EnumTransformer(Class<SD> sourceClass, Class<TD> targetClass) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	@Override
	public TD transform(SD data) {
		String textData = data.name();
		TD[] targetEnums = targetClass.getEnumConstants();
		if (targetEnums == null) {
			return null;
		}
		Optional<TD> findFirst = Arrays.stream(targetEnums).filter(s -> s.name().equals(textData)).findFirst();
		if (findFirst.isPresent()) {
			return findFirst.get();
		}
		return null;
	}
}
