package pk.ip.weather.android.service;

import java.util.List;
import java.util.Set;

import pk.ip.weather.android.api.service.ApiService;
import pk.ip.weather.android.dao.Dao;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public class WeatherServiceImpl implements WeatherService {
	
	private Dao dao;
	private ApiService apiService;
	
	public WeatherServiceImpl(Dao dao, ApiService apiService) {
		this.dao = dao;
		this.apiService = apiService;
	}
	
	public Set<City> findCities() {
		Set<City> cities = dao.findCities();
		
		if(cities.size() == 0) {
			cities = apiService.findCities();
			dao.saveCities(cities);
		}
		
		return cities;
	}
	
	public Set<GraphType> findGraphTypes() {
		Set<GraphType> types = dao.findGraphTypes();
		
		if(types.size() == 0) {
			types = apiService.findGraphTypes();
			dao.saveGraphTypes(types);
		}
		
		return types;
	}
	
	public Set<GraphGrouping> findGraphGroupings() {
		Set<GraphGrouping> groupings = dao.findGraphGroupings();
		
		if(groupings.size() == 0) {
			groupings = apiService.findGraphGroupings();
			dao.saveGraphGroupings(groupings);
		}
		
		return groupings;
	}
}
