package com.itu.demo.test;

import com.itu.demo.Url; 
import java.lang.reflect.Method; 
public class TestController {

     public static void main(String[] args) {
        System.out.println("=== Test des méthodes annotées ===");
        try {
            for (Method method : TestController.class.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Url.class)) {
                    Url url = method.getAnnotation(Url.class);
                    System.out.println("URL: " + url.value());
                    System.out.println("Méthode: " + method.getName());
                    System.out.println("---");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Url("/hello")
    public void sayHello() {
        System.out.println("Hello from test method!");
    }

    @Url("/test")
    public void testMethod() {
        System.out.println("Test method called");
    }
}