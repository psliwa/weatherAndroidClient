package pk.ip.weather.android;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pk.ip.weather.android.api.service.ApiService;
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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	
	private static final String TAG = "WeaterActivity";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	private Spinner citySpinner;
	private Spinner typeSpinner;
	private Spinner groupSpinner;
	private EditText dateFromInput;
	private EditText dateToInput;
	private Button showButton;
	private ImageView graphContainer;
	
	private WeatherService weatherService;
	private ApiService apiService;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        
        viewProcess();
    }
    
    private void viewProcess() {
    	assignWidgets();
    	
    	Calendar defaultFrom = new GregorianCalendar();
    	defaultFrom.add(Calendar.MONTH, -1);

    	attachDatePicker(dateFromInput, defaultFrom);
    	attachDatePicker(dateToInput, new GregorianCalendar());    	
    	
    	populateSpinner(citySpinner, getCities());
    	populateSpinner(typeSpinner, getGraphTypes());
    	populateSpinner(groupSpinner, getGraphGroupings());
    	
    	attachListeners();
    }
    
	private void assignWidgets() {
		citySpinner = (Spinner) findViewById(R.id.cityInput);
    	typeSpinner = (Spinner) findViewById(R.id.typeInput);
    	groupSpinner = (Spinner) findViewById(R.id.groupInput);
    	dateFromInput = (EditText) findViewById(R.id.dateFromInput);
    	dateToInput = (EditText) findViewById(R.id.dateToInput);
    	showButton = (Button) findViewById(R.id.showButton);
    	graphContainer = (ImageView) findViewById(R.id.graphContainer);
	}
	
	private void attachDatePicker(final EditText input, final Calendar defaultDate) {		
		final DatePickerDialog datePicker = new DatePickerDialog(this, new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = new GregorianCalendar();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				
				DateFormat format = new SimpleDateFormat(DATE_FORMAT);
				input.setText(format.format(calendar.getTime()));
			}
		}, defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH));
		input.setText(formatDate(new GregorianCalendar()));
		input.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				datePicker.show();	
				return false;
			}
		});
	}
	
	private String formatDate(Calendar calendar) {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.format(calendar.getTime());
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
    		weatherService = new WeatherServiceImpl(new InMemoryDao(), getApiService());
    	}
    	
    	return weatherService;
    }
    
    private ApiService getApiService() {
    	if(apiService == null) {
    		apiService = new StubApiService("");
    	}
    	
    	return apiService;
    }
    
    private void attachListeners() {
    	showButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try	{
					final City city = (City) citySpinner.getSelectedItem();
					final GraphType graphType = (GraphType) typeSpinner.getSelectedItem();
					final GraphGrouping graphGrouping = (GraphGrouping) groupSpinner.getSelectedItem();
					
					final Date dateFrom = dateFromString(dateFromInput.getText().toString());
					final Date dateTo = dateFromString(dateToInput.getText().toString());
					
					new AsyncTask<String, Integer, Drawable>()
					{
						@Override
						protected Drawable doInBackground(String... arg0) {
							Drawable drawable = null;
							InputStream is = null;
							try {
								is = getApiService().getGraphURL(dateFrom, dateTo, city, graphType, graphGrouping).openStream();
								drawable = Drawable.createFromStream(is, "image");
							} catch (IOException e) {
								//TODO: komunikat
								Log.e(TAG, "Error", e);
							} finally {
								if(is != null) {
									try {
										is.close();
									} catch (IOException e) {
										//TODO: komunikat
										Log.e(TAG, "Error", e);
									}
								}
							}
							
							return drawable;
						}
						
						protected void onPostExecute(Drawable drawable) {
							graphContainer.setImageDrawable(drawable);
						}
						
					}.execute(new String[0]);
				} catch (ValidationException e) {
					handleException(e);
				}
			}
		});
    }
    
    private void handleException(Throwable e) {
    	Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    
    private Date dateFromString(String stringDate) {
    	DateFormat format = new SimpleDateFormat(DATE_FORMAT);
    	try {
			return format.parse(stringDate);
		} catch (ParseException e) {
			throw new ValidationException("Date format is invalid", e);
		}
    }
}