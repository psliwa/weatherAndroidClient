package pk.ip.weather.android.util;

import java.util.ListIterator;

public interface ExtraIterator<E> extends ListIterator<E> {
	public void close();
	public int getCount();
}
