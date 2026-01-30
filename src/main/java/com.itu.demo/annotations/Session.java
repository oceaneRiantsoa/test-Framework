package com.itu.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sprint 11 : Annotation pour injecter la session HTTP dans un paramètre de contrôleur.
 * Le paramètre doit être de type Map<String, Object>.
 * Les modifications apportées à la Map sont synchronisées avec la vraie session HTTP.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Session {
}
