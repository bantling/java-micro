package me.bantling.micro.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A url prefix for all {@link Endpoint} annotated methods of a {@link Service} implementation. 
 * The prefix may not define any variables, only fixed parts.
 * 
 * EG, an interface whose endpoints are all related to customer can have the EndpointPrefix set to "/customer".
 * Then all Endpoint annotatated methods of the interface only need specify additional path parts, if any.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EndpointPrefix {
    String url();
}
