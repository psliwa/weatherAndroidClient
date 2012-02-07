package pk.ip.weather.android.domain;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

public class Graph implements Serializable {

	private static final long serialVersionUID = -3207554572057505032L;

	private Long id;
	private City city;
	private GraphGrouping grouping;
	private GraphType type;
	private Date dateFrom;
	private Date dateTo;
	private URI uri;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public GraphGrouping getGrouping() {
		return grouping;
	}
	public void setGrouping(GraphGrouping grouping) {
		this.grouping = grouping;
	}
	public GraphType getType() {
		return type;
	}
	public void setType(GraphType type) {
		this.type = type;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
	public URI getUri() {
		return uri;
	}
	public void setUri(URI file) {
		this.uri = file;
	}
}
