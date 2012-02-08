package pk.ip.weather.android.util;

public class ReverseExtraIterator<T> implements ExtraIterator<T>{

	private ExtraIterator<T> iter;
	
	public ReverseExtraIterator(ExtraIterator<T> iter) {
		this.iter = iter;
	}
	
	@Override
	public void add(T object) {
		
	}

	@Override
	public boolean hasNext() {
		return iter.hasPrevious();
	}

	@Override
	public boolean hasPrevious() {
		return iter.hasNext();
	}

	@Override
	public T next() {
		return iter.previous();
	}

	@Override
	public int nextIndex() {
		return iter.getCount() - 1 - iter.previousIndex();
	}

	@Override
	public T previous() {
		return iter.next();
	}

	@Override
	public int previousIndex() {
		return iter.getCount() - 1 - iter.nextIndex();
	}

	@Override
	public void remove() {
		
	}

	@Override
	public void set(T arg0) {
		
	}

	@Override
	public void close() {
		iter.close();
		
	}

	@Override
	public int getCount() {
		return iter.getCount();
	}
}
