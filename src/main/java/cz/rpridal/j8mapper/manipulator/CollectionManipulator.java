package cz.rpridal.j8mapper.manipulator;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.Transformer;

public class CollectionManipulator<SourceType, TargetType, SourceDataCollectionType extends Collection<? extends SourceDataType>, TargetDataCollectionType extends Collection<TargetDataType>, SourceDataType, TargetDataType>
		implements Manipulator<SourceType, TargetType> {

	private final Getter<SourceType, SourceDataCollectionType> getter;
	private final Setter<TargetType, TargetDataCollectionType> setter;
	private final Transformer<SourceDataType, TargetDataType> transformer;
	private final Supplier<TargetDataCollectionType> supplier;

	public CollectionManipulator(Getter<SourceType, SourceDataCollectionType> getter,
			Setter<TargetType, TargetDataCollectionType> setter,
			Transformer<SourceDataType, TargetDataType> transformer, Supplier<TargetDataCollectionType> supplier) {
		this.getter = getter;
		this.setter = setter;
		this.transformer = transformer;
		this.supplier = supplier;
	}

	@Override
	public void map(SourceType source, TargetType target) {
		SourceDataCollectionType sdl = getter.get(source);
		if (sdl == null) {
			return;
		}
		Stream<TargetDataType> map = sdl.stream().map(transformer::transform);
		TargetDataCollectionType collect = map.collect(Collectors.toCollection(supplier));
		setter.set(target, collect);
	}
}
