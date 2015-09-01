package cz.rpridal.j8mapper.manipulator;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.Transformer;

public class CollectionManipulator<S, T, SDL extends Collection<? extends SD>, TDL extends Collection<TD>, SD, TD> implements Manipulator<S, T> {

	private final Getter<S, SDL> getter;
	private final Setter<T, TDL> setter;
	private final Transformer<SD, TD> transformer;
	private final Supplier<TDL> supplier;
	
	public CollectionManipulator(Getter<S, SDL> getter, Setter<T, TDL> setter, Transformer<SD, TD> transformer, Supplier<TDL> supplier) {
		this.getter = getter;
		this.setter = setter;
		this.transformer = transformer;
		this.supplier = supplier;
	}
	
	@Override
	public void map(S source, T target) {
		SDL sdl = getter.get(source);
		if(sdl == null){
			return;
		}
		Stream<TD> map = sdl.stream().map(transformer::transform);
		TDL collect = map.collect(Collectors.toCollection(supplier));
		setter.set(target, collect);
	}
}
