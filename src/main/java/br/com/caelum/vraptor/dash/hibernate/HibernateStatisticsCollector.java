package br.com.caelum.vraptor.dash.hibernate;

import java.text.NumberFormat;

import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.statistics.Collector;

public class HibernateStatisticsCollector implements Collector {

	private NumberFormat decimalFormat;
	private Statistics statistics;

	public HibernateStatisticsCollector(Statistics statistics) {
		this.statistics = statistics;
		this.decimalFormat = NumberFormat.getNumberInstance();
		
	}

	public void collect(Result result) {
		result.include("connectionCount", decimalFormat.format(statistics.getConnectCount()));
		result.include("secondLevelCacheMissCount", decimalFormat.format(statistics.getSecondLevelCacheMissCount()));
		result.include("secondLevelCacheHitCount", decimalFormat.format(statistics.getSecondLevelCacheHitCount()));
		result.include("secondLevelCachePutCount", decimalFormat.format(statistics.getSecondLevelCachePutCount()));
	}

}
