package com.itu.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sprint 11bis : Annotation pour spécifier le rôle requis pour accéder à une méthode.
 * L'utilisateur doit être connecté ET avoir le rôle spécifié en session.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    /**
     * Le nom du rôle requis (ex: "admin", "chef", "user")
     */
    String value();
}