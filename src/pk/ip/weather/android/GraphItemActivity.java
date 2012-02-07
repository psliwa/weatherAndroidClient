package pk.ip.weather.android;

import java.io.IOException;
import java.net.MalformedURLException;
import pk.ip.weather.android.domain.Graph;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class GraphItemActivity extends AbstractActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_item);
		
		onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Graph graph = (Graph) getIntent().getSerializableExtra("graph");

		ImageView view = (ImageView) findViewById(R.id.graphContainer);
		
		try {
			Drawable drawable = Drawable.createFromStream(graph.getUri().toURL().openStream(), "image");
			view.setImageDrawable(drawable);
		} catch (MalformedURLException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}
}
