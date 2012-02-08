package pk.ip.weather.android.dao;

import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.util.ExtraIterator;

public class HalfInMemoryDao implements Dao {

	private Dao dao;
	
	public HalfInMemoryDao(Dao dao) {
		
	}
	
	@Override
	public Set<City> findCities() {
		return dao.findCities();
	}

	@Override
	public void saveCities(Set<City> cities) {
		dao.saveCities(cities);
	}

	@Override
	public Set<GraphType> findGraphTypes() {
		return dao.findGraphTypes();
	}

	@Override
	public void saveGraphTypes(Set<GraphType> types) {
		dao.saveGraphTypes(types);
		
	}

	@Override
	public Set<GraphGrouping> findGraphGroupings() {
		return dao.findGraphGroupings();
	}

	@Override
	public void saveGraphGroupings(Set<GraphGrouping> groupings) {
		dao.saveGraphGroupings(groupings);
	}

	@Override
	public void saveGraph(Graph graph) {
		
	}

	@Override
	public ExtraIterator<Graph> findGraphs() {
		// TODO Auto-generated method stub
		return null;
	}

	private static class DbHelper extends SQLiteOpenHelper	{
		final static String DB_NAME = "weather";
		final static Integer DB_VERSION = 1;
		
		final static String TABLE_GRAPH = "graph";
		final static String C_ID = "id";
		final static String C_CITY_ID = "cityId";

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase arg0) {
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			
		}
	}
}
