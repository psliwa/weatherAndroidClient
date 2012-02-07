package pk.ip.weather.android;

import pk.ip.weather.android.api.service.ApiService;
import pk.ip.weather.android.dao.Dao;
import pk.ip.weather.android.service.WeatherService;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

public class AbstractActivity extends Activity {
	
	protected WeatherApplication getWeatherApplication() {
		return (WeatherApplication) getApplication();
	}
	
	protected ApiService getApiService() {
		return getWeatherApplication().getApiService();
	}
	
	protected WeatherService getWeatherService() {
		return getWeatherApplication().getWeatherService();
	}
	
	protected Dao getDao() {
		return getWeatherApplication().getDao();
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		MenuItem prefsItem = menu.findItem(R.id.menuPrefs);
		
		prefsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				startActivity(new Intent(AbstractActivity.this, PrefsActivity.class));
				return false;
			}
		});
    	
    	return true;
    }
    
    protected void handleException(Throwable e) {
    	Toast.makeText(AbstractActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
