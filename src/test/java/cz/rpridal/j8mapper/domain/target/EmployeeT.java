package cz.rpridal.j8mapper.domain.target;

public class EmployeeT extends PersonT {
	private DepartmentT department;
	private ManagerT manager;

	public DepartmentT getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentT department) {
		this.department = department;
	}
	
	public ManagerT getManager() {
		return manager;
	}
	
	public void setManager(ManagerT manager) {
		this.manager = manager;
	}
}
