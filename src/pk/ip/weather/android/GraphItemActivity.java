package pk.ip.weather.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.widget.GraphBinder;
import pk.ip.weather.android.widget.ExtraIteratorAdapter.Binder;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class GraphItemActivity extends AbstractActivity {
	private static String TAG = GraphItemActivity.class.getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_item);
		
		onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Graph graph = (Graph) getIntent().getSerializableExtra("graph");

		Binder binder = GraphBinder.INSTANCE;
		View view = findViewById(R.id.layout);

		binder.bindObject(graph, view);
		
		ImageView imageView = (ImageView) findViewById(R.id.graphContainer);
		
		try {
			InputStream is = null;
			try {
				is = graph.openStream();
				
				Drawable drawable = Drawable.createFromStream(is, graph.getFilename());
				imageView.setImageDrawable(drawable);
			} finally {
				if(is != null) is.close();
			}
		} catch (MalformedURLException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}
}
