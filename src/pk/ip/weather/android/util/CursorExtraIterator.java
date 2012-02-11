package pk.ip.weather.android.util;

import android.database.Cursor;

/**
 * Adapter for Cursor object
 */
public class CursorExtraIterator<T> implements ExtraIterator<T> {

	private Cursor cursor;
	private Hydrator<T> hydrator;
	
	public CursorExtraIterator(Cursor cursor, Hydrator<T> hydrator) {
		this.cursor = cursor;
		this.hydrator = hydrator;
	}

	@Override
	public void add(T arg0) {
	}

	@Override
	public boolean hasNext() {
		return cursor.getPosition() < (cursor.getCount() - 1);
//		return !cursor.isAfterLast();
	}

	@Override
	public boolean hasPrevious() {
		return cursor.getPosition() >= 0;
//		return !cursor.isBeforeFirst();
	}

	@Override
	public T next() {
		cursor.moveToNext();
		T object = hydrator.hydrate(cursor);
		
		return object;
	}

	@Override
	public int nextIndex() {
		return cursor.getPosition() + 1;
	}

	@Override
	public T previous() {
		T object = hydrator.hydrate(cursor);
		cursor.moveToPrevious();
		
		return object;
	}

	@Override
	public int previousIndex() {
		return cursor.getPosition();
	}

	@Override
	public void remove() {
	}

	@Override
	public void set(T arg0) {
	}

	@Override
	public void close() {
		cursor.close();
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}
	
	public static interface Hydrator<T> {
		public T hydrate(Cursor cursor);
	}
}
