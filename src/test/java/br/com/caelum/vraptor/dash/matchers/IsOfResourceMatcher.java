package br.com.caelum.vraptor.dash.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.resource.ResourceMethod;

public final class IsOfResourceMatcher extends TypeSafeMatcher<OpenRequest> {
	private final ResourceMethod resourceMethod;

	private IsOfResourceMatcher(ResourceMethod resourceMethod) {
		this.resourceMethod = resourceMethod;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("An OpenRequest of the resource ");
		description.appendValue(resourceMethod);
	}

	@Override
	protected boolean matchesSafely(OpenRequest item) {
		return new OpenRequest(this.resourceMethod).getResource().equals(item.getResource());
	}

	@Override
	protected void describeMismatchSafely(OpenRequest item, Description mismatchDescription) {
		// TODO Auto-generated method stub

	}

	@Factory
	public static IsOfResourceMatcher isOpenRequestOfResource(ResourceMethod resourceMethod) {
		return new IsOfResourceMatcher(resourceMethod);
	}
}