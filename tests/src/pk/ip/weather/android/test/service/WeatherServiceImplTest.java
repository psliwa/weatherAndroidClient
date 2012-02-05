package pk.ip.weather.android.test.service;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pk.ip.weather.android.api.service.ApiService;
import pk.ip.weather.android.dao.Dao;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.service.WeatherServiceImpl;

@RunWith(JMock.class)
public class WeatherServiceImplTest {
	
	private WeatherServiceImpl service;
	private Dao dao;
	private ApiService apiService;
	private Mockery context;
	
	@Before()
	public void setUp() {
		context = new JUnit4Mockery();
		dao = context.mock(Dao.class);
		apiService = context.mock(ApiService.class);
		
		service = new WeatherServiceImpl(dao, apiService);
	}
	
	@Test
	public void findCitiesFromDao() {
		
		final Set<City> expectedCities = getCitiesStub();
		context.checking(new Expectations(){
			{
				one(dao).findCities();
				will(returnValue(expectedCities));
				
				never(apiService).findCities();
			}
		});
		
		Set<City> actualCities = service.findCities();
		
		assertEquals(expectedCities, actualCities);
	}
	
	private Set<City> getCitiesStub() {
		Set<City> cities = new HashSet<City>();
		cities.addAll(Arrays.asList(new City[]{ createCity(1L, "Kraków"), createCity(2L, "Warszawa")}));
		
		return cities;
	}
	
	private City createCity(Long id, String name) {
		City city = new City();
		city.setId(id);
		city.setName(name);
		
		return city;
	}
	
	@Test
	public void findCitiesFromApiServiceWhenDaoReturnsEmptyList() {
		final Set expected = getCitiesStub();
		
		Expectations expectations = new Expectations(){
			{
				one(dao).findCities();
				will(returnValue(Collections.EMPTY_LIST));
				
				one(apiService).findCities();
				will(returnValue(expected));
				
				one(dao).saveCities(expected);
			}
		};
		
		findCollectionFromApiServiceWhenDaoReturnsEmptyList(expected, expectations, new Invocation(service, "findCities"));
	}
	
	@Test()
	public void findGraphTypesFromDao() {
		final Set<GraphType> expectedTypes = getGraphTypesStub();
		
		context.checking(new Expectations(){
			{
				one(dao).findGraphTypes();
				will(returnValue(expectedTypes));
			}
		});
		
		Set<GraphType> actualTypes = service.findGraphTypes();
		assertEquals(expectedTypes, actualTypes);
	}
	
	private Set<GraphType> getGraphTypesStub() {
		Set<GraphType> types = new HashSet<GraphType>();
		types.addAll(Arrays.asList(new GraphType[]{ createGraphType("TYPE1", "Type 1"), createGraphType("TYPE2", "Type 2")}));
		
		return types;
	}
	
	private GraphType createGraphType(String id, String name) {
		GraphType type = new GraphType();
		type.setId(id);
		type.setName(name);
		
		return type;
	}
	
	@Test()
	public void findGraphTypesFromApiServiceWhenDaoReturnsEmptyList() {
		final Set expected = getGraphTypesStub();
		
		Expectations expectations = new Expectations(){
			{
				one(dao).findGraphTypes();
				will(returnValue(Collections.EMPTY_LIST));
				
				one(apiService).findGraphTypes();
				will(returnValue(expected));
				
				one(dao).saveGraphTypes(expected);
			}
		};
		
		findCollectionFromApiServiceWhenDaoReturnsEmptyList(expected, expectations, new Invocation(service, "findGraphTypes"));
	}
	
	@Test()
	public void findGraphGroupsFromDao() {
		final Set expected = getGraphGroupingsStub();
		
		context.checking(new Expectations(){
			{
				one(dao).findGraphGroupings();
				will(returnValue(expected));
			}
		});
		
		Set actual = service.findGraphGroupings();
		assertEquals(expected, actual);
	}
	
	private Set<GraphGrouping> getGraphGroupingsStub() {
		Set<GraphGrouping> types = new HashSet<GraphGrouping>();
		types.addAll(Arrays.asList(new GraphGrouping[]{ createGraphGrouping("TYPE1", "Type 1"), createGraphGrouping("TYPE2", "Type 2")}));
		
		return types;
	}
	
	private GraphGrouping createGraphGrouping(String id, String name) {
		GraphGrouping type = new GraphGrouping();
		type.setId(id);
		type.setName(name);
		
		return type;
	}
	
	@Test()
	public void findGraphGroupingsFromApiServiceWhenDaoReturnsEmptyList() {
		final Set expected = getGraphTypesStub();
		Expectations exceptations = new Expectations(){
			{
				one(dao).findGraphGroupings();
				will(returnValue(Collections.EMPTY_LIST));
				
				one(apiService).findGraphGroupings();
				will(returnValue(expected));
				
				one(dao).saveGraphGroupings(expected);
			}
		};
		
		findCollectionFromApiServiceWhenDaoReturnsEmptyList(expected, exceptations, new Invocation(service, "findGraphGroupings"));
	}
	
	public void findCollectionFromApiServiceWhenDaoReturnsEmptyList(Set expected, Expectations exceptations, Invocation invocation) {
		final Set emptyList = Collections.EMPTY_SET;
		
		context.checking(exceptations);
		
		Set actual = (Set) invocation.invoke();
		
		assertEquals(expected, actual);
	}
	
	private class Invocation {
		private Object object;
		private Method method;
		
		public Invocation(Object object, String methodName) {
			this.object = object;
			try {
				this.method = object.getClass().getMethod(methodName);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		
		public Object invoke() {
			try {
				return method.invoke(object);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
