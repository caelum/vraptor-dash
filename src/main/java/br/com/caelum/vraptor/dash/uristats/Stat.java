package br.com.caelum.vraptor.dash.uristats;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An stat that represents a specific request to a specific uri from an user at some time.
 * @author guilherme silveira
 */
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
