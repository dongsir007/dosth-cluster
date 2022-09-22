package com.szbsc.config;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@WebFilter(filterName = "GzipFilter", urlPatterns = "/*")
public class GzipFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String contentEncoding = request.getHeader("Content-Encoding");
		if (null != contentEncoding && "".equals(contentEncoding)) {
			request = new GzipRequestWrapper(request);
		}
		filterChain.doFilter(request, servletResponse);
	}

	class GzipRequestWrapper extends HttpServletRequestWrapper {

		private HttpServletRequest request;
		
		public GzipRequestWrapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			ServletInputStream stream = request.getInputStream();
			final GZIPInputStream gzipInputStream = new GZIPInputStream(stream);
			ServletInputStream newStream = new ServletInputStream() {
				@Override
				public int read() throws IOException {
					return gzipInputStream.read();
				}
				
				@Override
				public void setReadListener(ReadListener listener) {
				}
				
				@Override
				public boolean isReady() {
					return false;
				}
				
				@Override
				public boolean isFinished() {
					return false;
				}
			};
			return newStream;
		}
	}
}