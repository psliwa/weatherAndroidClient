package pk.ip.weather.android.dao;

import android.content.ContentValues;
import pk.ip.weather.android.domain.DomainObject;

interface Mapper<T extends DomainObject<?>> {
	public void doMapping(T object, ContentValues values);
}
