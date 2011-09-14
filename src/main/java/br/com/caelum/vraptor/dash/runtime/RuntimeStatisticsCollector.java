package br.com.caelum.vraptor.dash.runtime;

import java.text.NumberFormat;

import br.com.caelum.vraptor.dash.statistics.Collector;
import br.com.caelum.vraptor.freemarker.Template;

public class RuntimeStatisticsCollector implements Collector {

	private Runtime runtime;

	public RuntimeStatisticsCollector(Runtime runtime) {
		this.runtime = runtime;
	}

	public void collect(Template controlPanel) {
		NumberFormat decimalFormat = NumberFormat.getNumberInstance();
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		controlPanel.with("totalMemory", decimalFormat.format(runtime.totalMemory()));
		controlPanel.with("usedMemory", decimalFormat.format(usedMemory()));
		controlPanel.with("usedMemoryPerCent", percentFormat.format(usedMemory() / runtime.totalMemory()));
	}

	private double usedMemory() {
		return runtime.totalMemory() - runtime.freeMemory();
	}

}
