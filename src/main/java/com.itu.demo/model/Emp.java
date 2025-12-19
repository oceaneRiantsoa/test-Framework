package com.itu.demo.model;

public class Emp {
    private String name;
    private String dept;
    private int age;

    // Constructeur sans argument (obligatoire)
    public Emp() {}

    // Constructeur avec 3 arguments (pour le Sprint 9)
    public Emp(String name, String dept, int age) {
        this.name = name;
        this.dept = dept;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Emp{name='" + name + "', dept='" + dept + "', age=" + age + '}';
    }
}