package pk.ip.weather.android;

import pk.ip.weather.android.util.ExtraIterator;
import pk.ip.weather.android.widget.ExtraIteratorAdapter;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GraphListActivity extends AbstractActivity {
	
	private ListView listView;
	private ExtraIterator<? extends Object> iter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_list);
		listView = (ListView) findViewById(R.id.listView);
		
		initView();
	}
	
	private void initView() {
		if(iter != null) {
			iter.close();
		}

		iter = getDao().findGraphs();
		ListAdapter adapter = new ExtraIteratorAdapter(this, R.layout.graph_list_item, iter);
		listView.setAdapter(adapter);
	}
	
	public void onResume(Bundle savedInstanceState) {
		super.onResume();
		initView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		if(iter != null) {
			iter.close();
		}
	}
}
