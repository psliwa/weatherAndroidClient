package pk.ip.weather.android.service;

import java.util.List;
import java.util.Set;

import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public interface WeatherService {
	public Set<City> findCities();
	public Set<GraphType> findGraphTypes();
	public Set<GraphGrouping> findGraphGroupings();
}
