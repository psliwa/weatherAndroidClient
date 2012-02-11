package pk.ip.weather.android.domain;

import java.io.Serializable;

public interface DomainObject<T> {
	public T getId();
	public void setId(T id);
}
