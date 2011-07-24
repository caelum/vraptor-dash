<%@ page import="java.io.*"%>
<%@ page import="java.lang.Thread.State"%>
<%@ page import="java.lang.management.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.slf4j.*"%>

<%! 
public class BasicMonitor {

	public void printThreads(Writer out) {
		Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
		for (State st : State.values()) {
			for (Thread t : threads.keySet()) {
				if (t.getState().equals(st)) {
					print(new PrintWriter(out, true), t, threads.get(t));
				}
			}
		}

	}

	private void print(PrintWriter out, Thread t, StackTraceElement[] els) {
		out.println("------------------------------------------------------------------------------------<br/>");
		out.println("------------------------------------------------------------------------------------<br/>");
		out.println("------------------------------------------------------------------------------------<br/>");
		out.println("Nome da thread:               " + t.getName() + "<br/>");
		out.println("Status da thread:             " + t.getState().name()
				+ "<br/>");
		if (els != null) {
			for (StackTraceElement el : els) {
				out.println("[" + el.getFileName() + ":" + el.getLineNumber()
						+ "]     " + el.getClassName() + "."
						+ el.getMethodName() + "<br/>");
			}
		}
		out.println("------------------------------------------------------------------------------------<br/>");
		out.println("------------------------------------------------------------------------------------<br/>");
		out.println("------------------------------------------------------------------------------------<br/>");
	}

	private final Logger logger = LoggerFactory
			.getLogger(BasicMonitor.class);

	public void logStats() {

		try {
			ThreadMXBean threads = ManagementFactory.getThreadMXBean();
			checkDeadlocks(threads);
			logger.info("[Used {}M/Max {}M] [Threads {}]", new Object[] {
					threads.getThreadCount() });
		} catch (Exception ex) {
			logger.warn("Stats generation error", ex);
		}

		try {
			MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
			logger.info("[Used {}M/Max {}M]", new Object[] {
					memory.getHeapMemoryUsage().getUsed() / (1024 * 1024),
					memory.getHeapMemoryUsage().getMax() / (1024 * 1024) });
		} catch (Exception ex) {
			logger.warn("Stats generation error", ex);
		}
	}

	private void checkDeadlocks(ThreadMXBean threads) {
		{
			long[] ids = threads.findDeadlockedThreads();
			if (ids != null)
				for (long id : ids) {
					logger.info("Thread com dead lock? {}", threads
							.getThreadInfo(id).getThreadName());
					logTrace(threads.getThreadInfo(id).getStackTrace());
				}
		}
		{
			long[] ids = threads.findMonitorDeadlockedThreads();
			if (ids != null)
				for (long id : ids) {
					logger.info("Thread com synchronized lock? {}", threads
							.getThreadInfo(id).getThreadName());
					logTrace(threads.getThreadInfo(id).getStackTrace());
				}
		}

	}

	private void logTrace(StackTraceElement[] stackTrace) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement e : stackTrace) {
			sb.append(e.getClassName() + e.getMethodName() + ":"
					+ e.getLineNumber() + "\n");
		}
		logger.info(sb.toString());
	}

}

%>
<% new BasicMonitor().printThreads(out);
new BasicMonitor().logStats(); %>