package com.itu.demo;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class FrontServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "hi!";
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String context = request.getContextPath();
        String rel = uri.substring(Math.min(context.length(), uri.length()));
        if (rel.isEmpty()) rel = "/";

        // priorité à l'attribut posé par le Filter, sinon détection par extension
        boolean isResource = false;
        Object attr = request.getAttribute("isResource");
        if (attr instanceof Boolean) {
            isResource = (Boolean) attr;
        } else {
            isResource = rel.matches(".*\\.(css|js|png|jpg|jpeg|gif|ico|svg|html|htm)$");
        }

        if (isResource) {
            String resourcePath = (String) request.getAttribute("resourcePath");
            if (resourcePath == null || resourcePath.isEmpty()) {
                resourcePath = rel;
            }
            if (!resourcePath.startsWith("/")) resourcePath = "/" + resourcePath;

            InputStream in = getServletContext().getResourceAsStream(resourcePath);
            // fallback: if not found at root, try under /WEB-INF (files kept there are protected from direct URL access)
            if (in == null) {
                in = getServletContext().getResourceAsStream("/WEB-INF" + resourcePath);
            }
            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String mime = getServletContext().getMimeType(resourcePath);
            if (mime == null) mime = "application/octet-stream";
            response.setContentType(mime);
            response.setStatus(HttpServletResponse.SC_OK);

            try (InputStream in2 = in; OutputStream out = response.getOutputStream()) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = in2.read(buf)) != -1) {
                    out.write(buf, 0, r);
                }
            }
            return;
        }

        // comportement dynamique par défaut
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void destroy() {
    }
}