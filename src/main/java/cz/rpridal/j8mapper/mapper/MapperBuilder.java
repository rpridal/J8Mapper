package cz.rpridal.j8mapper.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import cz.rpridal.j8mapper.ClassDefinition;
import cz.rpridal.j8mapper.getter.Getter;
import cz.rpridal.j8mapper.manipulator.ClassSupplier;
import cz.rpridal.j8mapper.setter.Setter;
import cz.rpridal.j8mapper.transformer.MapperTransformer;
import cz.rpridal.j8mapper.transformer.Transformer;

/**
 * Builder for mapper
 * 
 * @author rpridal
 *
 * @param <S>
 *            source type
 * @param <T>
 *            target type
 */
public class MapperBuilder<S, T> {

	private static final Logger LOGGER = Logger.getLogger(MapperBuilder.class.getName());

	private LambdaMapper<S, T> lambdaMapper;
	protected ClassDefinition<S, T> classDefinition;
	protected MethodMapper<S, T> methodMapper = null;
	protected final Set<String> exclusionFields = new HashSet<>();

	{
		exclusionFields.add("class");
	}

	protected MapperBuilder(Class<S> sourceClass, Class<T> targetClass) {
		this.classDefinition = new ClassDefinition<>(sourceClass, targetClass);
		this.lambdaMapper = new LambdaMapper<>(this.classDefinition);
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
	public static <S, T> CanAutomaticMapperBuilder<S, T> start(Class<S> sourceClass, Class<T> targetClass) {
		return new CanAutomaticMapperBuilder<S, T>(sourceClass, targetClass);
	}

	public static <S, T> Mapper<S, T> autoBuild(Class<S> sourceClass, Class<T> targetClass) {
		return CanAutomaticMapperBuilder.start(sourceClass, targetClass).automatic().build();
	}

	/**
	 * get mapper with mappings
	 * 
	 * @return mapper
	 */
	public Mapper<S, T> build() {
		if (this.methodMapper != null) {
			CompositeMapper<S, T> compositeMapper = new CompositeMapper<>(this.classDefinition);
			compositeMapper.registrateMapper(this.methodMapper);
			compositeMapper.registrateMapper(this.lambdaMapper);
			return compositeMapper;
		}
		return lambdaMapper;
	}

	/**
	 * Add mapping for one field input to setter and output from getter is same
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
	 * Add mapping for one field input to {@link Setter} and output from
	 * {@link Getter} is not same to conversion there is {@link Transformer}
	 * 
	 * @param getter
	 *            - gets data from source object
	 * @param setter
	 *            - sets data to target object
	 * @param transformer
	 *            - transform data from output of getter to input of setter
	 * @return instance of mapperbuilder itself
	 */
	public <SD, TD> MapperBuilder<S, T> addMapping(Getter<S, SD> getter, Setter<T, TD> setter,
			Transformer<SD, TD> transformer) {
		registerMapping(getter, setter, transformer);
		return this;
	}

	/**
	 * Add mapping for one field input to {@link Setter} and output from
	 * {@link Getter} is not same to conversion there is {@link Mapper}
	 * 
	 * @param getter
	 *            - gets data from source object
	 * @param setter
	 *            - sets data to target object
	 * @param mapper
	 *            - transform data from output of getter to input of setter
	 * @return instance of mapperbuilder itself
	 */
	public <SD, TD> MapperBuilder<S, T> addMapping(Getter<S, SD> getter, Setter<T, TD> setter, Mapper<SD, TD> mapper) {
		registerMapping(getter, setter,
				new MapperTransformer<>(new ClassSupplier<TD>(mapper.getClassDefinition().getTargetClass()), mapper));
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
	public <D> MapperBuilder<S, T> addStaticMapping(D data, Setter<T, D> setter) {
		registerMapping(s -> data, setter);
		return this;
	}


	private <SD, TD> void registerMapping(Getter<S, SD> getter, Setter<T, TD> setter, Transformer<SD, TD> transformer) {
		lambdaMapper.registerMapping(getter, setter, transformer);
	}

	private <D> void registerMapping(Getter<S, D> getter, Setter<T, D> setter) {
		lambdaMapper.registerMapping(getter, setter);
	}
}
