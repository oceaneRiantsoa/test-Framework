package com.itu.demo.test;

import com.itu.demo.annotations.GetMapping;
import com.itu.demo.annotations.PostMapping;
import com.itu.demo.ModelView;

public class FormController {
    @GetMapping("/form")
    public ModelView showForm() {
        return new ModelView("form.jsp");
    }

    @PostMapping("/form/save")
    public ModelView saveForm(String nom, String email) {
        ModelView mv = new ModelView("form.jsp");
        mv.addItem("nom", nom);
        mv.addItem("email", email);
        mv.addItem("message", "Formulaire enregistré !");
        return mv;
    }
}