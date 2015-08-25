package cz.rpridal.j8mapper;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import cz.rpridal.j8mapper.domain.source.DepartmentS;
import cz.rpridal.j8mapper.domain.source.EmployeeS;
import cz.rpridal.j8mapper.domain.source.ManagerS;
import cz.rpridal.j8mapper.domain.target.DepartmentT;
import cz.rpridal.j8mapper.domain.target.EmployeeT;
import cz.rpridal.j8mapper.domain.target.ManagerT;
import cz.rpridal.j8mapper.mapper.Mapper;
import cz.rpridal.j8mapper.mapper.MapperBuilder;

public class MapperBuilderTest {

    class Source {
        String s;
        int i;

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
    }
    
    class SameTarget {
        String s;
        int i;

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
    }

    class Target {
        String t;
        int i;

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
            }
            else if (!t.equals(other.t))
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
        SameTarget target = MapperBuilder.start(Source.class, SameTarget.class)
                .automatic()
                .build()
                .map(s, SameTarget::new);

        assertEquals("text", target.getS());
        assertEquals(55, target.getI());
    }
    @Test
    public void mappingSimpleObjectBuilder() {
        Source s = new Source();
        s.setI(55);
        s.setS("text");
        Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(s, Target::new);

        assertEquals("text", target.getT());
        assertEquals(55, target.getI());
    }
    
    
    @Test
    public void mappingSimpleObjectMapper() {
        Source s = new Source();
        s.setI(55);
        s.setS("text");
        Mapper<Source, Target> mapper = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build();
        Target target = mapper.map(s, Target::new);

        assertEquals("text", target.getT());
        assertEquals(55, target.getI());
    }
    
    @Test
    public void mappingSimpleObjectBuilderStaticData() {
        Source s = new Source();
        s.setI(55);
        s.setS("text");
        Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(10, Target::setI)
                .build()
                .map(s, Target::new);

        assertEquals("text", target.getT());
        assertEquals(10, target.getI());
    }
    
    @Test
    public void mappingSimpleObjectBuilderCalculationData() {
        Source source = new Source();
        source.setI(55);
        source.setS("text");
        Target target = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(s -> s.getI() + 10, Target::setI)
                .build()
                .map(source, Target::new);

        assertEquals("text", target.getT());
        assertEquals(65, target.getI());
    }
    
    Source getNewSource(int i, String s){
        Source result = new Source();
        result.setI(i);
        result.setS(s);
        return result;
    }
    
    Target getNewTarget(int i, String t){
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
        
        List<Target> mapList = MapperBuilder.start(Source.class, Target.class)
                .addMapping(Source::getS, Target::setT)
                .addMapping(Source::getI, Target::setI)
                .build()
                .map(source, Target::new)
                .collect(Collectors.toList());

        assertEquals("p1", mapList.get(0).getT());
        assertEquals(1, mapList.get(0).getI());
        assertEquals("p2", mapList.get(1).getT());
        assertEquals(2, mapList.get(1).getI());
    }   
    
    @Test
    public void automaticMappingManualySubMappingOneToOne() {
    	EmployeeS employeeS = new EmployeeS();
    	employeeS.setName("name");
    	DepartmentS departmentS = new DepartmentS();
    	departmentS.setName("department");
    	employeeS.setDepartment(departmentS);
    	
    	Mapper<DepartmentS, DepartmentT> departmentMapper = MapperBuilder.start(DepartmentS.class, DepartmentT.class)
    		.addMapping(DepartmentS::getName, DepartmentT::setName)
    		.build();
    	
    	EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class)
    		.addMapper(departmentMapper)
    		.automatic()
    		.build()
    		.map(employeeS, EmployeeT::new);
    	
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
    		.automatic()
    		.build();
    	
    	EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class)
    		.addMapper(departmentMapper)
    		.automatic()
    		.build()
    		.map(employeeS, EmployeeT::new);
    	
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
    	
    	EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class)
    		.automatic()
    		.build()
    		.map(employeeS, EmployeeT::new);
    	
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
    	
    	EmployeeT result = MapperBuilder.start(EmployeeS.class, EmployeeT.class)
    		.automatic()
    		.addMapping("name2", EmployeeT::setName)
    		.build()
    		.map(employeeS, EmployeeT::new);
    	
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
    		.addMapping(DepartmentS::getName, DepartmentT::setName)
    		.build();
    	
    	ManagerT result = MapperBuilder.start(ManagerS.class, ManagerT.class)
    		.addMapper(departmentMapper)
    		.automatic()
    		.build()
    		.map(managerS, ManagerT::new);
    	
    	assertEquals("name", result.getName());
    	assertNotNull(result.getSubordinates());
    	assertEquals(2, result.getSubordinates().size());
    	assertEquals("employee1", result.getSubordinates().get(0).getName());
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
    		.addMapping(DepartmentS::getName, DepartmentT::setName)
    		.build();
    	
    	ManagerT result = MapperBuilder.start(ManagerS.class, ManagerT.class)
    		.addMapper(departmentMapper)
    		.automatic()
    		.build()
    		.map(managerS, ManagerT::new);
    	
    	assertEquals("name", result.getName());
    	assertNotNull(result.getSubordinates());
    	assertEquals(2, result.getSubordinates().size());
    	assertEquals("employee1", result.getSubordinates().get(0).getName());
    	assertEquals(result, result.getSubordinates().get(0).getManager());
    	assertSame(result, result.getSubordinates().get(0).getManager());
    }
    
	private EmployeeS getEmploeeS(String name) {
		EmployeeS employeeS1 = new EmployeeS();
    	employeeS1.setName(name);
    	DepartmentS departmentS = new DepartmentS();
    	departmentS.setName("department");
    	employeeS1.setDepartment(departmentS);
		return employeeS1;
	}

}
