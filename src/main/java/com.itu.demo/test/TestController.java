package com.itu.demo.test;

import com.itu.demo.Url;
import com.itu.demo.ModelView;

public class TestController {
   @Url("/user")
public ModelView user(String username) {
    ModelView mv = new ModelView("User.jsp");
    mv.addItem("username", username);
    mv.addItem("role", "admin");
    return mv;
}
}