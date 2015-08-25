package cz.rpridal.j8mapper.transformer;

import java.util.function.Supplier;

import cz.rpridal.j8mapper.manipulator.ObjectCache;
import cz.rpridal.j8mapper.mapper.Mapper;

public class MapperTransformer<SD, TD> implements Transformer<SD, TD> {
	private final Supplier<TD> supplier;
	private final Mapper<SD, TD> mapper;
	
	public MapperTransformer(Supplier<TD> supplier, Mapper<SD, TD> mapper) {
		super();
		this.supplier = supplier;
		this.mapper = mapper;
	}

	@Override
	public TD transform(SD source) {
		TD targetData = supplier.get();
		ObjectCache.saveObjects(source, targetData);
		return mapper.map(source, targetData);
	}
}