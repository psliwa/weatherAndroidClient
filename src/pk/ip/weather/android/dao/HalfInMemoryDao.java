package pk.ip.weather.android.dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.DomainObject;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.util.CursorExtraIterator;
import pk.ip.weather.android.util.CursorExtraIterator.Hydrator;
import pk.ip.weather.android.util.ExtraIterator;

public class HalfInMemoryDao implements Dao {

	private Dao dao;
	private DbHelper dbHelper;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public HalfInMemoryDao(Context context, Dao dao) {
		dao.clear();
		this.dao = dao;		
		this.dbHelper = new DbHelper(context);
	}
	
	@Override
	public Set<City> findCities() {
		Set<City> cities = dao.findCities();
		
		if(cities.size() == 0) {
			cities = findCitiesInDb();
			dao.saveCities(cities);
		}
		
		return cities;
	}
	
	private Set<City> findCitiesInDb() {
		String sql = "SELECT * FROM "+DbHelper.TABLE_CITY;
		
		Hydrator<City> hydrator = new Hydrator<City>(){
			@Override
			public City hydrate(Cursor cursor) {
				City city = new City();
				city.setId(cursor.getLong(0));
				city.setName(cursor.getString(1));
				
				return city;
			}			
		};
		
		return findDomainObjectsInDb(sql, hydrator);
	}
	
	private <T extends DomainObject> Set<T> findDomainObjectsInDb(String sql, Hydrator<T> hydrator) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(sql, null);
		
