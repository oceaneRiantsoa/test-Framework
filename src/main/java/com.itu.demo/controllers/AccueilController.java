package com.itu.demo.controllers;

import com.itu.demo.annotations.Controller;

@Controller("/accueil")
public class AccueilController {
    public void index() {
        System.out.println("Page d'accueil - Bienvenue !");
    }

    public void about() {
        System.out.println("À propos de nous");
    }
}