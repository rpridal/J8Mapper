package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.manipulator.ClassSupplier;
import cz.rpridal.j8mapper.manipulator.ObjectCache;

public abstract class AbstractMapper<SourceType, TargetType> implements Mapper<SourceType, TargetType> {

	private ClassDefinition<SourceType, TargetType> classDefinition;

	public AbstractMapper(ClassDefinition<SourceType, TargetType> classDefinition) {
		super();
		this.classDefinition = classDefinition;
	}

	public ClassDefinition<SourceType, TargetType> getClassDefinition() {
		return classDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.rpridal.j8mapper.Mapper#map(S, java.util.function.Supplier)
	 */
	@Override
	public TargetType map(SourceType source, Supplier<TargetType> supplier) {
		if (source == null) {
			return null;
		}
		TargetType target = supplier.get();
		ObjectCache.saveObjects(source, target);
		return map(source, target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.rpridal.j8mapper.Mapper#map(java.util.Collection,
	 * java.util.function.Supplier)
	 */
	@Override
	public Stream<TargetType> map(Collection<? extends SourceType> source, Supplier<TargetType> supplier) {
		if (source == null) {
			return Stream.empty();
		}
		return map(source.stream(), supplier);
	}

	@Override
	public Stream<TargetType> map(Collection<? extends SourceType> source) {
		if (source == null) {
			return Stream.empty();
		}
		Supplier<TargetType> supplier = new ClassSupplier<>(this.classDefinition.getTargetClass());
		return map(source.stream(), supplier);
	}

	@Override
	public Set<TargetType> mapToSet(Collection<? extends SourceType> source) {
		return this.map(source).collect(Collectors.toSet());
	}

	@Override
	public List<TargetType> mapToList(Collection<? extends SourceType> source) {
		return this.map(source).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.rpridal.j8mapper.Mapper#map(java.util.stream.Stream,
	 * java.util.function.Supplier)
	 */
	@Override
	public Stream<TargetType> map(Stream<? extends SourceType> source, Supplier<TargetType> supplier) {
		if (source == null) {
			return Stream.empty();
		}
		return source.map(s -> map(s, supplier.get()));
	}

	@Override
	public TargetType map(SourceType source) {
		if (source == null) {
			return null;
		}
		return map(source, new ClassSupplier<>(this.classDefinition.getTargetClass()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.rpridal.j8mapper.Mapper#map(S, T)
	 */
	@Override
	public abstract TargetType map(SourceType source, TargetType target);

}