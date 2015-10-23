package cz.rpridal.j8mapper.manipulator;

public interface Manipulator<SourceType, TargetType> {
	public void map(SourceType source, TargetType target);
}
