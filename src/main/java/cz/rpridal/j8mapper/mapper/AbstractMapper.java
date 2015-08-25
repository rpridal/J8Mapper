package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.manipulator.ObjectCache;

public abstract class AbstractMapper<S, T> implements Mapper<S, T> {
	
	private ClassDefinition<S, T> classDefinition;
	
	public AbstractMapper(ClassDefinition<S, T> classDefinition) {
		super();
		this.classDefinition = classDefinition;
	}
	
	public ClassDefinition<S, T> getClassDefinition() {
		return classDefinition;
	}

	/* (non-Javadoc)
	 * @see cz.rpridal.j8mapper.Mapper#map(S, java.util.function.Supplier)
	 */
	@Override
	public T map(S source, Supplier<T> supplier) {
		T target = supplier.get();
		ObjectCache.saveObjects(source, target);
		return map(source, target);
	}

	/* (non-Javadoc)
	 * @see cz.rpridal.j8mapper.Mapper#map(java.util.Collection, java.util.function.Supplier)
	 */
	@Override
	public Stream<T> map(Collection<? extends S> source, Supplier<T> supplier) {
		return map(source.stream(), supplier);
	}

	/* (non-Javadoc)
	 * @see cz.rpridal.j8mapper.Mapper#map(java.util.stream.Stream, java.util.function.Supplier)
	 */
	@Override
	public Stream<T> map(Stream<? extends S> source, Supplier<T> supplier) {
		return source.map(s -> map(s, supplier.get()));
	}
	
	/* (non-Javadoc)
	 * @see cz.rpridal.j8mapper.Mapper#map(S, T)
	 */
	@Override
	public abstract T map(S source, T target);

}