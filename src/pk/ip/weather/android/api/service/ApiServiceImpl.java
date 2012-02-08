package pk.ip.weather.android.api.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import pk.ip.weather.android.domain.*;

public class ApiServiceImpl implements ApiService {

	private String apiUrl;
	
	public ApiServiceImpl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public Set<City> findCities() {
		Set<City> cities = new HashSet<City>();
		cities.addAll(Arrays.asList(new City[]{ createCity(1L, "Kraków"), createCity(2L, "Warszawa"), createCity(3L, "Gdańsk"), createCity(4L, "Katowice")  }));
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
		types.addAll(Arrays.asList(new GraphType[]{ createGraphType("TEMPERATURE", "temperature"), createGraphType("PRESSURE", "pressure"), createGraphType("WIND_SPEED", "wind speed"), createGraphType("HUMIDITY", "humidity"), createGraphType("VISIBILITY", "visibility"), createGraphType("FOG", "fog"), createGraphType("RAIN", "rain"), createGraphType("SNOW", "snow"), createGraphType("THUNDER", "thunder")  }));
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
		groupings.addAll(Arrays.asList(new GraphGrouping[]{ createGraphGrouping("YEAR", "year"), createGraphGrouping("MONTH", "month"), createGraphGrouping("DAY", "day") }));
		return groupings;
	}
	
	private GraphGrouping createGraphGrouping(String id, String name) {
		GraphGrouping grouping = new GraphGrouping();
		grouping.setId(id);
		grouping.setName(name);
		
		return grouping;
	}

	@Override
	public URL getGraphURL(Date dateFrom, Date dateTo, City city,
			GraphType type, GraphGrouping grouping) throws ApiException {

		try {
			String url = String.format("%s/historygraph/%s/%s/%d/%s/%s", apiUrl, formatDate(dateFrom), formatDate(dateTo), city.getId(), type.getId(), grouping.getId());
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new ApiException("Invalid api service url", e);
		}
	}
	
	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
}
