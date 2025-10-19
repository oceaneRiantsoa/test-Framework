package com.itu.demo;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class ResourceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // pas d'init spécifique
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI();
            String context = req.getContextPath();
            String rel = "/";
            if (uri != null && context != null && uri.length() >= context.length()) {
                rel = uri.substring(context.length());
                if (rel.isEmpty()) rel = "/";
            }

            boolean isResource = rel.matches(".*\\.(css|js|png|jpg|jpeg|gif|ico|svg|html|htm)$");
            request.setAttribute("isResource", Boolean.valueOf(isResource));
            // fournir le chemin relatif attendu par FrontServlet (commençant par /)
            String resourcePath = rel.startsWith("/") ? rel : ("/" + rel);
            request.setAttribute("resourcePath", resourcePath);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // cleanup si besoin
    }
}