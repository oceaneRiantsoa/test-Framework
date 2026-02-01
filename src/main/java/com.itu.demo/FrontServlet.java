package com.itu.demo;

import com.itu.demo.annotations.RequestParam;
import com.itu.demo.annotations.RestApi;
import com.itu.demo.annotations.Session;
import com.itu.demo.annotations.AuthRequired;
import com.itu.demo.annotations.Role;
import com.itu.demo.model.ApiResponse;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.MultipartConfig;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.google.gson.Gson;

@MultipartConfig
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
                Class.forName("com.itu.demo.test.TestFormController"),
                Class.forName("com.itu.demo.test.EmpController"),
                Class.forName("com.itu.demo.test.ApiTestController"),
                Class.forName("com.itu.demo.test.FileUploadController"),
                Class.forName("com.itu.demo.test.SessionController"),
                Class.forName("com.itu.demo.test.SecurityController")
            };
            for (Class<?> ctrlClass : controllers) {
                System.out.println("[FrontServlet] Scanning controller: " + ctrlClass.getName());
                for (Method method : ctrlClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(com.itu.demo.Url.class)) {
                        com.itu.demo.Url urlAnnotation = method.getAnnotation(com.itu.demo.Url.class);
                        String urlPath = urlAnnotation.value();
                        // @Url supporte GET et POST
                        mappingUrls.put("GET:" + urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                        mappingUrls.put("POST:" + urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                        System.out.println("[FrontServlet] Mapped @Url: " + urlPath + " -> " + method.getName());
                    }
                    if (method.isAnnotationPresent(com.itu.demo.annotations.GetMapping.class)) {
                        com.itu.demo.annotations.GetMapping getAnn = method.getAnnotation(com.itu.demo.annotations.GetMapping.class);
                        String urlPath = getAnn.value();
                        mappingUrls.put("GET:" + urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                        System.out.println("[FrontServlet] Mapped @GetMapping: GET:" + urlPath + " -> " + method.getName());
                    }
                    if (method.isAnnotationPresent(com.itu.demo.annotations.PostMapping.class)) {
                        com.itu.demo.annotations.PostMapping postAnn = method.getAnnotation(com.itu.demo.annotations.PostMapping.class);
                        String urlPath = postAnn.value();
                        mappingUrls.put("POST:" + urlPath, new Mapping(ctrlClass.getName(), method.getName(), urlPath));
                        System.out.println("[FrontServlet] Mapped @PostMapping: POST:" + urlPath + " -> " + method.getName());
                    }
                }
            }
            System.out.println("[FrontServlet] Total mappings: " + mappingUrls.size());
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

        // Sprint 6-ter & 7 : recherche du mapping avec clé composite (METHOD:URL)
        for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            String key = entry.getKey();
            Mapping map = entry.getValue();
            
            // Extraire la méthode HTTP et le pattern de la clé (ex: "GET:/login")
            String[] keyParts = key.split(":", 2);
            if (keyParts.length != 2) continue;
            
            String keyMethod = keyParts[0];
            String pattern = keyParts[1];
            
            // Vérifier si la méthode HTTP correspond et l'URL match
            if (httpMethod.equalsIgnoreCase(keyMethod) && matchUrl(pattern, url)) {
                mapping = map;
                matchedPattern = pattern;
                break;
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

                    // Sprint 10 : extraction des fichiers uploadés et des données du formulaire
                    Map<String, FileUpload> uploadedFiles = new HashMap<>();
                    Map<String, Object> formData = new HashMap<>();
                    
                    // Vérifier si la requête contient des fichiers (multipart)
                    String contentType = request.getContentType();
                    if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
                        for (Part part : request.getParts()) {
                            String fieldName = part.getName();
                            String fileName = part.getSubmittedFileName();
                            
                            if (fileName != null && !fileName.isEmpty()) {
                                // C'est un fichier
                                InputStream fileContent = part.getInputStream();
                                byte[] fileBytes = readAllBytes(fileContent);
                                uploadedFiles.put(fieldName, new FileUpload(fileName, fileBytes, part.getContentType()));
                            } else {
                                // C'est un champ normal
                                InputStream is = part.getInputStream();
                                byte[] valueBytes = readAllBytes(is);
                                String value = new String(valueBytes, "UTF-8");
                                formData.put(fieldName, value);
                            }
                        }
                    } else {
                        // Paramètres normaux (non-multipart)
                        java.util.Map<String, String[]> paramMap = request.getParameterMap();
                        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                            String key = entry.getKey();
                            String[] values = entry.getValue();
                            formData.put(key, values.length == 1 ? values[0] : values);
                        }
                    }

                    // Sprint 11 : préparer la copie de la session pour injection
                    Map<String, Object> sessionCopy = new HashMap<>();
                    HttpSession httpSession = request.getSession(true);
                    Enumeration<String> sessionNames = httpSession.getAttributeNames();
                    while (sessionNames.hasMoreElements()) {
                        String name = sessionNames.nextElement();
                        sessionCopy.put(name, httpSession.getAttribute(name));
                    }

                    // Sprint 11bis : vérification de sécurité avant exécution
                    if (!checkMethodSecurity(method, sessionCopy, response)) {
                        return; // Accès refusé, la réponse a été envoyée
                    }

                    for (int i = 0; i < parameters.length; i++) {
                        // Sprint 11 : si le paramètre est annoté @Session et de type Map
                        Session sessionAnnot = parameters[i].getAnnotation(Session.class);
                        if (sessionAnnot != null && (paramTypes[i] == Map.class || paramTypes[i] == java.util.Map.class)) {
                            paramValues[i] = sessionCopy;
                        }
                        // Sprint 10 & 8 : si le paramètre est de type Map, injecter soit les fichiers soit les données
                        else if (paramTypes[i] == Map.class || paramTypes[i] == java.util.Map.class) {
                            RequestParam reqParam = parameters[i].getAnnotation(RequestParam.class);
                            if (reqParam != null && reqParam.value().equals("files")) {
                                paramValues[i] = uploadedFiles;
                            } else {
                                paramValues[i] = formData;
                            }
                        } 
                        // Sprint 8bis : binding automatique d'objet métier
                        else if (!isPrimitiveOrWrapper(paramTypes[i]) && !paramTypes[i].equals(String.class)) {
                            Object obj = paramTypes[i].getDeclaredConstructor().newInstance();
                            String prefix = parameters[i].getName() + ".";
                            for (Map.Entry<String, Object> entry : formData.entrySet()) {
                                String key = entry.getKey();
                                if (key.startsWith(prefix)) {
                                    String fieldName = key.substring(prefix.length());
                                    String value = entry.getValue().toString();
                                    setFieldValue(obj, fieldName, value);
                                }
                            }
                            paramValues[i] = obj;
                        } 
                        else {
                            // Sprint 6 & 6-bis : injection normale des paramètres
                            RequestParam reqParam = parameters[i].getAnnotation(RequestParam.class);
                            String paramKey = (reqParam != null) ? reqParam.value() : parameters[i].getName();
                            String value = pathVars.get(paramKey);
                            if (value == null && formData.containsKey(paramKey)) {
                                value = formData.get(paramKey).toString();
                            }
                            if (value == null) value = request.getParameter(paramKey);
                            if (value == null) paramValues[i] = getDefaultValue(paramTypes[i]);
                            else if (paramTypes[i] == int.class || paramTypes[i] == Integer.class) {
                                paramValues[i] = Integer.parseInt(value);
                            } else if (paramTypes[i] == double.class || paramTypes[i] == Double.class) {
                                paramValues[i] = Double.parseDouble(value);
                            } else if (paramTypes[i] == boolean.class || paramTypes[i] == Boolean.class) {
                                paramValues[i] = Boolean.parseBoolean(value);
                            } else {
                                paramValues[i] = value;
                            }
                        }
                    }

                    Object result = method.invoke(instance, paramValues);

                    // Sprint 11 : synchroniser les modifications de la session
                    // Supprimer les attributs qui ne sont plus dans sessionCopy
                    Enumeration<String> currentSessionNames = httpSession.getAttributeNames();
                    while (currentSessionNames.hasMoreElements()) {
                        String name = currentSessionNames.nextElement();
                        if (!sessionCopy.containsKey(name)) {
                            httpSession.removeAttribute(name);
                        }
                    }
                    // Ajouter/Mettre à jour les attributs de sessionCopy
                    for (Map.Entry<String, Object> entry : sessionCopy.entrySet()) {
                        httpSession.setAttribute(entry.getKey(), entry.getValue());
                    }

                     // Sprint 9 : si la méthode est annotée @RestApi, retourner JSON
                    if (method.isAnnotationPresent(RestApi.class)) {
                        response.setContentType("application/json;charset=UTF-8");
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        
                        // Définir le status HTTP selon la réponse
                        if (result instanceof ApiResponse) {
                            ApiResponse apiResponse = (ApiResponse) result;
                            response.setStatus(apiResponse.getStatus());
                        } else {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                        
                        out.print(json);
                        return;
                    }
                        

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
                out.println("<pre>");
                e.printStackTrace(out);
                out.println("</pre>");
            }
        } else {
            out.println("<html><body>");
            out.println("<h2>Aucune méthode associée à cette URL</h2>");
            out.println("<p>URL demandée: " + url + "</p>");
            out.println("<p>Méthode HTTP: " + httpMethod + "</p>");
            out.println("<h3>URLs disponibles:</h3>");
            out.println("<ul>");
            for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
                out.println("<li>" + entry.getKey() + " -> " + entry.getValue().getMethod() + "</li>");
            }
            out.println("</ul>");
            out.println("</body></html>");
        }
    }

    // Sprint 8bis : méthode pour setter une propriété d'un objet via réflexion
    private void setFieldValue(Object obj, String fieldName, String value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        if (fieldType == int.class || fieldType == Integer.class) {
            field.set(obj, Integer.parseInt(value));
        } else if (fieldType == double.class || fieldType == Double.class) {
            field.set(obj, Double.parseDouble(value));
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            field.set(obj, Boolean.parseBoolean(value));
        } else {
            field.set(obj, value);
        }
    }

    // Vérifie si un type est primitif ou wrapper
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || 
               type == Integer.class || type == Double.class || type == Boolean.class ||
               type == Long.class || type == Float.class || type == Short.class ||
               type == Byte.class || type == Character.class;
    }

    // Retourne une valeur par défaut pour les types primitifs
    private Object getDefaultValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return 0;
        if (type == double.class || type == Double.class) return 0.0;
        if (type == boolean.class || type == Boolean.class) return false;
        return null;
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

    // Sprint 11bis : vérification de sécurité basée sur les annotations
    private boolean checkMethodSecurity(Method method, Map<String, Object> session, HttpServletResponse response) 
            throws IOException {
        
        // Vérifier @AuthRequired
        if (method.isAnnotationPresent(AuthRequired.class)) {
            String username = (String) session.get("username");
            if (username == null || username.isEmpty()) {
                // Non authentifié
                sendSecurityError(response, "Authentification requise", 
                    "Vous devez vous connecter pour accéder à cette ressource.", 401);
                return false;
            }
        }
        
        // Vérifier @Role
        if (method.isAnnotationPresent(Role.class)) {
            String username = (String) session.get("username");
            String userRole = (String) session.get("role");
            Role roleAnnotation = method.getAnnotation(Role.class);
            String requiredRole = roleAnnotation.value();
            
            if (username == null || username.isEmpty()) {
                // Non authentifié
                sendSecurityError(response, "Authentification requise", 
                    "Vous devez vous connecter pour accéder à cette ressource.", 401);
                return false;
            }
            
            if (userRole == null || !userRole.equals(requiredRole)) {
                // Rôle insuffisant
                sendSecurityError(response, "Accès interdit", 
                    "Vous n'avez pas les droits suffisants. Rôle requis: " + requiredRole, 403);
                return false;
            }
        }
        
        return true; // Accès autorisé
    }
    
    // Sprint 11bis : envoyer une page d'erreur de sécurité
    private void sendSecurityError(HttpServletResponse response, String title, String message, int status) 
            throws IOException {
        response.setStatus(status);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>" + title + "</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; max-width: 500px; margin: 100px auto; text-align: center; }");
        out.println("        .error-box { background: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; padding: 30px; border-radius: 8px; }");
        out.println("        h2 { color: #721c24; margin-bottom: 20px; }");
        out.println("        .links { margin-top: 20px; }");
        out.println("        .links a { color: #007bff; text-decoration: none; margin: 0 10px; padding: 10px 20px; background: #e9ecef; border-radius: 4px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='error-box'>");
        out.println("        <h2>🚫 " + title + "</h2>");
        out.println("        <p>" + message + "</p>");
        out.println("    </div>");
        out.println("    <div class='links'>");
        out.println("        <a href='/FirstServlet/login'>Se connecter</a>");
        out.println("        <a href='javascript:history.back()'>Retour</a>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    // Méthode utilitaire pour lire tous les bytes d'un InputStream (compatible Java 8)
    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
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