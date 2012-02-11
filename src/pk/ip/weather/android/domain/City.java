package pk.ip.weather.android.domain;

import java.io.Serializable;

public class City implements DomainObject<Long>, Serializable {

	private static final long serialVersionUID = 6002450867307496096L;

	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof City)) {
			return false;
		}
		
		City city = (City) o;
		
		return (city.getId() == null && id == null || city.getId().equals(id)) && (city.getName() == null && name == null || city.getName().equals(name));
	}
}