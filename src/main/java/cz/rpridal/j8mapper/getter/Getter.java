package cz.rpridal.j8mapper.getter;

import java.util.function.Function;

/**
 * Marker interface for Function interface
 * 
 * @author rpridal
 *
 * @param <SourceType>
 *            source type
 * @param <DataType>
 *            data type
 */
public interface Getter<SourceType, DataType> extends Function<SourceType, DataType> {
	DataType get(SourceType source);

	@Override
	public default DataType apply(SourceType source) {
		return get(source);
	}
}