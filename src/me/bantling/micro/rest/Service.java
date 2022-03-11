package me.bantling.micro.rest;

/**
 * The empty interface {@link Service} is used as a marker for REST services.
 * The subclass of this interface must have at least one method annotated by {@link Endpoint},
 * and may optionally have an {@link @EndpointPrefix} annotation on the interface itself.
 * 
 * Each such annotated method must have the following signature:
 * 
 * void handle(pathVars..., queryParams..., HttpExchange exchange)
 * 
 * Where:
 * - pathVars is zero or more parameters of the correct type for variable path parts
 * - queryParams is zero or more parameters of the correct type for query params
 * 
 * Examples:
 * /customer/{uuid}                     : void handle(UUID, HttpExchange)
 * /customer?firstName=Bob              : void handle(String, HttpExchange)
 * /customer/{uuid}/address?type=string : void handle(UUID, String, HttpExchange) 
 */
public interface Service {
    //
}
