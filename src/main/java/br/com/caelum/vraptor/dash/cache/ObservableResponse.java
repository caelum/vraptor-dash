package br.com.caelum.vraptor.dash.cache;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@Vetoed
public class ObservableResponse extends HttpServletResponseWrapper {

	private MessageDigest digester;

	public ObservableResponse(HttpServletResponse res) {
		super(res);
		try {
			this.digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private int status = 0;
	private PrintWriter writer;
	private ResponseServletOutputStream oStream;
	private String cacheControl = "";
	private String etag = "";

	public ServletOutputStream getOutputStream() throws IOException {
		if (this.oStream == null) {
			this.oStream = new ResponseServletOutputStream(
					super.getOutputStream());
		}
		return this.oStream;
	}

	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
			this.writer = new ResponsePrintWriter(super.getWriter());
		}
		return this.writer;
	}

	@Override
	public void resetBuffer() {
		try {
			this.digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reset() {
		super.reset();
		resetBuffer();
	}

	public String getMd5() {
		// GAMBI WIN
		String c = "";
		for (byte b : this.digester.digest()) {
			c += (int) b;
		}
		return c;
	}

	private int size;

	private class ResponseServletOutputStream extends ServletOutputStream {

		private final ServletOutputStream original;

		public ResponseServletOutputStream(ServletOutputStream outputStream) {
			this.original = outputStream;
		}

		@Override
		public void write(int b) throws IOException {
			original.write(b);
			digester.update((byte) b);
			size++;
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			original.write(b, off, len);
			digester.update(b, off, len);
			size += len;
		}

	}

	private class ResponsePrintWriter extends PrintWriter {

		private ResponsePrintWriter(PrintWriter printWriter) {
			super(printWriter);
		}

		@Override
		public void write(char buf[], int off, int len) {
			super.write(buf, off, len);
			super.flush();
			size += len;
			digester.update(new String(buf).getBytes());
		}

		@Override
		public void write(String s, int off, int len) {
			super.write(s, off, len);
			super.flush();
			size += len;
			digester.update(s.getBytes());
		}

		@Override
		public void write(int c) {
			size++;
			digester.update((byte) c);
			super.write(c);
			super.flush();
		}

	}

	public long size() {
		return size;
	}

	@Override
	public void addHeader(String name, String value) {
		if (name.toLowerCase().equals("cache-control")) {
			this.cacheControl += ";" + value;
		} else if (name.toLowerCase().equals("etag")) {
			this.etag += ";" + value;
		}
		super.addHeader(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		if (name.toLowerCase().equals("cache-control")) {
			this.cacheControl = value;
		} else if (name.toLowerCase().equals("etag")) {
			this.etag = value;
		}
		super.setHeader(name, value);
	}

	public String getEtagHeader() {
		return etag;
	}

	public String getCacheControlHeader() {
		return cacheControl;
	}

	public int getGivenStatus() {
		return status;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		this.status = sc;
		super.sendError(sc, msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		this.status = sc;
		super.sendError(sc);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		this.status = 302;
		super.sendRedirect(location);
	}

	@Override
	public void setStatus(int sc, String sm) {
		this.status = sc;
		super.setStatus(sc, sm);
	}

	@Override
	public void setStatus(int sc) {
		this.status = sc;
		super.setStatus(sc);
	}

}