package cz.rpridal.j8mapper;

public class ClassDefinition<SourceType, TargetType> {
	/**
	 * 
	 */
	private Class<SourceType> sourceClass;
	private Class<TargetType> targetClass;

	public ClassDefinition(Class<SourceType> sourceClass, Class<TargetType> targetClass) {
		super();
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	public Class<SourceType> getSourceClass() {
		return sourceClass;
	}

	public Class<TargetType> getTargetClass() {
		return targetClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceClass == null) ? 0 : sourceClass.hashCode());
		result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		ClassDefinition other = (ClassDefinition) obj;
		if (sourceClass == null) {
			if (other.sourceClass != null)
				return false;
		} else if (!sourceClass.equals(other.sourceClass))
			return false;
		if (targetClass == null) {
			if (other.targetClass != null)
				return false;
		} else if (!targetClass.equals(other.targetClass))
			return false;
		return true;
	}

}