package org.diagram;

// Generated Jul 16, 2012 4:06:14 PM by Hibernate Tools 4.0.0

/**
 * Dog generated by hbm2java
 */
public class Dog implements java.io.Serializable {

	private long id;
	private String name;

	public Dog() {
	}

	public Dog(long id) {
		this.id = id;
	}

	public Dog(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
