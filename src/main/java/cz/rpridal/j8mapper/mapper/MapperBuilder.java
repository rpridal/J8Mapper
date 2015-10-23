package cz.rpridal.j8mapper.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
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
 * @param <SourceType>
 *            source type
 * @param <TargetType>
 *            target type
 */
public class MapperBuilder<SourceType, TargetType> {

	private static final Logger LOGGER = Logger.getLogger(MapperBuilder.class.getName());

	private LambdaMapper<SourceType, TargetType> lambdaMapper;
	protected ClassDefinition<SourceType, TargetType> classDefinition;
	protected MethodMapper<SourceType, TargetType> methodMapper = null;
	protected final Set<String> exclusionFields = new HashSet<>();

	{
		exclusionFields.add("class");
	}

	protected MapperBuilder(Class<SourceType> sourceClass, Class<TargetType> targetClass) {
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
	public Mapper<SourceType, TargetType> build() {
		if (this.methodMapper != null) {
			CompositeMapper<SourceType, TargetType> compositeMapper = new CompositeMapper<>(this.classDefinition);
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
	public <DataType> MapperBuilder<SourceType, TargetType> addMapping(Getter<SourceType, DataType> getter,
			Setter<TargetType, DataType> setter) {
		registerMapping(getter, setter);
		return this;
	}
	
	public <DataType> MapperBuilder<SourceType, TargetType> addMapping(Supplier<DataType> supplier,
			Setter<TargetType, DataType> setter) {
		Getter<SourceType, DataType> getter = s -> supplier.get();
		registerMapping(getter , setter);
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
	public <SourceDataType, TargetDataType> MapperBuilder<SourceType, TargetType> addMapping(
			Getter<SourceType, SourceDataType> getter, Setter<TargetType, TargetDataType> setter,
			Transformer<SourceDataType, TargetDataType> transformer) {
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
	public <SourceDataType, TargetDataType> MapperBuilder<SourceType, TargetType> addMapping(
			Getter<SourceType, SourceDataType> getter, Setter<TargetType, TargetDataType> setter,
			Mapper<SourceDataType, TargetDataType> mapper) {
		registerMapping(getter, setter, new MapperTransformer<>(
				new ClassSupplier<TargetDataType>(mapper.getClassDefinition().getTargetClass()), mapper));
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
	public <DataType> MapperBuilder<SourceType, TargetType> addStaticMapping(DataType data,
			Setter<TargetType, DataType> setter) {
		registerMapping(s -> data, setter);
		return this;
	}
	
	private <SourceDataType, TargetDataType> void registerMapping(Getter<SourceType, SourceDataType> getter,
			Setter<TargetType, TargetDataType> setter, Transformer<SourceDataType, TargetDataType> transformer) {
		lambdaMapper.registerMapping(getter, setter, transformer);
	}

	private <DataType> void registerMapping(Getter<SourceType, DataType> getter, Setter<TargetType, DataType> setter) {
		lambdaMapper.registerMapping(getter, setter);
	}

}
