package no.abakus.naut.entity.news;

public class Type {
	private Long id;
	private String description;
	private String name;
	
	public Type() {}
	public Type(Long id, String description, String name) {
		this.id = id;
		this.description = description;
		this.name = name;
	}
	
	/**
	 * @return	the id of the type. 
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return	the name of the type. 
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return	a description of the type.  
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public boolean equals(Object other) {
		return other instanceof Type && this.getId().equals(((Type)other).getId());
	}
	
	public int hashCode() {
		int result = 0;
		if (id != null) {
			result += id.hashCode();
		}
		if (name != null) {
			result += name.hashCode();
		}
		if (description != null) {
			result += description.hashCode();
		}
		return result;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
