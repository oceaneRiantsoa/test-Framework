package com.itu.demo;

public class Mapping {
    private String className;
    private String method;
    private String urlPattern;

    public Mapping(String className, String method, String urlPattern) {
        this.className = className;
        this.method = method;
        this.urlPattern = urlPattern;
    }

    public String getClassName() { return className; }
    public String getMethod() { return method; }
    public String getUrlPattern() { return urlPattern; }
}