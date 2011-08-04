package br.com.caelum.vraptor.dash.hibernate;

import static br.com.caelum.vraptor.dash.matchers.IsEmptyMapMatcher.isEmptyMap;
import static br.com.caelum.vraptor.dash.matchers.IsOfResourceMatcher.isOpenRequestOfResource;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequest;
import br.com.caelum.vraptor.dash.hibernate.stats.OpenRequests;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;

public class OpenRequestsTest {

	private DefaultResourceMethod wrapped;
	private OpenRequests openRequests;

	@Before
	public void setup() {
		Class<?> type = Class.class;
		Method method = type.getDeclaredMethods()[0];
		wrapped = new DefaultResourceMethod(new DefaultResourceClass(type), method);

		openRequests = new OpenRequests();
	}

	@Test
	public void createsAnOpenRequestForTheAddedResourceMethod() throws Exception {
		assertThat(openRequests.add(wrapped), isOpenRequestOfResource(wrapped));
	}

	@Test
	public void returnsAMapOfOpenRequestsWithTheAddedResourceMethod() throws Exception {
		assertEquals(0, openRequests.toMap().size());
		OpenRequest added = openRequests.add(wrapped);
		assertThat(openRequests.toMap(), hasEntry(added.getId(), added));
	}

	@Test
	public void returnsAnEmptyMapWhenAllTheAddedRequestsAreRemoved() throws Exception {
		OpenRequest request = openRequests.add(wrapped);
		openRequests.remove(request);
		assertThat(openRequests.toMap(), isEmptyMap());
	}

}
