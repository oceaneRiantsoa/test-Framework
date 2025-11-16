package com.itu.demo.test;

import com.itu.demo.Url;
import com.itu.demo.ModelView;

public class TestController2 {
    @Url("/userJsp")
    public ModelView goToUserJsp() {
        return new ModelView("User.jsp");
    }
}