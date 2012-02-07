package pk.ip.weather.android.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;

public class InMemoryDao implements Dao {

	private Set<City> cities = new HashSet<City>();
	private Set<GraphType> graphTypes = new HashSet<GraphType>();
	private Set<GraphGrouping> graphGroupings = new HashSet<GraphGrouping>();
	private List<Graph> graphs = new LinkedList<Graph>();
	
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
	
	@Override
	public void saveGraph(Graph graph) {
		graphs.add(graph);
	}
	
	@Override
	public Iterator<Graph> findGraphs() {
		return graphs.iterator();
	}
}
