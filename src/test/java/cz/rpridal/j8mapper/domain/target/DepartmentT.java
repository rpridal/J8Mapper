package cz.rpridal.j8mapper.domain.target;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.rpridal.j8mapper.domain.source.EmployeeS;

public class DepartmentT {
	private String name;
	private List<EmployeeT> employees = new LinkedList<>();
	private Set<EmployeeT> employeeSet = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EmployeeT> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeT> employees) {
		this.employees = employees;
	}

	public Set<EmployeeT> getEmployeeSet() {
		return employeeSet;
	}

	public void setEmployeeSet(Set<EmployeeT> employeeSet) {
		this.employeeSet = employeeSet;
	}
}
