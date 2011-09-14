package br.com.caelum.vraptor.dash.hibernate;

import java.text.NumberFormat;

import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.dash.statistics.Collector;
import br.com.caelum.vraptor.freemarker.Template;

public class HibernateStatisticsCollector implements Collector {

	private NumberFormat decimalFormat;
	private Statistics statistics;

	public HibernateStatisticsCollector(Statistics statistics) {
		this.statistics = statistics;
		this.decimalFormat = NumberFormat.getNumberInstance();
		
	}

	public void collect(Template template) {
		template.with("connectionCount", decimalFormat.format(statistics.getConnectCount()));
		template.with("secondLevelCacheMissCount", decimalFormat.format(statistics.getSecondLevelCacheMissCount()));
		template.with("secondLevelCacheHitCount", decimalFormat.format(statistics.getSecondLevelCacheHitCount()));
		template.with("secondLevelCachePutCount", decimalFormat.format(statistics.getSecondLevelCachePutCount()));
	}

}
