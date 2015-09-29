package cz.rpridal.j8mapper.domain.source;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DepartmentS {
	private String name;
	private List<EmployeeS> employees = new LinkedList<>();
	private Set<EmployeeS> employeeSet = new HashSet<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<EmployeeS> getEmployees() {
		return employees;
	}
	
	public void setEmployees(List<EmployeeS> employees) {
		this.employees = employees;
	}
	
	public void setEmployeeSet(Set<EmployeeS> employeeSet) {
		this.employeeSet = employeeSet;
	}
	
	public Set<EmployeeS> getEmployeeSet() {
		return employeeSet;
	}
}
