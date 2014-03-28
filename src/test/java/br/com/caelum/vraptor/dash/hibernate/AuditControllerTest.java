package br.com.caelum.vraptor.dash.hibernate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.runtime.RuntimeStatisticsCollector;
import br.com.caelum.vraptor.dash.statistics.Collector;
import br.com.caelum.vraptor.dash.statistics.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class AuditControllerTest {

	@Mock
	private Result result;
	@Mock
	private Session session;
	@Mock
	private Statistics statistics;
	@Mock
	private HibernateAuditAwareUser user;


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
	public void shouldIncludeHibernateStatisticConnectionCount() throws Exception {
		when(statistics.getConnectCount()).thenReturn(1L);
		when(user.canSeeHibernateAudits()).thenReturn(true);
		new AuditController(session, user, result).collectStatistics(hibernateCollector);
		verify(result).include("connectionCount", "1");
	}

	@Test
	public void shouldIncludeHibernateStatisticSecondLevelCacheMissCount() throws Exception {
		when(statistics.getSecondLevelCacheMissCount()).thenReturn(2L);
		when(user.canSeeHibernateAudits()).thenReturn(true);
		new AuditController(session, user, result).collectStatistics(hibernateCollector);
		verify(result).include("secondLevelCacheMissCount", "2");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCacheHitCount() throws Exception {
		when(statistics.getSecondLevelCacheHitCount()).thenReturn(3L);
		when(user.canSeeHibernateAudits()).thenReturn(true);
		new AuditController(session, user, result).collectStatistics(hibernateCollector);
		verify(result).include("secondLevelCacheHitCount", "3");
	}

	@Test
	public void shouldIncludeHibernateStatistictSecondLevelCachePutCount() throws Exception {
		when(statistics.getSecondLevelCachePutCount()).thenReturn(4L);
		when(user.canSeeHibernateAudits()).thenReturn(true);
		new AuditController(session, user, result).collectStatistics(hibernateCollector);
		verify(result).include("secondLevelCachePutCount", "4");
	}

	@Test
	public void shouldIncludeVmStatisticTotalMemory(){
		when(runtime.totalMemory()).thenReturn(1L);
		new AuditController(session, user, result).collectStatistics(runtimeCollector);
		verify(result).include("totalMemory", "1");
	}

	@Test
	public void shouldIncludeVmStatisticUsedMemory(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(2L);
		new AuditController(session, user, result).collectStatistics(runtimeCollector);
		verify(result).include("usedMemory", "2");
	}

	@Test
	public void shouldIncludeVmStatisticUsedMemoryPerCent(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(0L);
		new AuditController(session, user, result).collectStatistics(runtimeCollector);
		verify(result).include("usedMemoryPerCent", "100%");
	}

	@Test
	public void shouldIncludeVmStatisticFreeMemory(){
		when(runtime.freeMemory()).thenReturn(5L);
		new AuditController(session, user, result).collectStatistics(runtimeCollector);
		verify(result).include("freeMemory", "5");
	}

	@Test
	public void shouldIncludeVmStatisticFreeMemoryPercent(){
		when(runtime.totalMemory()).thenReturn(4L);
		when(runtime.freeMemory()).thenReturn(0L);
		new AuditController(session, user, result).collectStatistics(runtimeCollector);
		verify(result).include("freeMemoryPerCent", "0%");
	}
}
