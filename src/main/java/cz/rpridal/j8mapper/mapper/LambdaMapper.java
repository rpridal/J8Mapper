package cz.rpridal.j8mapper.mapper;

import java.util.HashSet;
import java.util.Set;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.manipulator.IdentityManipulator;
import cz.rpridal.j8mapper.manipulator.Manipulator;
import cz.rpridal.j8mapper.manipulator.TransformerManipulator;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.Transformer;

/**
 * Mapper is responsible for mapping data from source object to target, it
 * provide maping collections
 * 
 * @author rpridal
 *
 * @param <SourceType>
 *            source type
 * @param <TargetType>
 *            target type
 */
public class LambdaMapper<SourceType, TargetType> extends AbstractMapper<SourceType, TargetType> {
	public LambdaMapper(ClassDefinition<SourceType, TargetType> classDefinition) {
		super(classDefinition);
	}

	protected final Set<Manipulator<SourceType, TargetType>> manipulators = new HashSet<Manipulator<SourceType, TargetType>>();

	public <DataType> void registerMapping(Getter<SourceType, DataType> getter, Setter<TargetType, DataType> setter) {
		manipulators.add(new IdentityManipulator<SourceType, TargetType, DataType>(getter, setter));
	}

	public <DataType> void registerMapping(DataType data, Setter<TargetType, DataType> setter) {
		manipulators.add(new IdentityManipulator<SourceType, TargetType, DataType>(s -> data, setter));
	}

	public <SourceDataType, TargetDataType> void registerMapping(Getter<SourceType, SourceDataType> getter,
			Setter<TargetType, TargetDataType> setter, Transformer<SourceDataType, TargetDataType> transformer) {
		manipulators.add(new TransformerManipulator<>(getter, setter, transformer));
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
	public TargetType map(SourceType source, TargetType target) {
		if (source == null) {
			return null;
		}
		manipulators.stream().forEach(m -> m.map(source, target));
		return target;
	}

	public void addManipulator(Manipulator<SourceType, TargetType> methodManipulator) {
		manipulators.add(methodManipulator);
	}
}