package com.itu.demo.test;

import com.itu.demo.Url;

public class DeptController {
    @Url("/dep/save")
    public String save(String nom, String code) {
        return "Département enregistré : " + nom + " (" + code + ")";
    }
}