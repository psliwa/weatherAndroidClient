package pk.ip.weather.android;

import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.util.ExtraIterator;
import pk.ip.weather.android.widget.ExtraIteratorAdapter;
import pk.ip.weather.android.widget.GraphBinder;
import pk.ip.weather.android.widget.ExtraIteratorAdapter.Binder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GraphListActivity extends AbstractActivity implements OnItemClickListener {
	
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
		if(iter != null) {
			iter.close();
		}

		iter = getDao().findGraphs();
		Binder binder = GraphBinder.INSTANCE;
		ListAdapter adapter = new ExtraIteratorAdapter(this, binder, R.layout.graph_list_item, iter);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Graph graph = (Graph) listView.getItemAtPosition(position);
		startActivity(new Intent(GraphListActivity.this, GraphItemActivity.class).putExtra("graph", graph));		
	}
}
