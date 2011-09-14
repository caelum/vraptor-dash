package br.com.caelum.vraptor.dash.hibernate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.caelum.vraptor.dash.runtime.RuntimeStatisticsCollector;
import br.com.caelum.vraptor.dash.statistics.Collector;
import br.com.caelum.vraptor.dash.statistics.Collectors;
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
	@Mock
	private Runtime runtime;
	private Collectors hibernateCollector;
	private Collectors runtimeCollector;
	
	@Before
	public void setup() {
		initMocks(this);
		hibernateCollector = new Collectors(Arrays.<Collector>asList(new HibernateStatisticsCollector(statistics)));
		runtimeCollector = new Collectors(Arrays.<Collector>asList(new RuntimeStatisticsCollector(runtime)));
	}
	
	@Test
	public void shouldIncludeHibernateStatisticConnectionCount() throws IOException, TemplateException {
		when(statistics.getConnectCount()).thenReturn(1L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, hibernateCollector);
		verify(controlPanel).with("connectionCount", "1");
	}

	@Test
	public void shouldIncludeHibernateStatisticSecondLevelCacheMissCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCacheMissCount()).thenReturn(2L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, hibernateCollector);
		verify(controlPanel).with("secondLevelCacheMissCount", "2");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCacheHitCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCacheHitCount()).thenReturn(3L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, hibernateCollector);
		verify(controlPanel).with("secondLevelCacheHitCount", "3");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCachePutCount() throws IOException, TemplateException {
		when(statistics.getSecondLevelCachePutCount()).thenReturn(4L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, hibernateCollector);
		verify(controlPanel).with("secondLevelCachePutCount", "4");
	}

	@Test
	public void shouldIncludeVmStatisticTotalMemory(){
		when(runtime.totalMemory()).thenReturn(1L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, runtimeCollector);
		verify(controlPanel).with("totalMemory", "1");
	}

	@Test
	public void shouldIncludeVmStatisticUsedMemory(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(2L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, runtimeCollector);
		verify(controlPanel).with("usedMemory", "2");
	}

	@Test
	public void shouldIncludeVmStatisticUsedMemoryPerCent(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(0L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, runtimeCollector);
		verify(controlPanel).with("usedMemoryPerCent", "100%");
	}

	@Test
	public void shouldIncludeVmStatisticFreeMemory(){
		when(runtime.freeMemory()).thenReturn(5L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, runtimeCollector);
		verify(controlPanel).with("freeMemory", "5");
	}

	@Test
	public void shouldIncludeVmStatisticFreeMemoryPercent(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(0L);
		new AuditController(session , new MockResult(), marker).collectStatistics(controlPanel, runtimeCollector);
		verify(controlPanel).with("freeMemoryPerCent", "0%");
	}
}
