package pk.ip.weather.android.api.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public class StubApiService implements ApiService {

	private String host;
	
	public StubApiService(String host) {
		this.host = host;
	}
	
	public Set<City> findCities() {
		Set<City> cities = new HashSet<City>();
		cities.addAll(Arrays.asList(new City[]{ createCity(1L, "Kraków"), createCity(2L, "Warszawa")  }));
		return cities;
	}
	
	private City createCity(Long id, String name) {
		City city = new City();
		city.setId(id);
		city.setName(name);
		
		return city;
	}
	
	public Set<GraphType> findGraphTypes() {
		Set<GraphType> types = new HashSet<GraphType>();
		types.addAll(Arrays.asList(new GraphType[]{ createGraphType("TEMPERATURE", "Temperature"), createGraphType("PRESSURE", "Pressure")  }));
		return types;
	}
	
	private GraphType createGraphType(String id, String name) {
		GraphType type = new GraphType();
		type.setId(id);
		type.setName(name);
		
		return type;
	}
	
	public Set<GraphGrouping> findGraphGroupings() {
		Set<GraphGrouping> groupings = new HashSet<GraphGrouping>();
		groupings.addAll(Arrays.asList(new GraphGrouping[]{ createGraphGrouping("YEAR", "Year"), createGraphGrouping("MONTH", "Month"), createGraphGrouping("DAY", "Day") }));
		return groupings;
	}
	
	private GraphGrouping createGraphGrouping(String id, String name) {
		GraphGrouping grouping = new GraphGrouping();
		grouping.setId(id);
		grouping.setName(name);
		
		return grouping;
	}
}
