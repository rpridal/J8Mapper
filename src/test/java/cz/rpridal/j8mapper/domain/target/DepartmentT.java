package cz.rpridal.j8mapper.domain.target;

import java.util.LinkedList;
import java.util.List;

public class DepartmentT {
	private String name;
	private List<EmployeeT> employees = new LinkedList<>();
	
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
}
