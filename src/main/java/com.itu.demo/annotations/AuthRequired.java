package com.itu.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sprint 11bis : Annotation pour marquer qu'une méthode nécessite une authentification.
 * L'utilisateur doit être connecté (avoir une session avec username) pour accéder à la méthode.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
}