package cz.rpridal.j8mapper.setter;

import java.util.function.BiConsumer;

/**
 * Marker interface for BiConsumer interface
 * 
 * @author rpridal
 *
 * @param <TargetType>
 *            target type
 * @param <DataType>
 *            data type
 */
@FunctionalInterface
public interface Setter<TargetType, DataType> extends BiConsumer<TargetType, DataType> {
	void set(TargetType target, DataType data);

	@Override
	public default void accept(TargetType t, DataType u) {
		set(t, u);
	}
}