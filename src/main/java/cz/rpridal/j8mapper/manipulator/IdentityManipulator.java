package cz.rpridal.j8mapper.manipulator;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;

/**
 * Object for connection getters and setters
 * 
 * @author rpridal
 *
 * @param <SourceType>
 *            source type
 * @param <TargetType>
 *            target type
 * @param <DataType>
 *            data type
 */
public class IdentityManipulator<SourceType, TargetType, DataType>
		extends TransformerManipulator<SourceType, TargetType, DataType, DataType>
		implements Manipulator<SourceType, TargetType> {

	public IdentityManipulator(Getter<SourceType, DataType> getter, Setter<TargetType, DataType> setter) {
		super(getter, setter, d -> d);
	}
}