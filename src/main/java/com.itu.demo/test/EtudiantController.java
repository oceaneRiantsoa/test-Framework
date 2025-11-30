package com.itu.demo.test;

import com.itu.demo.Url;

public class EtudiantController {
    @Url("/etud/{id}")
    public String fiche(int id) {
        return "Fiche étudiant : " + id;
    }
}