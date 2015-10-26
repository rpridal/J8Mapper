package cz.rpridal.j8mapper.manipulator;

import java.util.logging.Logger;

import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.Transformer;

public class TransformerManipulator<SourceType, TargetType, SourceDataType, TargetDataType>
		implements Manipulator<SourceType, TargetType> {

	private static final Logger LOGGER = Logger.getLogger(TransformerManipulator.class.getName());

	private final Getter<SourceType, SourceDataType> getter;
	private final Setter<TargetType, TargetDataType> setter;
	private final Transformer<SourceDataType, TargetDataType> transformer;

	public TransformerManipulator(Getter<SourceType, SourceDataType> getter, Setter<TargetType, TargetDataType> setter,
			Transformer<SourceDataType, TargetDataType> transformer) {
		super();
		if (getter == null || setter == null || transformer == null) {
			throw new NullPointerException();
		}
		this.getter = getter;
		this.setter = setter;
		this.transformer = transformer;
	}

	@Override
	public void map(SourceType source, TargetType target) {
		if (source == null) {
			return;
		}
		SourceDataType data = null;
		try {
			data = getter.get(source);
		} catch (Exception e) {
			LOGGER.severe("Getter '" + getter.getClass().getName() + "' on object '" + source.getClass().getName()
					+ "' throw null pointer exeption ");
			return;
		}
		if (data == null) {
			return;
		}
		TargetDataType transformedData = transformer.transform(data);
		if (transformedData == null) {
			return;
		}
		setter.set(target, transformedData);
	}
}
