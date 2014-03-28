package br.com.caelum.vraptor.dash.hibernate.stats;

import javax.enterprise.inject.Vetoed;

import org.hibernate.stat.QueryStatistics;

@Vetoed
public class QueryStatsWrapper {

	private final String query;
	private final QueryStatistics queryStats;

	public QueryStatsWrapper(String query, QueryStatistics queryStats) {
		this.query = query;
		this.queryStats = queryStats;
	}

	public long getCacheHitCount() {
		return queryStats.getCacheHitCount();
	}

	public long getCacheMissCount() {
		return queryStats.getCacheMissCount();
	}

	public long getCachePutCount() {
		return queryStats.getCachePutCount();
	}

	public long getExecutionAvgTime() {
		return queryStats.getExecutionAvgTime();
	}

	public long getExecutionCount() {
		return queryStats.getExecutionCount();
	}

	public String getQuery() {
		return query;
	}

}
