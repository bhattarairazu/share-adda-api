package com.shareadda.api.ShareAdda.cors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.addHeader("Access-Control-Allow-Origin","*");
        httpResponse.addHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, PATCH, OPTIONS");
        httpResponse.addHeader("Access-Control-Allow-Headers","Content-Type, X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,Cache-Control");
        httpResponse.addHeader("Access-Control-Max-Age","3600");
        httpResponse.addHeader("Access-Control-Expose-Headers","Content-Disposition");
        httpResponse.addHeader("Access-Control-Allow-Credentials","true");

        if(httpRequest.getMethod().equals("OPTIONS")){
            httpResponse.setStatus(200);
            httpResponse.getWriter().print("OK");
            httpResponse.getWriter().flush();
        }else{
            chain.doFilter(request,httpResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
