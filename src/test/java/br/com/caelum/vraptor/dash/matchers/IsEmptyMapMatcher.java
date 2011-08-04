package br.com.caelum.vraptor.dash.matchers;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsEmptyMapMatcher extends TypeSafeMatcher<Map<?,?>> {

	@Override
	public void describeTo(Description description) {
		description.appendText("An empty map");
	}

	@Override
	protected boolean matchesSafely(Map<?,?> map) {
		return map.isEmpty();
	}

	@Factory
	public static Matcher<Map<?, ?>> isEmptyMap() {
		return new IsEmptyMapMatcher();
	}

}
