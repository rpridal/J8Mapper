package cz.rpridal.j8mapper.domain.source;

import java.util.LinkedList;
import java.util.List;

public class DepartmentS {
	private String name;
	private List<EmployeeS> employees = new LinkedList<>();
	
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
}
