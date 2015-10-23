package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;

public class MethodMapper<SourceType, TargetType> extends LambdaMapper<SourceType, TargetType> {

	private Map<ClassDefinition<?, ?>, Mapper<?, ?>> mappers = new HashMap<>();

	public MethodMapper(ClassDefinition<SourceType, TargetType> classDefinition) {
		super(classDefinition);
	}

	public void addSubMapper(Mapper<?, ?> subMapper) {
		mappers.put(subMapper.getClassDefinition(), subMapper);
	}

	@Override
	public Stream<TargetType> map(Collection<? extends SourceType> source, Supplier<TargetType> supplier) {
		if (source == null) {
			return null;
		}
		return super.map(source, supplier);
	}

}
