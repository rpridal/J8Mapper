package cz.rpridal.j8mapper;

/**
 * Builder for mapper 
 * @author rpridal
 *
 * @param <S> source type
 * @param <T> target type
 */
public class MapperBuilder<S, T> {

	private Mapper<S, T> mapper;

	private MapperBuilder() {
		mapper = new Mapper<S, T>();
	}

	/**
	 * this method initialize mapper - it is for specification of types
	 * MapperBuilder
	 * 
	 * @param souce
	 *            class eg. Source.class
	 * @param target
	 *            class eg. Target.class
	 * @return new MapperBuilder
	 */
	public static <S, T> MapperBuilder<S, T> start(Class<S> s, Class<T> t) {
		return new MapperBuilder<S, T>();
	}

	/**
	 * get mapper with mappings
	 * 
	 * @return mapper
	 */
	public Mapper<S, T> build() {
		return mapper;
	}

	/**
	 * Add mapping for one field
	 * 
	 * @param getter
	 *            - gets data from source object
	 * @param setter
	 *            - sets data to target object
	 * @return instance of mapperbuilder itself
	 */
	public <D> MapperBuilder<S, T> addMapping(Getter<S, D> getter, Setter<T, D> setter) {
		registerMapping(getter, setter);
		return this;
	}

	/**
	 * Add static mapping for one field
	 * 
	 * @param data
	 *            - data for setting to target object
	 * @param setter
	 *            - sets data to target object
	 * @return instance of MapperBuilder itself
	 */
	public <D> MapperBuilder<S, T> addMapping(D data, Setter<T, D> setter) {
		registerMapping(s -> data, setter);
		return this;
	}

	private <D> void registerMapping(Getter<S, D> getter, Setter<T, D> setter) {
		mapper.registerMapping(getter, setter);
	}
}
