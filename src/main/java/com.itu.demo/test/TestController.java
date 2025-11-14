package com.itu.demo.test;

import com.itu.demo.Url;

public class TestController {
    @Url("/hello")
    public String sayHello() {
        return "Hello from test method!";
    }

    @Url("/test")
    public void testMethod() {
        System.out.println("Test method called");
    }
}