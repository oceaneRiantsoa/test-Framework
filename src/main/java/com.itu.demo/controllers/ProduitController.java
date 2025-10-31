package com.itu.demo.controllers;

import com.itu.demo.annotations.Controller;

@Controller("/produits")
public class ProduitController {
    public void index() {
        System.out.println("Liste des produits");
    }

    public void details() {
        System.out.println("Détails du produit");
    }
}