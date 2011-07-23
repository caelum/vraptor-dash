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
	
	private final Calendar createdAt;
	
	private final String verb;
	
	private final String resource;
	
	private final String method;

	private final long time;
	
	private final String etag;
	
	private final int resultCode;

	private final boolean hadEtag;

	private final String cache;

	private final int size;
	
	Stat() {
		this("", "", 0, "", "", "", "",0 , false, "", 0);
	}
	
	public Stat(String userId, String uri, long time, String verb, String resource, String action, String etag, int resultCode, boolean hadEtag, String cache, int size) {
		this.userId = userId;
		this.uri = uri;
		this.time = time;
		this.verb = verb;
		this.resultCode = resultCode;
		this.hadEtag = hadEtag;
		this.cache = cache;
		this.size = size;
		this.createdAt = Calendar.getInstance();
		this.resource = resource;
		this.method = action;
		this.etag = etag;
	}

}
