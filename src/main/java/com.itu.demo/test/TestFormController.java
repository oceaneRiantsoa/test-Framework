package com.itu.demo.test;

import com.itu.demo.annotations.GetMapping;
import com.itu.demo.annotations.PostMapping;
import com.itu.demo.ModelView;
import java.util.Map;

public class TestFormController {
    @GetMapping("/test/form")
    public ModelView showTestForm() {
        return new ModelView("testForm.jsp");
    }

    @PostMapping("/test/form")
    public ModelView submitForm(Map<String, Object> form) {
        String nom = (String) form.get("nom");
        String ageStr = (String) form.get("age");
        int age = 0;
        if (ageStr != null && !ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        ModelView mv = new ModelView("testResult.jsp");
        mv.addItem("nom", nom);
        mv.addItem("age", age);
        mv.addItem("message", "Données reçues avec succès !");
        return mv;
    }
}