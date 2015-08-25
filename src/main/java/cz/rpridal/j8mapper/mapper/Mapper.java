package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;

public interface Mapper<S, T> {

	/**
	 * Do map if you have supplier
	 * 
	 * @param source
	 * @param supplier
	 *            eg. Target::new
	 * @return return new instance of target object
	 */
	T map(S source, Supplier<T> supplier);

	/**
	 * Do map if you have collection of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	Stream<T> map(Collection<? extends S> source, Supplier<T> supplier);

	/**
	 * Do map if you have stream of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	Stream<T> map(Stream<? extends S> source, Supplier<T> supplier);

	T map(S source, T target);
	
	ClassDefinition<S, T> getClassDefinition();

}