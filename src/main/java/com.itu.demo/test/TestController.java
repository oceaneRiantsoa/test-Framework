package com.itu.demo.test;

import com.itu.demo.Url;
import com.itu.demo.ModelView;

public class TestController {
    @Url("/user")
    public ModelView user() {
        ModelView mv = new ModelView("User.jsp");
        mv.addItem("username", "Oceane");
        mv.addItem("role", "admin");
        return mv;
    }
}