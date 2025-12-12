package com.itu.demo;

import com.itu.demo.annotations.RequestParam;
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
                Class.forName("com.itu.demo.test.TestController2"),
                Class.forName("com.itu.demo.test.DeptController"),
                Class.forName("com.itu.demo.test.EtudiantController"),
                Class.forName("com.itu.demo.test.FormController"),
                Class.forName("com.itu.demo.test.TestFormController")  // Sprint 8
            };
            for (Class<?> ctrlClass : controllers) {
                for (Method method : ctrlClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(com.itu.demo.Url.class)) {
                        com.itu.demo.Url urlAnnotation = method.getAnnotation(com.itu.demo.Url.class);
                        String urlPath = urlAnnotation.value();
                        mappingUrls.put(urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                    }
                    if (method.isAnnotationPresent(com.itu.demo.annotations.GetMapping.class)) {
                        com.itu.demo.annotations.GetMapping getAnn = method.getAnnotation(com.itu.demo.annotations.GetMapping.class);
                        String urlPath = getAnn.value();
                        mappingUrls.put(urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                    }
                    if (method.isAnnotationPresent(com.itu.demo.annotations.PostMapping.class)) {
                        com.itu.demo.annotations.PostMapping postAnn = method.getAnnotation(com.itu.demo.annotations.PostMapping.class);
                        String urlPath = postAnn.value();
                        mappingUrls.put(urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
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
        String httpMethod = request.getMethod();

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

        PrintWriter out = response.getWriter();
        Mapping mapping = null;
        String matchedPattern = null;

        // Sprint 6-ter & 7 : recherche du mapping avec variable dans l'URL et méthode HTTP
        for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            String pattern = entry.getKey();
            Mapping map = entry.getValue();
            Method method = getMethodFromMapping(map);
            if (method != null) {
                boolean match = matchUrl(pattern, url);
                boolean isGet = httpMethod.equalsIgnoreCase("GET") && (
                        method.isAnnotationPresent(com.itu.demo.annotations.GetMapping.class)
                        || method.isAnnotationPresent(com.itu.demo.Url.class));
                boolean isPost = httpMethod.equalsIgnoreCase("POST") && (
                        method.isAnnotationPresent(com.itu.demo.annotations.PostMapping.class)
                        || method.isAnnotationPresent(com.itu.demo.Url.class));
                if (match && (isGet || isPost)) {
                    mapping = map;
                    matchedPattern = pattern;
                    break;
                }
            }
        }

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
                    Class<?>[] paramTypes = method.getParameterTypes();
                    java.lang.reflect.Parameter[] parameters = method.getParameters();
                    Object[] paramValues = new Object[paramTypes.length];

                    // Sprint 6-ter : extraction des variables de l'URL
                    Map<String, String> pathVars = extractPathVariables(matchedPattern, url);

                    for (int i = 0; i < parameters.length; i++) {
                        // Sprint 8 : si le paramètre est de type Map, injecter les paramètres de la requête
                        if (paramTypes[i] == Map.class || paramTypes[i] == java.util.Map.class) {
                            Map<String, Object> formData = new HashMap<>();
                            java.util.Map<String, String[]> paramMap = request.getParameterMap();
                            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                                String key = entry.getKey();
                                String[] values = entry.getValue();
                                formData.put(key, values.length == 1 ? values[0] : values);
                            }
                            paramValues[i] = formData;
                        } else {
                            // Sprint 6 & 6-bis : injection normale des paramètres
                            RequestParam reqParam = parameters[i].getAnnotation(RequestParam.class);
                            String paramKey = (reqParam != null) ? reqParam.value() : parameters[i].getName();
                            String value = pathVars.get(paramKey);
                            if (value == null) value = request.getParameter(paramKey);
                            if (value == null) paramValues[i] = null;
                            else if (paramTypes[i] == int.class || paramTypes[i] == Integer.class) {
                                paramValues[i] = Integer.parseInt(value);
                            } else {
                                paramValues[i] = value;
                            }
                        }
                    }

                    Object result = method.invoke(instance, paramValues);

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

    // Méthode utilitaire pour matcher les patterns d'URL avec variables
    private boolean matchUrl(String pattern, String url) {
        String[] patParts = pattern.split("/");
        String[] urlParts = url.split("/");
        if (patParts.length != urlParts.length) return false;
        for (int i = 0; i < patParts.length; i++) {
            if (patParts[i].startsWith("{") && patParts[i].endsWith("}")) continue;
            if (!patParts[i].equals(urlParts[i])) return false;
        }
        return true;
    }

    // Méthode utilitaire pour extraire les variables de l'URL
    private Map<String, String> extractPathVariables(String pattern, String url) {
        Map<String, String> vars = new HashMap<>();
        String[] patParts = pattern.split("/");
        String[] urlParts = url.split("/");
        for (int i = 0; i < patParts.length; i++) {
            if (patParts[i].startsWith("{") && patParts[i].endsWith("}") && i < urlParts.length) {
                String varName = patParts[i].substring(1, patParts[i].length() - 1);
                vars.put(varName, urlParts[i]);
            }
        }
        return vars;
    }

    // Méthode utilitaire pour retrouver la méthode à partir du Mapping
    private Method getMethodFromMapping(Mapping mapping) {
        try {
            Class<?> clazz = Class.forName(mapping.getClassName());
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.getName().equals(mapping.getMethod())) {
                    return m;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
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