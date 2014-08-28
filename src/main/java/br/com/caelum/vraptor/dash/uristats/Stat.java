package br.com.caelum.vraptor.dash.uristats;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An stat that represents a specific request to a specific uri from an user at
 * some time.
 * 
 * @author guilherme silveira
 */
@Table(name = "DashUriStat")
@Entity(name = "DashUriStat")
public class Stat {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private final String uri;

	@Lob
	private final String queryString;

	private final String userId;

	private final Calendar createdAt;

	private final String verb;

	private final String resource;

	private final String method;

	private final long time;

	private final String etag;

	private final int resultCode;

	private final String hadEtag;

	private final String cache;

	private final long size;

	private final String ipAdress;

	Stat() {
		this("", "", "", 0, "", "", "", "", 0, "unknown", "", 0,"");
	}

	public Stat(String userId, String uri, String queryString, long time,
			String verb, String resource, String action, String etag,
			int resultCode, String hadEtag, String cache, long size, String ipAdress) {
		this.userId = userId;
		this.uri = uri;
		this.queryString = queryString;
		this.time = time;
		this.verb = verb;
		this.resultCode = resultCode;
		this.hadEtag = hadEtag;
		this.cache = cache;
		this.size = size;
		this.ipAdress = ipAdress;
		this.createdAt = Calendar.getInstance();
		this.resource = resource;
		this.method = action;
		this.etag = etag;
	}

	private static final Logger LOG = LoggerFactory.getLogger(Stat.class);

	public void log() {
		if ("GET".equalsIgnoreCase(verb)) {
			LOG.info(String.format(
					"%tc-[%s]-%s-%s-%s-%d-%s-%d-[%s]-%d-[%s]-%s-%s-%s", createdAt,
					etag, verb, resource, method, time, hadEtag, resultCode,
					cache, size, userId, uri, queryString,ipAdress));
		} else {
			LOG.info(String.format(
					"%tc-[%s]-%s-%s-%s-%d-%s-%d-[%s]-%d-[%s]-%s-%s", createdAt,
					etag, verb, resource, method, time, hadEtag, resultCode,
					cache, size, userId, uri,ipAdress));
		}
	}
}
