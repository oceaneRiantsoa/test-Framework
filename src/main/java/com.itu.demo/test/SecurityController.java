package com.itu.demo.test;

import com.itu.demo.ModelView;
import com.itu.demo.annotations.*;
import java.util.Map;

/**
 * Sprint 11bis : Contrôleur de démonstration pour la sécurité basée sur les rôles
 */
@Controller
public class SecurityController {
    
    /**
     * Page publique - accessible à tous (aucune annotation)
     */
    @GetMapping("/public")
    public ModelView publicPage() {
        ModelView mv = new ModelView("public.jsp");
        mv.addItem("message", "Cette page est accessible à tous");
        mv.addItem("requireAuth", false);
        return mv;
    }
    
    /**
     * Page privée - nécessite une authentification
     */
    @AuthRequired
    @GetMapping("/private")
    public ModelView privatePage(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("private.jsp");
        mv.addItem("message", "Cette page nécessite une authentification");
        mv.addItem("username", session.get("username"));
        mv.addItem("role", session.get("role"));
        return mv;
    }
    
    /**
     * Page admin - nécessite le rôle "admin"
     */
    @Role("admin")
    @GetMapping("/admin/dashboard")
    public ModelView adminDashboard(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("admin.jsp");
        mv.addItem("message", "Tableau de bord administrateur");
        mv.addItem("username", session.get("username"));
        mv.addItem("stats", "15 utilisateurs, 42 connexions aujourd'hui");
        return mv;
    }
    
    /**
     * Page chef - nécessite le rôle "chef"
     */
    @Role("chef")
    @GetMapping("/chef/reports")
    public ModelView chefReports(@Session Map<String, Object> session) {
        ModelView mv = new ModelView("chef.jsp");
        mv.addItem("message", "Rapports pour les chefs d'équipe");
        mv.addItem("username", session.get("username"));
        mv.addItem("reports", new String[]{"Rapport mensuel", "Analyse performance", "Budget équipe"});
        return mv;
    }
    
    /**
     * Permet de changer de rôle pour tester (simulation)
     */
    @AuthRequired
    @PostMapping("/switch-role")
    public ModelView switchRole(@Session Map<String, Object> session, Map<String, Object> formData) {
        String newRole = (String) formData.get("role");
        if (newRole != null && (newRole.equals("user") || newRole.equals("admin") || newRole.equals("chef"))) {
            session.put("role", newRole);
        }
        
        ModelView mv = new ModelView("roleChanged.jsp");
        mv.addItem("username", session.get("username"));
        mv.addItem("newRole", session.get("role"));
        return mv;
    }
}