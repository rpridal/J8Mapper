package cz.rpridal.j8mapper.mapper;

import java.util.LinkedList;
import java.util.List;

import cz.rpridal.j8mapper.ClassDefinition;

public class CompositeMapper<SourceType, TargetType> extends AbstractMapper<SourceType, TargetType> {

	public CompositeMapper(ClassDefinition<SourceType, TargetType> classDefinition) {
		super(classDefinition);
	}

	private List<Mapper<SourceType, TargetType>> mappers = new LinkedList<>();

	public void registrateMapper(Mapper<SourceType, TargetType> mapper) {
		if (mapper != null) {
			mappers.add(mapper);
		}
	}

	@Override
	public TargetType map(SourceType source, TargetType target) {
		if (source == null) {
			return null;
		}
		mappers.stream().forEach(m -> m.map(source, target));
		return target;
	}

}
