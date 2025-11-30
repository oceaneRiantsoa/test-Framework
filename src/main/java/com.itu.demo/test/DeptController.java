package com.itu.demo.test;

import com.itu.demo.Url;
import com.itu.demo.annotations.RequestParam;

public class DeptController {
    @Url("/dep/save")
    public String save(@RequestParam("nom") String nom, @RequestParam("code") String code) {
        return "Département enregistré : " + nom + " (" + code + ")";
    }
}