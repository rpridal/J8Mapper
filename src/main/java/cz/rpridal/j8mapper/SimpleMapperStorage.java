package cz.rpridal.j8mapper;

import java.util.HashMap;
import java.util.Map;

import cz.rpridal.j8mapper.mapper.CompositeMapper;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperStorage;

public class SimpleMapperStorage implements MapperStorage {

	private final Map<ClassDefinition<?, ?>, Mapper<?, ?>> map = new HashMap<ClassDefinition<?, ?>, Mapper<?, ?>>();
	
	@Override
	public <S, T> void store(Mapper<S, T> mapper) {
		ClassDefinition<S, T> classDefinition = mapper.getClassDefinition();
		Mapper<S, T> getMapper = this.get(classDefinition);
		if(getMapper == null){
			map.put(classDefinition, mapper);
		}else if(getMapper instanceof CompositeMapper){
			CompositeMapper<S, T> compositeMapper = (CompositeMapper<S, T>)getMapper;
			compositeMapper.registrateMapper(mapper);
		}else {
			CompositeMapper<S, T> compositeMapper = new CompositeMapper<S, T>(mapper.getClassDefinition());
			compositeMapper.registrateMapper(getMapper);
			compositeMapper.registrateMapper(mapper);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S, T> Mapper<S, T> get(ClassDefinition<S, T> classDefinition) {
		return (Mapper<S, T>) map.get(classDefinition);
	}

	@Override
	public <S, T> boolean hasMapper(ClassDefinition<S, T> classDefinition) {
		return map.containsKey(classDefinition);
	}
}
