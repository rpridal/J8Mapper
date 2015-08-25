package cz.rpridal.j8mapper.mapper;

import java.util.HashSet;
import java.util.Set;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.manipulator.IdentityManipulator;
import cz.rpridal.j8mapper.manipulator.Manipulator;
import cz.rpridal.j8mapper.setter.Setter;

/**
 * Mapper is responsible for mapping data from source object to target, it
 * provide maping collections
 * 
 * @author rpridal
 *
 * @param <S> source type
 * @param <T> target type
 */
public class LambdaMapper<S, T> extends AbstractMapper<S, T> {
	public LambdaMapper(ClassDefinition<S, T> classDefinition) {
		super(classDefinition);
	}

	protected final Set<Manipulator<S, T>> manipulators = new HashSet<Manipulator<S, T>>();

	public <D> void registerMapping(Getter<S, D> getter, Setter<T, D> setter) {
		manipulators.add(new IdentityManipulator<S, T, D>(getter, setter));
	}

	public <D> void registerMapping(D data, Setter<T, D> setter) {
		manipulators.add(new IdentityManipulator<S, T, D>(s -> data, setter));
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

	public void addManipulator(Manipulator<S, T> methodManipulator) {
		manipulators.add(methodManipulator);
	}
}