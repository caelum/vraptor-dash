package br.com.caelum.vraptor.dash.hibernate;

import java.text.NumberFormat;

import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.freemarker.Template;

public class HibernateStatisticsCollector {

	private NumberFormat decimalFormat;
	private Statistics statistics;

	public HibernateStatisticsCollector(Statistics statistics) {
		this.statistics = statistics;
		this.decimalFormat = NumberFormat.getNumberInstance();
		
	}

	public void collect(Template template) {
		template.with("connectionCount", decimalFormat.format(statistics.getConnectCount()));
	}

}
