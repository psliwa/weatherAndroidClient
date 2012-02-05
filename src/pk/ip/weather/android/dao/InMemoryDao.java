package pk.ip.weather.android.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public class InMemoryDao implements Dao {

	private Set<City> cities = new HashSet<City>();
	private Set<GraphType> graphTypes = new HashSet<GraphType>();
	private Set<GraphGrouping> graphGroupings = new HashSet<GraphGrouping>();
	
	@Override
	public Set<City> findCities() {
		return Collections.unmodifiableSet(cities);
	}

	@Override
	public void saveCities(Set<City> cities) {
		this.cities.addAll(cities);
	}

	@Override
	public Set<GraphType> findGraphTypes() {
		return Collections.unmodifiableSet(graphTypes);
	}

	@Override
	public void saveGraphTypes(Set<GraphType> types) {
		this.graphTypes.addAll(types);
		
	}

	@Override
	public Set<GraphGrouping> findGraphGroupings() {
		return Collections.unmodifiableSet(graphGroupings);
	}

	@Override
	public void saveGraphGroupings(Set<GraphGrouping> groupings) {
		this.graphGroupings.addAll(groupings);
	}
}
