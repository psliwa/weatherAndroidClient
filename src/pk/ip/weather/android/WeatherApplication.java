package pk.ip.weather.android;

import pk.ip.weather.android.api.service.ApiService;
import pk.ip.weather.android.api.service.ApiServiceImpl;
import pk.ip.weather.android.dao.Dao;
import pk.ip.weather.android.dao.DbDao;
import pk.ip.weather.android.dao.InMemoryDao;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.service.WeatherService;
import pk.ip.weather.android.service.WeatherServiceImpl;
import pk.ip.weather.android.util.ExtraIterator;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class WeatherApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private static final String TAG = "WeaterApplication";
	private static final String PREFS_KEY_API_URL = "prefApiUrl";
	
	private SharedPreferences preferences;
	private WeatherService weatherService;
	private ApiService apiService;
	private Dao dao;
	
	private static Context context;
	
	public void onCreate() {
		loadPrefs();
		context = this;
	}
	
	public static Context getContext() {
		return context;
	}
	
    private void loadPrefs() {
    	preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	preferences.registerOnSharedPreferenceChangeListener(this);
    }
    
	@Override
	public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		Log.d(TAG, "onSharedPreferenceChanged: "+key);
		if(key.equals(PREFS_KEY_API_URL)) {
			apiService = null;
			weatherService = null;
		}
	}
	
    public WeatherService getWeatherService() {
    	if(weatherService == null) {
    		weatherService = new WeatherServiceImpl(getDao(), getApiService());
    	}
    	
    	return weatherService;
    }
    
    public Dao getDao() {
    	if(dao == null) {
    		dao = new DbDao(this, new InMemoryDao());
    	}
    	
    	return dao;
    }
    
    @Override
	public void onTerminate() {
		super.onTerminate();
		
		dao.close();
		dao = null;
	}

	public ApiService getApiService() {
    	if(apiService == null) {
    		String apiUrl = preferences.getString(PREFS_KEY_API_URL, "");
    		apiService = new ApiServiceImpl(apiUrl);
    		Log.d(TAG, "Utworzono obiekt apiService, apiUrl: "+apiUrl);
    	}
    	
    	return apiService;
    }
	
	public void clearDao() {
		ExtraIterator<Graph> graphs = getDao().findGraphs();
		while(graphs.hasNext()) {
			Graph graph = graphs.next();
			boolean result = deleteFile(graph.getFilename());
			
			Log.d(TAG, "próba usunięcia pliku "+graph.getFilename()+": "+result);
		}
		
		getDao().clear();
	}
}
