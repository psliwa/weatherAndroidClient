package pk.ip.weather.android.api.service;

import java.util.Set;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public interface ApiService {
	public Set<City> findCities();
	public Set<GraphType> findGraphTypes();
	public Set<GraphGrouping> findGraphGroupings();
}
