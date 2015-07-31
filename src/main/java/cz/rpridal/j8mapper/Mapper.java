package cz.rpridal.j8mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Mapper is responsible for mapping data from source object to target, it
 * provide maping collections
 * 
 * @author rpridal
 *
 * @param <S> source type
 * @param <T> target type
 */
public class Mapper<S, T> {
	private final Set<Manipulator<S, T, ?>> manipulators = new HashSet<Manipulator<S, T, ?>>();

	public <D> void registerMapping(Getter<S, D> getter, Setter<T, D> setter) {
		manipulators.add(new Manipulator<S, T, D>(getter, setter));
	}

	public <D> void registerMapping(D data, Setter<T, D> setter) {
		manipulators.add(new Manipulator<S, T, D>(s -> data, setter));
	}

	/**
	 * Do map for instance object - if you have instance of each object
	 * 
	 * @param source
	 *            object
	 * @param target
	 *            object
	 * @return target object - the same instance as parameter
	 */
	public T map(S source, T target) {
		manipulators.stream().forEach(m -> m.map(source, target));
		return target;
	}

	/**
	 * Do map if you have supplier
	 * 
	 * @param source
	 * @param supplier
	 *            eg. Target::new
	 * @return return new instance of target object
	 */
	public T map(S source, Supplier<T> supplier) {
		T target = supplier.get();
		return map(source, target);
	}

	/**
	 * Do map if you have collection of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	public Stream<T> map(Collection<S> source, Supplier<T> supplier) {
		return map(source.stream(), supplier);
	}

	/**
	 * Do map if you have stream of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	public Stream<T> map(Stream<S> source, Supplier<T> supplier) {
		return source.map(s -> map(s, supplier.get()));
	}

}