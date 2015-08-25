package cz.rpridal.j8mapper.domain.target;

import java.util.LinkedList;
import java.util.List;

public class ManagerT extends EmployeeT {
	private List<EmployeeT> subordinates = new LinkedList<>();

	public List<EmployeeT> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(List<EmployeeT> subordinates) {
		this.subordinates = subordinates;
	}
}
