package pk.ip.weather.android.widget;

import pk.ip.weather.android.util.ExtraIterator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ExtraIteratorAdapter extends BaseAdapter {

	private ExtraIterator<? extends Object> iter;
	
	public ExtraIteratorAdapter(ExtraIterator<? extends Object> iter) {
		this.iter = iter;
	}
	
	@Override
	public int getCount() {
		return iter.getCount();
	}

	@Override
	public Object getItem(int index) {
		Object result = null;
		int objectIndex = -1;

		if(iter.hasNext() && index >= iter.nextIndex()) {
			while(iter.hasNext() && index >= iter.nextIndex()) {
				objectIndex = iter.nextIndex();
				result = iter.next();
			}
		}else if(iter.hasPrevious() && index <= iter.previousIndex()) {
			while(iter.hasPrevious() && index <= iter.previousIndex()) {
				objectIndex = iter.previousIndex();
				result = iter.next();
			}
		}
		
		return objectIndex == index ? result : null;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
