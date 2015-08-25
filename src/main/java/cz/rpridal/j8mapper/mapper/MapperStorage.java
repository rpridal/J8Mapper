package cz.rpridal.j8mapper.mapper;

import cz.rpridal.j8mapper.ClassDefinition;

public interface MapperStorage {
	public <S, T> Mapper<S, T> get(ClassDefinition<S, T> classDefinition);
	public <S, T> void store(Mapper<S, T> mapper);
	public <S, T> boolean hasMapper(ClassDefinition<S, T> classDefinition);
}
