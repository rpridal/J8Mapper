package cz.rpridal.j8mapper.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;

public interface Mapper<SourceType, TargetType> {

	/**
	 * Do map if you have supplier
	 * 
	 * @param source
	 * @param supplier
	 *            eg. Target::new
	 * @return return new instance of target object
	 */
	TargetType map(SourceType source, Supplier<TargetType> supplier);

	/**
	 * Do map if you have collection of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	Stream<TargetType> map(Collection<? extends SourceType> source, Supplier<TargetType> supplier);

	Stream<TargetType> map(Collection<? extends SourceType> source);

	/**
	 * Do map if you have stream of source objects.
	 * 
	 * @param source
	 * @param supplier
	 *            supplier eg. Target::new
	 * @return Stream of target objects
	 */
	Stream<TargetType> map(Stream<? extends SourceType> source, Supplier<TargetType> supplier);

	TargetType map(SourceType source, TargetType target);

	ClassDefinition<SourceType, TargetType> getClassDefinition();

	TargetType map(SourceType source);

	Set<TargetType> mapToSet(Collection<? extends SourceType> source);

	List<TargetType> mapToList(Collection<? extends SourceType> source);

	<DataType> Mapper<SourceType, TargetType> addAdHocMapping(Getter<SourceType, DataType> getter,
			Setter<TargetType, DataType> setter);

	<DataType> Mapper<SourceType, TargetType> addAdHocMapping(DataType value, Setter<TargetType, DataType> setter);

	<DataType> Mapper<SourceType, TargetType> addAdHocMapping(Supplier<DataType> supplier,
			Setter<TargetType, DataType> setter);
}