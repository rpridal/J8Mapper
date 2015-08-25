package cz.rpridal.j8mapper.mapper;

import java.util.LinkedList;
import java.util.List;

import cz.rpridal.j8mapper.ClassDefinition;

public class CompositeMapper<S, T> extends AbstractMapper<S, T> {

	public CompositeMapper(ClassDefinition<S, T> classDefinition) {
		super(classDefinition);
	}

	private List<Mapper<S, T>> mappers = new LinkedList<>();
	
	public void registrateMapper(Mapper<S, T> mapper){
		if(mapper != null){
			mappers.add(mapper);
		}
	}
	
	@Override
	public T map(S source, T target) {
		mappers.stream().forEach(m -> m.map(source, target));
		return target;
	}

}