		try {
			return hydrateDomainObjects(cursor, hydrator);
		} finally {
			cursor.close();
//			db.close();
		}
	}
	
	private <T extends DomainObject> Set<T> hydrateDomainObjects(Cursor cursor, Hydrator<T> hydrator) {
		Set<T> objects = new HashSet<T>();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			objects.add(hydrator.hydrate(cursor));
			cursor.moveToNext();
		}
		
		return objects;
	}

	@Override
	public void saveCities(Set<City> cities) {
		dao.saveCities(cities);
		saveCitiesInDb(cities);
	}
	
	private void saveCitiesInDb(Set<City> cities) {
		Mapper<City> mapper = new Mapper<City>(){

			@Override
			public void doMapping(City object, ContentValues values) {
				values.put(DbHelper.C_ID, object.getId());
				values.put(DbHelper.C_NAME, object.getName());
			}
		};
		saveDomainObjectInDb(cities, DbHelper.TABLE_CITY, mapper);
	}
	
	private <T extends DomainObject> void saveDomainObjectInDb(Set<T> objects, String tableName, Mapper<T> mapper) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try {
			db.execSQL("DELETE FROM "+tableName);
			ContentValues values = new ContentValues();
			for(T object : objects) {
				values.clear();
				mapper.doMapping(object, values);
				db.insertOrThrow(tableName, null, values);
			}
		} finally {
			db.close();
		}
	}

	@Override
	public Set<GraphType> findGraphTypes() {
		Set<GraphType> types = dao.findGraphTypes();
		
		if(types.size() == 0) {
			types = findGraphTypesInDb();
			dao.saveGraphTypes(types);
		}
		
		return types;
	}
	
	private Set<GraphType> findGraphTypesInDb() {
		String sql = "SELECT * FROM "+DbHelper.TABLE_GRAPH_TYPE;
		
		Hydrator<GraphType> hydrator = new Hydrator<GraphType>(){
			@Override
			public GraphType hydrate(Cursor cursor) {
				GraphType type = new GraphType();
				type.setId(cursor.getString(0));
				type.setName(cursor.getString(1));
				
				return type;
			}			
		};

		return findDomainObjectsInDb(sql, hydrator);
	}

	@Override
	public void saveGraphTypes(Set<GraphType> types) {
		dao.saveGraphTypes(types);
		saveGraphTypesInDb(types);
	}
	
	private void saveGraphTypesInDb(Set<GraphType> objects) {
		Mapper<GraphType> mapper = new Mapper<GraphType>(){

			@Override
			public void doMapping(GraphType object, ContentValues values) {
				values.put(DbHelper.C_ID, object.getId());
				values.put(DbHelper.C_NAME, object.getName());
			}
		};
		saveDomainObjectInDb(objects, DbHelper.TABLE_GRAPH_TYPE, mapper);
	}

	@Override
	public Set<GraphGrouping> findGraphGroupings() {
		Set<GraphGrouping> groupings = dao.findGraphGroupings();
		
		if(groupings.size() == 0) {
			groupings = findGraphGroupingsInDb();
			dao.saveGraphGroupings(groupings);
		}
		
		return groupings;
	}
	
	private Set<GraphGrouping> findGraphGroupingsInDb() {
		String sql = "SELECT * FROM "+DbHelper.TABLE_GRAPH_GROUPING;
		
		Hydrator<GraphGrouping> hydrator = new Hydrator<GraphGrouping>(){
			@Override
			public GraphGrouping hydrate(Cursor cursor) {
				GraphGrouping grouping = new GraphGrouping();
				grouping.setId(cursor.getString(0));
				grouping.setName(cursor.getString(1));
				
				return grouping;
			}			
		};
		
		return findDomainObjectsInDb(sql, hydrator);
	}

	@Override
	public void saveGraphGroupings(Set<GraphGrouping> groupings) {
		dao.saveGraphGroupings(groupings);
		saveGraphGroupingsInDb(groupings);
	}
	
	private void saveGraphGroupingsInDb(Set<GraphGrouping> objects) {
		Mapper<GraphGrouping> mapper = new Mapper<GraphGrouping>(){

			@Override
			public void doMapping(GraphGrouping object, ContentValues values) {
				values.put(DbHelper.C_ID, object.getId());
				values.put(DbHelper.C_NAME, object.getName());
			}
		};
		saveDomainObjectInDb(objects, DbHelper.TABLE_GRAPH_GROUPING, mapper);
	}

	@Override
	public void saveGraph(Graph graph) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(DbHelper.C_CITY_ID, graph.getCity().getId());
			values.put(DbHelper.C_TYPE_ID, graph.getType().getId());
			values.put(DbHelper.C_GROUPING_ID, graph.getGrouping().getId());
			values.put(DbHelper.C_DATE_FROM, formatDate(graph.getDateFrom()));
			values.put(DbHelper.C_DATE_TO, formatDate(graph.getDateTo()));
			values.put(DbHelper.C_FILENAME, graph.getUri().toString());
			
			Long id = db.insertOrThrow(DbHelper.TABLE_GRAPH, null, values);
			graph.setId(id);
		} finally {
			db.close();
		}
	}
	
	private static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	@Override
	public ExtraIterator<Graph> findGraphs() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM "+DbHelper.TABLE_GRAPH+" ORDER BY "+DbHelper.C_ID+" DESC", null);
		Hydrator<Graph> hydrator = new Hydrator<Graph>(){
			@Override
			public Graph hydrate(Cursor cursor) {
				Graph graph = new Graph();
				graph.setId(cursor.getLong(0));
				graph.setCity(findCityById(cursor.getLong(1)));
				graph.setType(findGraphTypeById(cursor.getString(2)));
				graph.setGrouping(findGroupingById(cursor.getString(3)));				
				graph.setDateFrom(parseDate(cursor.getString(4)));
				graph.setDateTo(parseDate(cursor.getString(5)));

				try {
					graph.setUri(new URI(cursor.getString(6)));
				} catch (URISyntaxException e) {
					throw new RuntimeException(e);
				}

				return graph;
			}
		};
		
		return new CursorExtraIterator<Graph>(cursor, hydrator);
	}
	
	private Date parseDate(String s) {
		try {
			return dateFormat.parse(s);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	private City findCityById(Long id) {
		return (City) findDomainObject(findCities(), id);
	}
	
	private GraphGrouping findGroupingById(String id) {
		return (GraphGrouping) findDomainObject(findGraphGroupings(), id);
	}
	
	private GraphType findGraphTypeById(String id) {
		return (GraphType) findDomainObject(findGraphTypes(), id);
	}
	
	private <T> DomainObject<T> findDomainObject(Set<? extends DomainObject<T>> objects, T id) {
		for(DomainObject<T> object : objects) {
			if(object.getId().equals(id)) {
				return object;
			}
		}
		return null;
	}
	
	private static class DbHelper extends SQLiteOpenHelper	{
		private static final String TAG = "DbHelper";
		
		final static String DB_NAME = "weather.db";
		final static Integer DB_VERSION = 1;
		
		final static String TABLE_GRAPH = "graph";
		final static String TABLE_CITY = "city";
		final static String TABLE_GRAPH_TYPE = "graph_type";
		final static String TABLE_GRAPH_GROUPING = "graph_grouping";
		
		final static String C_ID = BaseColumns._ID;
		final static String C_NAME = "name";
		final static String C_CITY_ID = "cityId";
		final static String C_TYPE_ID = "typeId";
		final static String C_GROUPING_ID = "groupingId";
		final static String C_DATE_FROM = "dateFrom";
		final static String C_DATE_TO = "dateTo";
		final static String C_FILENAME = "filename";
		
		private Context context;

		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			
			this.context = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String[] queries = { 
					"CREATE TABLE "+TABLE_GRAPH+"("+
					C_ID+" INT PRIMARY KEY, " +
					C_CITY_ID+" int not null, "+
					C_TYPE_ID+" varchar(64) not null, "+
					C_GROUPING_ID+" varchar(64) not null, "+
					C_DATE_FROM+" date not null, "+
					C_DATE_TO+" date not null, " +
					C_FILENAME+" varchar(255) not null);",
					"CREATE TABLE "+TABLE_CITY+"("+
					C_ID+" INT PRIMARY KEY, "+
					C_NAME+" VARCHAR(64) NOT NULL);",
					"CREATE TABLE "+TABLE_GRAPH_TYPE+"("+
					C_ID+" VARCHAR(32) PRIMARY KEY,"+
					C_NAME+" VARCHAR(32) NOT NULL);",
					"CREATE TABLE "+TABLE_GRAPH_GROUPING+"("+
					C_ID+" VARCHAR(32) PRIMARY KEY,"+
					C_NAME+" VARCHAR(32) NOT NULL);"
			};

			for(String query : queries) {
				db.execSQL(query);
				
				Log.d(TAG, "Wykonano zapytanie: "+query);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_GRAPH);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_CITY);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_GRAPH_TYPE);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_GRAPH_GROUPING);
			onCreate(db);
		}
	}
	
	public void close() {
		dao.close();
		dbHelper.close();
	}
	
	public void clear() {
		dao.clear();
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try {
			dbHelper.onUpgrade(db, 0, 0);
		} finally {
			db.close();
		}
	}
}
