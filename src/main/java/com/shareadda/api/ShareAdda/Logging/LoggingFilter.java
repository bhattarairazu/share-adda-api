package com.shareadda.api.ShareAdda.Logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    long time = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("userID", httpServletRequest.getRemoteUser());
        chain.doFilter(request, response);
        time = System.currentTimeMillis() - time;
        logger.info("API Duration {}:{} ms",httpServletRequest.getRequestURI(),time);
    }

    @Override
    public void destroy() {

    }
}
