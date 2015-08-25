package cz.rpridal.j8mapper.manipulator;

public interface Manipulator <S, T>{
	public void map(S source, T target);
}
