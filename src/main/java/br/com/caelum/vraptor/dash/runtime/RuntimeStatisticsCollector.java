package br.com.caelum.vraptor.dash.runtime;

import java.text.NumberFormat;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.dash.statistics.Collector;

public class RuntimeStatisticsCollector implements Collector {

	private Runtime runtime;

	public RuntimeStatisticsCollector(Runtime runtime) {
		this.runtime = runtime;
	}

	public void collect(Result result) {
		NumberFormat decimalFormat = NumberFormat.getNumberInstance();
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		result.include("totalMemory", decimalFormat.format(runtime.totalMemory()));
		result.include("usedMemory", decimalFormat.format(usedMemory()));
		result.include("usedMemoryPerCent", percentFormat.format(usedMemory() / runtime.totalMemory()));
		result.include("freeMemory", decimalFormat.format(runtime.freeMemory()));
		result.include("freeMemoryPerCent", percentFormat.format((double)runtime.freeMemory() / runtime.totalMemory()));
	}

	private double usedMemory() {
		return runtime.totalMemory() - runtime.freeMemory();
	}

}
