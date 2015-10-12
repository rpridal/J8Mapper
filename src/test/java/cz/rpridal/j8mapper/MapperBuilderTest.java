package cz.rpridal.j8mapper;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.junit.Test;

import cz.rpridal.j8mapper.domain.source.DepartmentS;
import cz.rpridal.j8mapper.domain.source.EmployeeS;
import cz.rpridal.j8mapper.domain.source.ManagerS;
import cz.rpridal.j8mapper.domain.source.PersonS;
import cz.rpridal.j8mapper.domain.source.SexS;
import cz.rpridal.j8mapper.domain.target.DepartmentT;
import cz.rpridal.j8mapper.domain.target.EmployeeT;
import cz.rpridal.j8mapper.domain.target.ManagerT;
import cz.rpridal.j8mapper.domain.target.PersonT;
import cz.rpridal.j8mapper.domain.target.SexT;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperBuilder;
import cz.rpridal.j8mapper.transformer.Transformer;

@SuppressWarnings("unused")
public class MapperBuilderTest {

	public class Source {
		String s;
		int i;
		Integer bi;
		List<String> ls;
		List<String> ss;

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}

		public void setI(int i) {
			this.i = i;
		}

		public int getI() {
			return i;
		}

		public List<String> getSs() {
			return ss;
		}

		public void setSs(List<String> ss) {
			this.ss = ss;
		}

		public List<String> getLs() {
			return ls;
		}

		public void setLs(List<String> ls) {
			this.ls = ls;
		}

		public Integer getBi() {
			return bi;
		}

