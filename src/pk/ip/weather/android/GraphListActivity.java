package pk.ip.weather.android;

import java.util.Iterator;

import pk.ip.weather.android.domain.Graph;

import android.os.Bundle;
import android.widget.ListView;

public class GraphListActivity extends AbstractActivity {
	
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_list);
		listView = (ListView) findViewById(R.id.listView);
		
		Iterator<Graph> graphs = getDao().findGraphs();
	}
}
