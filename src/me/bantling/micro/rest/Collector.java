package me.bantling.micro.rest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ServiceLoader;

import com.sun.net.httpserver.HttpExchange;

public class Collector {
    /**
     * Expected parameters for an endpoint method
     */
    static final Class<?>[] ENDPOINT_PARAMETERS = {HttpExchange.class};
    
    /**
     * Error to complain that an {@link Endpoint} method has the wrong signature
     */
    static final String WRONG_ENDPOINT_SIGNATURE = "Wrong @EndPoint signature for %s %s at %s.%s, must return void and accept only a com.sun.net.httpserver.HttpExchange";
    
    /**
     * Error to complain about multiple {@link Endpoint}s with the same method and url
     */
    static final String DUPLICATE_ENDPOINT_URL = "Duplicate @EndPoint %s %s at %s.%s";
    
    /**
     * Collect all the services for this Server via {@link ServiceLoader} on interfaces of type {@link Service}.
     * Only one instance of Server should call this method, as it will always collect the same services every time.
     */
    protected void collectServices() {
        ServiceLoader.
            load(Service.class).
            forEach(rh -> {
                for (final Method m : rh.getClass().getMethods()) {
                    final Endpoint endPoint = m.getAnnotation(Endpoint.class);                    
                    if (endPoint != null) {
                        // Have an EndPoint annotation, verify signature
                        if (
                            (m.getReturnType() != void.class) ||
                            (! Arrays.equals(ENDPOINT_PARAMETERS, m.getParameterTypes()))
                        ) {
                            throw new RuntimeException(String.format(
                                WRONG_ENDPOINT_SIGNATURE,
                                endPoint.method(),
                                endPoint.url(),
                                rh.getClass().getName(),
                                m
                            ));
                        }
                        
                        // Verify not a duplicate method and url
//                        services.merge(
//                            endPoint,
//                            ex -> {
//                                Try.to(() -> {
//                                    m.invoke(rh, ex);
//                                });
//                            },
//                            ($, $$) -> {
//                                throw new RuntimeException(String.format(
//                                    DUPLICATE_ENDPOINT_URL,
//                                    endPoint.method(),
//                                    endPoint.url(),
//                                    rh.getClass().getName(),
//                                    m
//                                ));
//                            }
//                        );
                    }
                }
            });
    }
}
