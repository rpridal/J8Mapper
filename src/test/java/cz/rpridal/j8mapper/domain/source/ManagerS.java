package cz.rpridal.j8mapper.domain.source;

import java.util.LinkedList;
import java.util.List;

public class ManagerS extends EmployeeS {
	private List<EmployeeS> subordinates = new LinkedList<>();

	public List<EmployeeS> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(List<EmployeeS> subordinates) {
		this.subordinates = subordinates;
	}
}
