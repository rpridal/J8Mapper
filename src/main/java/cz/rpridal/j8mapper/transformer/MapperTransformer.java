package cz.rpridal.j8mapper.transformer;

import java.util.function.Supplier;

import cz.rpridal.j8mapper.manipulator.ObjectCache;
import cz.rpridal.j8mapper.mapper.Mapper;

public class MapperTransformer<SourceDataType, TargetDataType> implements Transformer<SourceDataType, TargetDataType> {
	private final Supplier<TargetDataType> supplier;
	private final Mapper<SourceDataType, TargetDataType> mapper;

	public MapperTransformer(Supplier<TargetDataType> supplier, Mapper<SourceDataType, TargetDataType> mapper) {
		super();
		this.supplier = supplier;
		this.mapper = mapper;
	}

	@Override
	public TargetDataType transform(SourceDataType source) {
		TargetDataType targetData = supplier.get();
		ObjectCache.saveObjects(source, targetData);
		return mapper.map(source, targetData);
	}
}