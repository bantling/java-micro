package me.bantling.micro.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A REST method for an endpoint, that has an {@link HttpMethod} and a url.
 * The url may define any number of variables, where a variable is a type contained in curly braces.
 * The allowable types are:
 * - boolean
 * - string
 * - int
 * - long
 * - uuid
 * 
 * EG, the url /customer/{uuid} indicates a fixed part customer followed by a variable part that must be a valid uuid.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Endpoint {
    String method();
    String url() default "";
}
