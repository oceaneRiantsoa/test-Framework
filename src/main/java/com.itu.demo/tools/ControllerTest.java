package com.itu.demo.tools;

import com.itu.demo.annotations.Controller;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllerTest {
    public static void main(String[] args) throws Exception {
        // Chemin racine des classes compilées
        File classesRoot = new File(".");
        String basePackage = "com.itu.demo.controllers";
        File packageDir = new File(classesRoot, basePackage.replace('.', File.separatorChar));

        List<String> classNames = new ArrayList<>();
        collectClassNames(basePackage, packageDir, classNames);

        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Controller.class)) {
                testController(clazz);
            }
        }
    }

    private static void collectClassNames(String basePackage, File dir, List<String> classNames) {
        if (!dir.exists()) return;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                collectClassNames(basePackage + "." + file.getName(), file, classNames);
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private static void testController(Class<?> clazz) throws Exception {
        Controller ctrl = clazz.getAnnotation(Controller.class);
        System.out.println(clazz.getSimpleName() + " (" + ctrl.value() + ")");

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                System.out.print(method.getName() + "() -> ");
                method.invoke(instance);
            }
        }
    }
}