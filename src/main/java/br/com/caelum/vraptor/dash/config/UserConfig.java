package br.com.caelum.vraptor.dash.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An user configuration.
 * @author guilherme silveira
 */
@Table(name="DashUserConfig")
@Entity(name="DashUserConfig")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserConfig {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String key;
	
	private String value;
	
	private String userId;
	
	UserConfig() {
		this("", "", "");
	}

	public UserConfig(String key, String value, String userId) {
		this.key = key;
		this.value = value;
		this.userId = userId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
