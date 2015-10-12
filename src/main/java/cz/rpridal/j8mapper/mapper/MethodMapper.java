package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;

public class MethodMapper<S, T> extends LambdaMapper<S, T> {

	private Map<ClassDefinition<?, ?>, Mapper<?, ?>> mappers = new HashMap<>();

	public MethodMapper(ClassDefinition<S, T> classDefinition) {
		super(classDefinition);
	}

	public void addSubMapper(Mapper<?, ?> subMapper) {
		mappers.put(subMapper.getClassDefinition(), subMapper);
	}
	
	@Override
	public Stream<T> map(Collection<? extends S> source, Supplier<T> supplier) {
		if(source == null){
			return null;
		}
		return super.map(source, supplier);
	}

}
