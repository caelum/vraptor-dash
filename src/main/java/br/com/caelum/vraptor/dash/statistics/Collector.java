package br.com.caelum.vraptor.dash.statistics;

import br.com.caelum.vraptor.freemarker.Template;

public interface Collector {

	void collect(Template template);
	
}