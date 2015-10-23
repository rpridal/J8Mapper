package cz.rpridal.j8mapper.mapper;

import cz.rpridal.j8mapper.ClassDefinition;

public interface MapperStorage {
	public <SourceType, TargetType> Mapper<SourceType, TargetType> get(
			ClassDefinition<SourceType, TargetType> classDefinition);

	public <SourceType, TargetType> void store(Mapper<SourceType, TargetType> mapper);

	public <SourceType, TargetType> boolean hasMapper(ClassDefinition<SourceType, TargetType> classDefinition);
}
