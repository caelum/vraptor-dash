package br.com.caelum.vraptor.dash.uristats;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="DashUriStat")
@Entity(name="DashUriStat")
public class Stat {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private final String uri;
	
	private final String userId;
	
	private Calendar createdAt;
	
	Stat() {
		this("", "");
	}
	
	public Stat(String userId, String uri) {
		this.userId = userId;
		this.uri = uri;
		this.createdAt = Calendar.getInstance();
	}

}
