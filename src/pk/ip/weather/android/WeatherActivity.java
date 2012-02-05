package pk.ip.weather.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pk.ip.weather.android.api.service.StubApiService;
import pk.ip.weather.android.dao.InMemoryDao;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.service.WeatherService;
import pk.ip.weather.android.service.WeatherServiceImpl;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class WeatherActivity extends Activity {
	
	private static final String TAG = "WeaterActivity";
	
	private Spinner citySpinner;
	private Spinner typeSpinner;
	private Spinner groupSpinner;
	private EditText dateFromInput;
	private EditText dateToInput;
	private Button showButton;
	
	private WeatherService weatherService;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        
        viewProcess();
    }
    
    private void viewProcess() {
    	assignWidgets();
    	attachDatePicker(dateFromInput);
    	attachDatePicker(dateToInput);    	
    	
    	populateSpinner(citySpinner, getCities());
    	populateSpinner(typeSpinner, getGraphTypes());
    	populateSpinner(groupSpinner, getGraphGroupings());
    }
    
	private void assignWidgets() {
		citySpinner = (Spinner) findViewById(R.id.cityInput);
    	typeSpinner = (Spinner) findViewById(R.id.typeInput);
    	groupSpinner = (Spinner) findViewById(R.id.groupInput);
    	dateFromInput = (EditText) findViewById(R.id.dateFromInput);
    	dateToInput = (EditText) findViewById(R.id.dateToInput);
    	showButton = (Button) findViewById(R.id.showButton);
	}
	
	private void attachDatePicker(final EditText input) {
		Calendar calendar = new GregorianCalendar();
		Log.d(TAG, Integer.toString(calendar.get(Calendar.YEAR)));
		Log.d(TAG, Integer.toString(calendar.get(Calendar.MONTH)));
		Log.d(TAG, Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		
		final DatePickerDialog datePicker = new DatePickerDialog(this, new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = new GregorianCalendar();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				input.setText(format.format(calendar.getTime()));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		
		input.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				datePicker.show();	
				return false;
			}
		});
	}
	
    private <T> void populateSpinner(Spinner spinner, List<T> objects) {
    	ArrayAdapter<T> adapter = new ArrayAdapter<T>(this, android.R.layout.simple_spinner_dropdown_item, objects);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(adapter);
    }
    
    private List<City> getCities() {
    	Set<City> cities = getWeatherService().findCities();
    	return asList(cities);
    }
    
    private List<GraphType> getGraphTypes() {
    	Set<GraphType> types = getWeatherService().findGraphTypes();
    	return asList(types);
    }
    
    private List<GraphGrouping> getGraphGroupings() {
    	Set<GraphGrouping> types = getWeatherService().findGraphGroupings();
    	return asList(types);
    }
    
    private List asList(Set set) {
    	List list = new LinkedList();
    	for(Object o : set) {
    		list.add(o);
    	}
    	
    	return list;
    }
    
    private WeatherService getWeatherService() {
    	if(weatherService == null) {
    		weatherService = new WeatherServiceImpl(new InMemoryDao(), new StubApiService(""));
    	}
    	
    	return weatherService;
    }
}