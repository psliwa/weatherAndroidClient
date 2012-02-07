package pk.ip.weather.android.widget;

import pk.ip.weather.android.util.ExtraIterator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExtraIteratorAdapter extends BaseAdapter {

	private ExtraIterator<? extends Object> iter;
	private LayoutInflater inflater;
	private int resource;
	
	public ExtraIteratorAdapter(Context context, int resource, ExtraIterator<? extends Object> iter) {
		this.iter = iter;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}
	
	@Override
	public int getCount() {
		return iter.getCount();
	}

	@Override
	public Object getItem(int index) {
		Object result = null;
		Log.d("iter", "index: "+index);

		int objectIndex = -1;

		if(iter.hasNext() && index >= iter.nextIndex()) {
			while(iter.hasNext() && index >= iter.nextIndex()) {
				objectIndex = iter.nextIndex();
				result = iter.next();
			}
		}else if(iter.hasPrevious() && index <= iter.previousIndex()) {
			while(iter.hasPrevious() && index <= iter.previousIndex()) {
				objectIndex = iter.previousIndex();
				result = iter.previous();
			}
		}
		
		return objectIndex == index ? result : null;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(resource, parent, false);
		}
		
		((TextView) convertView).setText(getItem(position).toString());
		
		return convertView;
	}
}
