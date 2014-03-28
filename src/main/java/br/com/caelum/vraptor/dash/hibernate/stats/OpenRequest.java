package br.com.caelum.vraptor.dash.hibernate.stats;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Random;

import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.controller.ControllerMethod;

@Vetoed
public class OpenRequest {

	private final String controller;
	private final Calendar startingTime;
	private final long id;

	public OpenRequest(ControllerMethod method) {
		this(method.getMethod(), method.getController().getType());
	}

	public OpenRequest(Method method, Class<?> type) {
		this(method, type, Calendar.getInstance());
	}

	public OpenRequest(Method method, Class<?> type, Calendar startingTime) {
		this.controller = ("Method: " + method.getName()+ ", Type: " + type.getName());
		this.startingTime = startingTime;
		this.id = hashCode() * new Random().nextInt();
	}

	public String getController() {
		return controller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
		result = prime * result + ((controller == null) ? 0 : controller.hashCode());
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
		if (controller == null) {
			if (other.controller != null) {
				return false;
			}
		} else if (!controller.equals(other.controller)) {
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
