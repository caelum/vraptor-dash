package br.com.caelum.vraptor.dash.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicMonitor {

	private static final Logger logger = LoggerFactory
			.getLogger(BasicMonitor.class);

	public static void logStats() {
		ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		MemoryMXBean memory = ManagementFactory.getMemoryMXBean();

		checkDeadlocks(threads);
		logger.info("[Used {}M/Max {}M] [Threads {}]",
				new Object[] {
						memory.getHeapMemoryUsage().getUsed() / (1024 * 1024),
						memory.getHeapMemoryUsage().getMax() / (1024 * 1024),
						threads.getThreadCount() });
	}

	private static void checkDeadlocks(ThreadMXBean threads) {
		{
			long[] ids = threads.findDeadlockedThreads();
			if (ids != null) {
				for (long id : ids) {
					logger.info("Thread com dead lock? {}",
							threads.getThreadInfo(id).getThreadName());
					logTrace(threads.getThreadInfo(id).getStackTrace());
				}
			}
		}
		{
			long[] ids = threads.findMonitorDeadlockedThreads();
			if (ids != null) {
				for (long id : ids) {
					logger.info("Thread com synchronized lock? {}", threads
							.getThreadInfo(id).getThreadName());
					logTrace(threads.getThreadInfo(id).getStackTrace());
				}
			}
		}

	}

	private static void logTrace(StackTraceElement[] stackTrace) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement e : stackTrace) {
			sb.append(e.getClassName() + e.getMethodName() + ":"
					+ e.getLineNumber() + "\n");
		}
		logger.info(sb.toString());
	}

}