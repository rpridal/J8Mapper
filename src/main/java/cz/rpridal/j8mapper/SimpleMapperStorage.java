package cz.rpridal.j8mapper;

import java.util.HashMap;
import java.util.Map;

import cz.rpridal.j8mapper.mapper.CompositeMapper;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperStorage;

public class SimpleMapperStorage implements MapperStorage {

	private final Map<ClassDefinition<?, ?>, Mapper<?, ?>> map = new HashMap<ClassDefinition<?, ?>, Mapper<?, ?>>();

	@Override
	public <SourceType, TargetType> void store(Mapper<SourceType, TargetType> mapper) {
		ClassDefinition<SourceType, TargetType> classDefinition = mapper.getClassDefinition();
		Mapper<SourceType, TargetType> getMapper = this.get(classDefinition);
		if (getMapper == null) {
			map.put(classDefinition, mapper);
		} else if (getMapper instanceof CompositeMapper) {
			CompositeMapper<SourceType, TargetType> compositeMapper = (CompositeMapper<SourceType, TargetType>) getMapper;
			compositeMapper.registrateMapper(mapper);
		} else {
			CompositeMapper<SourceType, TargetType> compositeMapper = new CompositeMapper<SourceType, TargetType>(
					mapper.getClassDefinition());
			compositeMapper.registrateMapper(getMapper);
			compositeMapper.registrateMapper(mapper);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <SourceType, TargetType> Mapper<SourceType, TargetType> get(
			ClassDefinition<SourceType, TargetType> classDefinition) {
		return (Mapper<SourceType, TargetType>) map.get(classDefinition);
	}

	@Override
	public <SourceType, TargetType> boolean hasMapper(ClassDefinition<SourceType, TargetType> classDefinition) {
		return map.containsKey(classDefinition);
	}
}
