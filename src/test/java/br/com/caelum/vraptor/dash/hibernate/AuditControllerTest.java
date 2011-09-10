package br.com.caelum.vraptor.dash.hibernate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.NumberFormat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.junit.Test;
import org.mockito.Mockito;

import freemarker.template.TemplateException;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.freemarker.Template;
import br.com.caelum.vraptor.util.test.MockResult;

public class AuditControllerTest {

	@Test
	public void shouldIncludeHibernateStatisticConnectionCount() throws IOException, TemplateException {
		Freemarker marker = mock(Freemarker.class);
		Template controlPanel = mock(Template.class);
		Session session = mock(Session.class);
		Statistics statistics = mock(Statistics.class);
		when(statistics.getConnectCount()).thenReturn(1L);
		new AuditController(session , new MockResult(), marker).extractConnectionCount(NumberFormat.getNumberInstance(), statistics, controlPanel);
		verify(controlPanel).with("connectionCount", "1");
	}

}
