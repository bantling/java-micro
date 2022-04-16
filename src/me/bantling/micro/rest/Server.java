package me.bantling.micro.rest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.ServiceLoader;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A simple REST server that maps GET PUT DELETE POST.
 * Any number of services can be running which are expressed as instances of {@link Service}.
 * The services are discovered via {@link ServiceLoader}.
 */
public class Server implements HttpHandler {
    /**
     * Mapping of path parts to handlers.
     * 
     * Example Service:
     * - GET /customer?lastName:string+&firstName:string
     *   list all customers by a required lastName (string+) and optional firstName (string) 
     * - GET /customer/{uuid}
     *   list one customer by id
     * - PUT /customer
     *   create/update one or more customers
     * - DELETE /customer/{uuid}
     *   delete one customer by id
     * 
     * services structure to map it:
     * "GET" -> "/customer" -> PathPart
     *   variablePart: queryParams: {
     *     lastName  -> {type = String, required = true},
     *     firstName -> {type = String, required = false}
     *   }
     *   handler: list all matching customers
     *   nextParts: PathPart:/{uuid} -> PathPart
     *     handler: list one customer
     *     nextPart: empty
     * PUT -> /customer -> PathPart
     *   handler: create/update cusyomers
     *   nextPart: empty
     * DELETE -> /customer -> PathPart
     *   handler: empty
     *   nextPart: PathPart:/{uuid} -> PathPart
     *     handler: delete customer
     *     nextPart: empty
     */
    protected Map<String, Map<String, PathElement>> services;
    
    // ==== Error messages
    
    /**
     * Error when an endpoint is valid, but the method is not
     */
    protected static final String HTTP_METHOD_NOT_ALLOWED_RESPONSE = HttpURLConnection.HTTP_BAD_METHOD + " Method Not Allowed";
    
    // ==== Construct
    
    public Server(final Map<String, Map<String, PathElement>> services) {
        this.services = services;
    }
    
    // ==== HttpHandler
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (
            final Writer w = new OutputStreamWriter(exchange.getResponseBody())
        ) {
            exchange.sendResponseHeaders(
                HttpURLConnection.HTTP_BAD_METHOD,
                HTTP_METHOD_NOT_ALLOWED_RESPONSE.length()
            );
            w.append(HTTP_METHOD_NOT_ALLOWED_RESPONSE);
        }
    }
}
