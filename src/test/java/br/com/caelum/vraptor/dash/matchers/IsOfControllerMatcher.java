package br.com.caelum.vraptor.dash.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;

public final class IsOfControllerMatcher extends TypeSafeMatcher<OpenRequest> {
	private final ControllerMethod resourceMethod;

	private IsOfControllerMatcher(ControllerMethod resourceMethod) {
		this.resourceMethod = resourceMethod;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("An OpenRequest of the resource ");
		description.appendValue(resourceMethod);
	}

	@Override
	protected boolean matchesSafely(OpenRequest item) {
		return new OpenRequest(this.resourceMethod).getController().equals(item.getController());
	}

	@Override
	protected void describeMismatchSafely(OpenRequest item, Description mismatchDescription) {
		// TODO Auto-generated method stub

	}

	@Factory
	public static IsOfControllerMatcher isOpenRequestOfController(ControllerMethod resourceMethod) {
		return new IsOfControllerMatcher(resourceMethod);
	}
}