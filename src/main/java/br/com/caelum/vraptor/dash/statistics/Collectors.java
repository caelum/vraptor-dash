package br.com.caelum.vraptor.dash.statistics;

import java.util.List;

import br.com.caelum.vraptor.freemarker.Template;

public class Collectors implements Collector {

	private List<Collector> collectors;

	public Collectors(List<Collector> collectors) {
		this.collectors = collectors;
	}

	@Override
	public void collect(Template template) {
		for (Collector collector : collectors) {
			collector.collect(template);
		}
	}

}
