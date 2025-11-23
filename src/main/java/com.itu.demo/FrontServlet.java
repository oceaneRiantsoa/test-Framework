package com.itu.demo;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> mappingUrls = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class<?>[] controllers = {
                Class.forName("com.itu.demo.test.TestController"),
                Class.forName("com.itu.demo.test.TestController2")
            };
            for (Class<?> ctrlClass : controllers) {
                for (Method method : ctrlClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Url.class)) {
                        Url urlAnnotation = method.getAnnotation(Url.class);
                        String urlPath = urlAnnotation.value();
                        mappingUrls.put(urlPath, new Mapping(ctrlClass.getName(), method.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = uri.substring(contextPath.length());

        // Sprint 1 & 1bis : fichiers statiques
        if (url.contains(".")) {
            InputStream is = getServletContext().getResourceAsStream(url);
            if (is != null) {
                String mimeType = getServletContext().getMimeType(url);
                response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                return;
            }
        }

        // Sprint 3, 4, 4-bis, 5 : mapping, réflexion, ModelView, données vers la vue
        PrintWriter out = response.getWriter();
        Mapping mapping = mappingUrls.get(url);
        if (mapping != null) {
            try {
                Class<?> clazz = Class.forName(mapping.getClassName());
                Object instance = clazz.getDeclaredConstructor().newInstance();
                Method method = null;
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.getName().equals(mapping.getMethod())) {
                        method = m;
                        break;
                    }
                }
                if (method != null) {
                    Object result = method.invoke(instance);
                    // Sprint 5 : dispatcher si retour ModelView et envoyer les attributs
                    if (result instanceof ModelView) {
                        ModelView mv = (ModelView) result;
                        for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
                        }
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/" + mv.getView());
                        dispatcher.forward(request, response);
                        return;
                    }
                    // Sprint 4 : afficher le String si retour String
                    if (result instanceof String) {
                        out.println(result);
                        return;
                    }
                }
                // Affichage classique
                out.println("<html><body>");
                out.println("Nom : " + mapping.getMethod() + "<br>");
                out.println("</body></html>");
            } catch (Exception e) {
                out.println("<pre>" + e + "</pre>");
            }
        } else {
            out.println("<html><body>");
            out.println("Aucune méthode associée à cette URL");
            out.println("</body></html>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}