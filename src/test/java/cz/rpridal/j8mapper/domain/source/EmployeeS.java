package cz.rpridal.j8mapper.domain.source;

public class EmployeeS extends PersonS {
	private DepartmentS department;
	private ManagerS manager;
	
	
	public DepartmentS getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentS department) {
		this.department = department;
	}
	
	public void setManager(ManagerS manager) {
		this.manager = manager;
	}
	
	public ManagerS getManager() {
		return manager;
	}
}
