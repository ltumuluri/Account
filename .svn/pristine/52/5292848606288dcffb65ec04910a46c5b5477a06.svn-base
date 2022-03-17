package org.uftwf.account.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xyang on 2/26/19.
 */
public class CorsFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorsFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("doFilter(): add the cross domain filter to application" + "\r\n");
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String[] allowDomain={"*"};
        String originHeader=request.getHeader("origin");
        if(originHeader!=null) {
            for (String domain : allowDomain) {
                if (originHeader.contains(domain)) {
                    response.setHeader("Access-Control-Allow-Origin", originHeader);
                    break;
                }
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
