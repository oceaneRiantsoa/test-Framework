package com.itu.demo.test;

import com.itu.demo.ModelView;
import com.itu.demo.annotations.Controller;
import com.itu.demo.annotations.GetMapping;
import com.itu.demo.annotations.PostMapping;
import com.itu.demo.annotations.Session;
import java.util.Map;

/**
 * Sprint 11 : Contrôleur de démonstration pour la gestion de session
 */
@Controller
public class SessionController {
    
    /**
     * Affiche le formulaire de login
     */
    @GetMapping("/login")
    public ModelView showLoginForm(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("login.jsp");
        
        // Vérifier si l'utilisateur est déjà connecté
        if (session.containsKey("username")) {
            mv.addItem("message", "Vous êtes déjà connecté en tant que: " + session.get("username"));
            mv.addItem("isLoggedIn", true);
        } else {
            mv.addItem("isLoggedIn", false);
        }
        
        return mv;
    }
    
    /**
     * Traite la connexion et stocke l'utilisateur en session
     */
    @PostMapping("/login")
    public ModelView doLogin(@Session Map<String, Object> session, Map<String, Object> formData) {
        ModelView mv = new ModelView("loginResult.jsp");
        
        String username = (String) formData.get("username");
        String password = (String) formData.get("password");
        
        // Simulation de vérification (en vrai, vérifier en base)
        if (username != null && !username.isEmpty() && password != null && password.equals("1234")) {
            // Attribuer un rôle selon l'utilisateur pour les tests
            String role = "user"; // rôle par défaut
            if (username.equalsIgnoreCase("admin")) {
                role = "admin";
            } else if (username.equalsIgnoreCase("chef")) {
                role = "chef";
            }
            
            // Stocker dans la session
            session.put("username", username);
            session.put("role", role);
            session.put("loginTime", System.currentTimeMillis());
            
            mv.addItem("success", true);
            mv.addItem("message", "Bienvenue " + username + " !");
        } else {
            mv.addItem("success", false);
            mv.addItem("message", "Identifiants incorrects");
        }
        
        return mv;
    }
    
    /**
     * Affiche le profil de l'utilisateur connecté
     */
    @GetMapping("/profil")
    public ModelView profil(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("profil.jsp");
        
        String username = (String) session.get("username");
        if (username != null) {
            mv.addItem("username", username);
            mv.addItem("role", session.get("role"));
            mv.addItem("loginTime", session.get("loginTime"));
            mv.addItem("isLoggedIn", true);
        } else {
            mv.addItem("isLoggedIn", false);
            mv.addItem("message", "Vous devez vous connecter");
        }
        
        return mv;
    }
    
    /**
     * Déconnexion : supprime les données de session
     */
    @GetMapping("/logout")
    public ModelView logout(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("logout.jsp");
        
        String username = (String) session.get("username");
        
        // Supprimer les données de session
        session.remove("username");
        session.remove("role");
        session.remove("loginTime");
        
        mv.addItem("message", username != null ? "Au revoir " + username + " !" : "Vous n'étiez pas connecté");
        
        return mv;
    }
}
