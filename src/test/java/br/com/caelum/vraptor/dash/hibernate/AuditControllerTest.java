package br.com.caelum.vraptor.dash.hibernate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.text.NumberFormat;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.freemarker.Template;
import br.com.caelum.vraptor.util.test.MockResult;
import freemarker.template.TemplateException;

public class AuditControllerTest {

	@Mock
	private Freemarker marker;
	@Mock
	private Template controlPanel;
	@Mock
	private Session session;
	@Mock
	private Statistics statistics;
	
	@Before
	public void setup() {
		initMocks(this);
	}
	
	@Test
	public void shouldIncludeHibernateStatisticConnectionCount() throws IOException, TemplateException {
		when(statistics.getConnectCount()).thenReturn(1L);
		new AuditController(session , new MockResult(), marker).extractConnectionCount(NumberFormat.getNumberInstance(), statistics, controlPanel);
		verify(controlPanel).with("connectionCount", "1");
	}

	@Test
	public void shouldIncludeHibernateStatisticSecondLevelCacheMissCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCacheMissCount()).thenReturn(2L);
		new AuditController(session , new MockResult(), marker).extractConnectionCount(NumberFormat.getNumberInstance(), statistics, controlPanel);
		verify(controlPanel).with("secondLevelCacheMissCount", "2");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCacheHitCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCacheHitCount()).thenReturn(3L);
		new AuditController(session , new MockResult(), marker).extractConnectionCount(NumberFormat.getNumberInstance(), statistics, controlPanel);
		verify(controlPanel).with("secondLevelCacheHitCount", "3");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCachePutCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCachePutCount()).thenReturn(4L);
		new AuditController(session , new MockResult(), marker).extractConnectionCount(NumberFormat.getNumberInstance(), statistics, controlPanel);
		verify(controlPanel).with("secondLevelCachePutCount", "4");
	}

}
