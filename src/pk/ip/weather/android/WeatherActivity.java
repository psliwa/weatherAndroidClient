package pk.ip.weather.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import pk.ip.weather.android.api.service.ApiException;
import pk.ip.weather.android.api.service.ApiService;
import pk.ip.weather.android.api.service.ApiServiceImpl;
import pk.ip.weather.android.dao.InMemoryDao;
import pk.ip.weather.android.domain.City;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.domain.GraphGrouping;
import pk.ip.weather.android.domain.GraphType;
import pk.ip.weather.android.service.WeatherService;
import pk.ip.weather.android.service.WeatherServiceImpl;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class WeatherActivity extends AbstractActivity {
	
	private static final String TAG = "WeaterActivity";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String PREFS_KEY_API_URL = "prefApiUrl";

	public static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	private Spinner citySpinner;
	private Spinner typeSpinner;
	private Spinner groupSpinner;
	private EditText dateFromInput;
	private EditText dateToInput;
	private Button showButton;
	
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
	}
	
	private void attachDatePicker(final EditText input, final Calendar defaultDate) {
		OnDateSetListener dateSetListener = new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
				input.setText(dateFormat.format(calendar.getTime()));
			}
		};

		final DatePickerDialog datePicker = new DatePickerDialog(this, dateSetListener, defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH));
		input.setText(formatDate(defaultDate));
		OnTouchListener listener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				datePicker.show();	
				return false;
			}
		};
		input.setOnTouchListener(listener);
	}
	
	private String formatDate(Calendar calendar) {
		return dateFormat.format(calendar.getTime());
	}
	
    private <T> void populateSpinner(Spinner spinner, List<T> objects) {
    	ArrayAdapter<T> adapter = new ArrayAdapter<T>(this, android.R.layout.simple_spinner_dropdown_item, objects);
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
					
					new AsyncTask<String, Integer, Graph>()
					{
						@Override
						protected Graph doInBackground(String... arg0) {
							InputStream is = null;
							try {
								URL url = getApiService().getGraphURL(dateFrom, dateTo, city, graphType, graphGrouping);

								Graph graph = new Graph();
								graph.setCity(city);
								graph.setType(graphType);
								graph.setGrouping(graphGrouping);
								graph.setDateFrom(dateFrom);
								graph.setDateTo(dateTo);
								String filename = writeFile(url);
								graph.setFilename(filename);
								
								graph = getWeatherApplication().getDao().saveGraph(graph);
								
								return graph;
							} catch (ApiException e) {
								handleException(e);
								Log.e(TAG, "Error", e);
							} catch (RuntimeException e) {
								handleException(e);
								Log.e(TAG, "Error", e);
							} catch (IOException e) {
								handleException(e);
								Log.e(TAG, "Error", e);
							} finally {
								if(is != null) {
									try {
										is.close();
									} catch (IOException e) {
										handleException(e);
										Log.e(TAG, "Error", e);
									}
								}
							}
							
							return null;
						}
						
						protected void onPostExecute(Graph graph) {
							if(graph != null) {
								Intent intent = new Intent(WeatherActivity.this, GraphItemActivity.class).putExtra("graph", graph);
								Bundle extras = new Bundle();
								extras.putSerializable("graph", graph);
								intent.putExtras(extras);
								startActivity(intent);
							}
						}
						
					}.execute(new String[0]);
				} catch (ValidationException e) {
					handleException(e);
				}
			}
		});
    }
    
    private String writeFile(URL url) throws IOException {
    	MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		String filename;
		try {
			filename = new String(digest.digest(url.toURI().toString().getBytes()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		OutputStream os = openFileOutput(filename, MODE_PRIVATE);
		InputStream is = null;
    	
        try {
    		is = url.openStream();

    		int length = 1024;
    		byte[] buffer = new byte[length];
    		int read = 0;

    		while((read = is.read(buffer, 0, length)) != -1) {
    			os.write(buffer, 0, read);
    		}
    		
    	} finally {
    		if(is != null) is.close();
    		if(os != null) os.close();
    	}
    	
    	return filename;
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