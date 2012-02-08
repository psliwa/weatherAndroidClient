package pk.ip.weather.android.dao;

import java.util.Set;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.util.ExtraIterator;

public interface Dao {
	public Set<City> findCities();
	public void saveCities(Set<City> cities);
	
	public Set<GraphType> findGraphTypes();
	public void saveGraphTypes(Set<GraphType> types);
	
	public Set<GraphGrouping> findGraphGroupings();
	public void saveGraphGroupings(Set<GraphGrouping> groupings);
	
	public void saveGraph(Graph graph);
	public ExtraIterator<Graph> findGraphs();
}
