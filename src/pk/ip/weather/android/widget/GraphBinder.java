package pk.ip.weather.android.widget;

import java.text.DateFormat;

import pk.ip.weather.android.R;
import pk.ip.weather.android.WeatherActivity;
import pk.ip.weather.android.domain.Graph;
import pk.ip.weather.android.widget.ExtraIteratorAdapter.Binder;
import android.view.View;
import android.widget.TextView;

public class GraphBinder implements Binder {
	private DateFormat dateFormat = WeatherActivity.dateFormat;
	
	public static final Binder INSTANCE = new GraphBinder();
	
	@Override
	public void bindObject(Object object, View view) {
		Graph graph = (Graph) object;
		TextView cityView = (TextView) view.findViewById(R.id.textCity);
		TextView typeView = (TextView) view.findViewById(R.id.textType);
		TextView dateView = (TextView) view.findViewById(R.id.textDate);
		TextView groupingView = (TextView) view.findViewById(R.id.textGrouping);

		cityView.setText(graph.getCity().toString());
		typeView.setText(graph.getType().toString());
		dateView.setText(dateFormat.format(graph.getDateFrom())+" - "+dateFormat.format(graph.getDateTo()));
		groupingView.setText(graph.getGrouping().toString());
	}
}
