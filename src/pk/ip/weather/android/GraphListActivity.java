package pk.ip.weather.android;

import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.util.ExtraIterator;
import pk.ip.weather.android.widget.ExtraIteratorAdapter;
import pk.ip.weather.android.widget.GraphBinder;
import pk.ip.weather.android.widget.ExtraIteratorAdapter.Binder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GraphListActivity extends AbstractActivity implements OnItemClickListener {
	
	private static final String TAG = GraphListActivity.class.getSimpleName();
	
	private ListView listView;
	private ExtraIterator<? extends Object> iter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_list);
		listView = (ListView) findViewById(R.id.listView);

		listView.setOnItemClickListener(this);
		
		initView();
	}
	
	private void initView() {
		closeIter();

		iter = getDao().findGraphs();
//		cur
		Binder binder = GraphBinder.INSTANCE;
		ListAdapter adapter = new ExtraIteratorAdapter(this, binder, R.layout.graph_list_item, iter);
		listView.setAdapter(adapter);
	}
	
	private void closeIter() {
		if(iter != null) {
			iter.close();
			iter = null;
		}
		
		Log.d(TAG, "closeIter");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		closeIter();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		closeIter();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		if(iter != null) {
			iter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Graph graph = (Graph) listView.getItemAtPosition(position);
		startActivity(new Intent(GraphListActivity.this, GraphItemActivity.class).putExtra("graph", graph));		
	}
}