		public void setBi(Integer bi) {
			this.bi = bi;
		}
	}

	public class SameTarget {
		String s;
		int i;
		List<String> ls;
		Set<String> ss;

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}

		public void setI(int i) {
			this.i = i;
		}

		public int getI() {
			return i;
		}

		public List<String> getLs() {
			return ls;
		}

		public void setLs(List<String> ls) {
			this.ls = ls;
		}

		public void setSs(Set<String> ss) {
			this.ss = ss;
		}

		public Set<String> getSs() {
			return ss;
		}
	}

	public class Target {
		String t;
		Integer i;
		List<String> ls;
		String bi;

		public List<String> getLs() {
			return ls;
		}

		public void setLs(List<String> ls) {
			this.ls = ls;
		}

		public String getT() {
			return t;
		}

		public void setT(String t) {
			this.t = t;
		}

		public void setI(int i) {
			this.i = i;
		}

		public int getI() {
			return i;
		}
		
		public void setBi(String bi) {
			this.bi = bi;
		}
		
		public String getBi() {
			return bi;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + i;
			result = prime * result + ((t == null) ? 0 : t.hashCode());
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
			Target other = (Target) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (i != other.i)
				return false;
			if (t == null) {
				if (other.t != null)
					return false;
			} else if (!t.equals(other.t))
				return false;
			return true;
		}

		private MapperBuilderTest getOuterType() {
			return MapperBuilderTest.this;
		}

	}

	@Test
	public void automaticMappingSimpleObjectBuilder() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		SameTarget target = MapperBuilder.start(Source.class, SameTarget.class).automatic().build().map(s,
				SameTarget::new);

		assertEquals("text", target.getS());
		assertEquals(55, target.getI());
	}

	@Test
	public void automaticMappingSimpleObjectBuilderNull() {
		Source s = null;
		SameTarget target = MapperBuilder.start(Source.class, SameTarget.class).automatic().build().map(s,
				SameTarget::new);

		assertNull(target);
	}

	@Test
	public void automaticMappingSimpleObjectBuilderList() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		s.setLs(Arrays.asList("S1", "S2"));
		SameTarget target = MapperBuilder.start(Source.class, SameTarget.class).automatic().build().map(s,
				SameTarget::new);

		assertEquals("text", target.getS());
		assertEquals(55, target.getI());
		assertTrue(contains(target.getLs(), a -> a.equals("S1")));
		assertTrue(contains(target.getLs(), a -> a.equals("S2")));
	}

	@Test
	public void automaticMappingSimpleObjectBuilderSet() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		s.setSs(Arrays.asList("S1", "S2"));
		SameTarget target = MapperBuilder.start(Source.class, SameTarget.class).automatic().build().map(s,
				SameTarget::new);

		assertEquals("text", target.getS());
		assertEquals(55, target.getI());
		assertTrue(contains(target.getSs(), a -> a.equals("S1")));
		assertTrue(contains(target.getSs(), a -> a.equals("S2")));
	}

	@Test
	public void automaticMappingSimpleObjectBuilderSet2() {
		SameTarget s = new SameTarget();
		s.setI(55);
		s.setS("text");
		s.setSs(new HashSet<String>(Arrays.asList("S1", "S2")));
		Source target = MapperBuilder.start(SameTarget.class, Source.class).automatic().build().map(s, Source::new);

		assertEquals("text", target.getS());
		assertEquals(55, target.getI());
		assertTrue(contains(target.getSs(), a -> a.equals("S1")));
		assertTrue(contains(target.getSs(), a -> a.equals("S2")));
	}

	@Test
	public void mappingSimpleObjectBuilder() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		Target target = MapperBuilder.start(Source.class, Target.class).addMapping(Source::getS, Target::setT)
				.addMapping(Source::getI, Target::setI).build().map(s, Target::new);

		assertEquals("text", target.getT());
		assertEquals(55, target.getI());
	}

	@Test
	public void mappingSimpleObjectWithTransformerBuilder() {
		Source s = new Source();
		s.setBi(55);
		Target target = MapperBuilder.start(Source.class, Target.class)
				.addMapping(Source::getBi, Target::setBi, new Transformer<Integer, String>() {
					@Override
					public String transform(Integer data) {
						return data.toString();
					}
				}).build().map(s, Target::new);

		assertEquals("55", target.getBi());
	}

	@Test
	public void mappingSimpleObjectMapper() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		Mapper<Source, Target> mapper = MapperBuilder.start(Source.class, Target.class)
				.addMapping(Source::getS, Target::setT).addMapping(Source::getI, Target::setI).build();
		Target target = mapper.map(s, Target::new);

		assertEquals("text", target.getT());
		assertEquals(55, target.getI());
	}

	@Test
	public void mappingSimpleObjectBuilderStaticData() {
		Source s = new Source();
		s.setI(55);
		s.setS("text");
		Target target = MapperBuilder.start(Source.class, Target.class).addMapping(Source::getS, Target::setT)
				.addStaticMapping(10, Target::setI).build().map(s, Target::new);

		assertEquals("text", target.getT());
		assertEquals(10, target.getI());
	}

	@Test
	public void mappingSimpleObjectBuilderCalculationData() {
		Source source = new Source();
		source.setI(55);
		source.setS("text");
		Target target = MapperBuilder.start(Source.class, Target.class).addMapping(Source::getS, Target::setT)
				.addMapping(s -> s.getI() + 10, Target::setI).build().map(source, Target::new);

		assertEquals("text", target.getT());
		assertEquals(65, target.getI());
	}

	Source getNewSource(int i, String s) {
		Source result = new Source();
		result.setI(i);
		result.setS(s);
		return result;
	}

	Target getNewTarget(int i, String t) {
		Target result = new Target();
		result.setI(i);
		result.setT(t);
		return result;
	}

	@Test
	public void mappingListObjects() {
		List<Source> source = new LinkedList<Source>();
		source.add(getNewSource(1, "p1"));
		source.add(getNewSource(2, "p2"));

		List<Target> mapList = MapperBuilder.start(Source.class, Target.class).addMapping(Source::getS, Target::setT)
				.addMapping(Source::getI, Target::setI).build().map(source, Target::new).collect(Collectors.toList());

		assertEquals("p1", mapList.get(0).getT());
		assertEquals(1, mapList.get(0).getI());
		assertEquals("p2", mapList.get(1).getT());
		assertEquals(2, mapList.get(1).getI());
	}
	
	@Test
	public void mappingListObjectsNull() {
		List<Source> source = null;

		List<Target> mapList = MapperBuilder.start(Source.class, Target.class).addMapping(Source::getS, Target::setT)
				.addMapping(Source::getI, Target::setI)
				.build()
				.map(source, Target::new)
				.collect(Collectors.toList());

		assertTrue(mapList.isEmpty());
	}

	@Test
	public void automaticMappingManualySubMappingOneToOne() {
		EmployeeS employeeS = new EmployeeS();
		employeeS.setName("name");
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS.setDepartment(departmentS);

		Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
				.addMapping(DepartmentS::getName, DepartmentT::setName).build();

		EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class).addMapper(departmentMapper).automatic()
				.build().map(employeeS, EmployeeT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getDepartment());
		assertEquals("department", result.getDepartment().getName());
	}
	
	@Test
	public void automaticMappingManualySubMappingOneToOneAddNonautomaticMapperMapping() {
		EmployeeS employeeS = new EmployeeS();
		employeeS.setName("name");
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS.setDepartment(departmentS);

		Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
				.addMapping(DepartmentS::getName, DepartmentT::setName).build();

		EmployeeT result = MapperBuilder
				.start(EmployeeS.class, EmployeeT.class)
				.addMapping(EmployeeS::getName, EmployeeT::setName)
				.addMapping(EmployeeS::getDepartment, EmployeeT::setDepartment, departmentMapper)				
				.build().map(employeeS, EmployeeT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getDepartment());
		assertEquals("department", result.getDepartment().getName());
	}

	@Test
	public void automaticMappingAutoSubMappingOneToOne() {
		EmployeeS employeeS = new EmployeeS();
		employeeS.setName("name");
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS.setDepartment(departmentS);

		Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
				.automatic().build();

		EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class).addMapper(departmentMapper).automatic()
				.build().map(employeeS, EmployeeT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getDepartment());
		assertEquals("department", result.getDepartment().getName());
	}

	@Test
	public void automaticMappingSubMappingWithoutDeclarationOneToOne() {
		EmployeeS employeeS = new EmployeeS();
		employeeS.setName("name");
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS.setDepartment(departmentS);

		EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class).automatic().build().map(employeeS,
				EmployeeT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getDepartment());
		assertEquals("department", result.getDepartment().getName());
	}

	@Test
	public void automaticMappingSubMappingWithoutDeclarationOneToOneWithInheriets() {
		EmployeeS employeeS = new EmployeeS();
		employeeS.setName("name");
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS.setDepartment(departmentS);

		EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class).automatic()
				.addStaticMapping("name2", EmployeeT::setName).build().map(employeeS, EmployeeT::new);

		assertEquals("name2", result.getName());
		assertNotNull(result.getDepartment());
		assertEquals("department", result.getDepartment().getName());
	}

	@Test
	public void automaticMappingManualySubMappingOneToMany() {
		ManagerS managerS = new ManagerS();
		managerS.setName("name");
		managerS.getSubordinates().add(getEmploeeS("employee1"));
		managerS.getSubordinates().add(getEmploeeS("employee2"));

		Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
				.addMapping(DepartmentS::getName, DepartmentT::setName).build();

		ManagerT result = MapperBuilder.start(ManagerS.class, ManagerT.class).addMapper(departmentMapper).automatic()
				.build().map(managerS, ManagerT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getSubordinates());
		assertEquals(2, result.getSubordinates().size());
		assertEquals("employee1", result.getSubordinates().get(0).getName());
	}

	@Test
	public void automaticMappingEnum() {
		PersonS person = new PersonS();
		person.setSex(SexS.FEMALE);

		Mapper<PersonS, PersonT> mapper = MapperBuilder.start(PersonS.class, PersonT.class).automatic().build();

		PersonT target = mapper.map(person);
		assertEquals(SexT.FEMALE, target.getSex());

	}

	@Test
	public void automaticMappingManualySubMappingManyToOne() {
		ManagerS managerS = new ManagerS();
		managerS.setName("name");
		EmployeeS emploeeS1 = getEmploeeS("employee1");
		emploeeS1.setManager(managerS);
		managerS.getSubordinates().add(emploeeS1);
		EmployeeS emploeeS2 = getEmploeeS("employee2");
		emploeeS2.setManager(managerS);
		managerS.getSubordinates().add(emploeeS2);

		Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
				.addMapping(DepartmentS::getName, DepartmentT::setName).build();

		ManagerT result = MapperBuilder.start(ManagerS.class, ManagerT.class).addMapper(departmentMapper).automatic()
				.build().map(managerS, ManagerT::new);

		assertEquals("name", result.getName());
		assertNotNull(result.getSubordinates());
		assertEquals(2, result.getSubordinates().size());
		assertEquals("employee1", result.getSubordinates().get(0).getName());
		assertEquals(result, result.getSubordinates().get(0).getManager());
		assertSame(result, result.getSubordinates().get(0).getManager());
	}

	@Test
	public void megaTest() {
		DepartmentS departmentS = getDepartment("name");
		ManagerS manager1 = getManagerS("manager1");
		manager1.setDepartment(departmentS);
		ManagerS manager2 = getManagerS("manager2");
		manager2.setDepartment(departmentS);
		EmployeeS emploeeS1 = getEmploeeS("employee1");
		emploeeS1.setDepartment(departmentS);
		EmployeeS emploeeS2 = getEmploeeS("employee2");
		emploeeS1.setDepartment(departmentS);
		EmployeeS emploeeS3 = getEmploeeS("employee3");
		emploeeS1.setDepartment(departmentS);
		manager1.setManager(manager2);
		manager2.setSubordinates(Arrays.asList(manager1, emploeeS1, emploeeS2, emploeeS3));
		manager2.setSubordinates(Arrays.asList(emploeeS1, emploeeS2, emploeeS3));
		departmentS.setEmployees(Arrays.asList(manager1, manager2, emploeeS1, emploeeS2, emploeeS3));
		DepartmentT departmentT = MapperBuilder.start(DepartmentS.class, DepartmentT.class).automatic().build()
				.map(departmentS);

		assertEquals(5, departmentT.getEmployees().size());
		contains(departmentT.getEmployees(), getNamePredicate("manager1"));
		contains(departmentT.getEmployees(), getNamePredicate("manager2"));
		contains(departmentT.getEmployees(), getNamePredicate("employee1"));
		contains(departmentT.getEmployees(), getNamePredicate("employee2"));
		contains(departmentT.getEmployees(), getNamePredicate("employee3"));
	}

	@Test
	public void autoboxingTest() {
		PersonS personS = new PersonS();
		personS.setAge(50);

		PersonT result = MapperBuilder.start(PersonS.class, PersonT.class).automatic().build().map(personS);

		assertEquals(50, result.getAge().intValue());
	}

	private Predicate<EmployeeT> getNamePredicate(String string) {
		return e -> e.getName().equals(string);
	}

	private <T> boolean contains(Stream<T> stream, Predicate<? super T> predicate) {
		return stream.filter(predicate).findFirst().isPresent();
	}

	private <T> boolean contains(Collection<T> collection, Predicate<? super T> predicate) {
		return contains(collection.stream(), predicate);
	}

	private DepartmentS getDepartment(String name) {
		DepartmentS department = new DepartmentS();
		department.setName(name);
		return department;
	}

	private EmployeeS getEmploeeS(String name) {
		EmployeeS employeeS1 = new EmployeeS();
		employeeS1.setName(name);
		DepartmentS departmentS = new DepartmentS();
		departmentS.setName("department");
		employeeS1.setDepartment(departmentS);
		return employeeS1;
	}

	private ManagerS getManagerS(String name) {
		ManagerS managerS = new ManagerS();
		managerS.setName(name);
		return managerS;
	}

}
