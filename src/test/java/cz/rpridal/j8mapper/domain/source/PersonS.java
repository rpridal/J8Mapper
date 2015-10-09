package cz.rpridal.j8mapper.domain.source;

public class PersonS {
	private String name;
	private SexS sex;
	private int age; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SexS getSex() {
		return sex;
	}

	public void setSex(SexS sex) {
		this.sex = sex;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}