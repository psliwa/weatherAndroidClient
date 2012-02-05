package pk.ip.weather.android.domain;

public class GraphGrouping {
	private String id;
	private String name;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
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
		if(!(o instanceof GraphGrouping)) {
			return false;
		}
		
		GraphGrouping city = (GraphGrouping) o;
		
		return (city.getId() == null && id == null || city.getId().equals(id)) && (city.getName() == null && name == null || city.getName().equals(name));
	}
}
