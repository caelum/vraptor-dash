package br.com.caelum.vraptor.dash.hibernate.stats;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Random;

import br.com.caelum.vraptor.resource.ResourceMethod;

public class OpenRequest {

	private final String resource;
	private final Calendar startingTime;
	private final long id;

	public OpenRequest(ResourceMethod resourceMethod) {
		this(resourceMethod.getMethod(), resourceMethod.getResource().getType());
	}

	public OpenRequest(Method method, Class<?> type) {
		this(method, type, Calendar.getInstance());
	}

	public OpenRequest(Method method, Class<?> type, Calendar startingTime) {
		this.resource = ("Method: " + method.getName()+ ", Type: " + type.getName());
		this.startingTime = startingTime;
		this.id = hashCode() * new Random().nextInt();
	}

	public String getResource() {
		return resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OpenRequest other = (OpenRequest) obj;
		if (startingTime == null) {
			if (other.startingTime != null) {
				return false;
			}
		} else if (!startingTime.equals(other.startingTime)) {
			return false;
		}
		if (resource == null) {
			if (other.resource != null) {
				return false;
			}
		} else if (!resource.equals(other.resource)) {
			return false;
		}
		return true;
	}

	public Long getLivingTimeInSeconds() {
		return (Calendar.getInstance().getTimeInMillis() - this.startingTime.getTimeInMillis()) / 1000;
	}

	public Long getId() {
		return this.id;
	}
}
