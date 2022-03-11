package me.bantling.micro.rest;

/**
 * An HTTP method to respond to
 */
public final class HttpMethod {
    private HttpMethod() {
        throw new RuntimeException("no instances");
    }
    
    public static final String GET    = "GET";
    public static final String PUT    = "PUT";
    public static final String DELETE = "DELETE";
    public static final String POST   = "POST";
}
