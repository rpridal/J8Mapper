package cz.rpridal.j8mapper.setter;

import java.util.function.BiConsumer;

/**
 * Marker interface for BiConsumer interface 
 * @author rpridal
 *
 * @param <T> target type
 * @param <D> data type
 */
@FunctionalInterface
public interface Setter<T, D> extends BiConsumer<T, D>{
    void set(T target, D data);
    @Override
	public default void accept(T t, D u){
    	set(t, u);
    }
}