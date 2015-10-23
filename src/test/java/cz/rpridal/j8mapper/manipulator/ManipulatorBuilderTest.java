package cz.rpridal.j8mapper.manipulator;

import static org.junit.Assert.*;

import org.junit.Test;

import cz.rpridal.j8mapper.domain.source.EmployeeS;
import cz.rpridal.j8mapper.domain.target.EmployeeT;

public class ManipulatorBuilderTest {

	@Test
	public void test() {
		assertFalse(ManipulatorBuilder.isMappable(String.class));
		assertFalse(ManipulatorBuilder.isMappable(Integer.class));
		assertFalse(ManipulatorBuilder.isMappable(Boolean.class));
		assertTrue(ManipulatorBuilder.isMappable(EmployeeS.class));
		assertTrue(ManipulatorBuilder.isMappable(EmployeeT.class));
	}

}
