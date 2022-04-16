package me.bantling.micro.rest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code EndpointBuilder} builds endpoints into a {@link Server} instance that serves those endpoints.
 * As endpoints are added, they are all merged together into a single structure.
 */
public class ServerBuilder {
    
    // ==== Fields

    // A variable part matcher for {name}
    static final Pattern VARIABLE_PATH_PART = Pattern.compile("[{]([A-Za-z]+)[}]");

    // A query params matcher for name:type[+]?, where optional + means param is required
    static final Pattern QUERY_PARAMS_PART = Pattern.compile("([A-Za-z0-9_]+):([A-Za-z]+)([+])?");
    
    // URL cannot be empty
    static final String URL_CANNOT_BE_EMPTY = "The url cannot be empty";
    
    // Query params cannot have multiple ?
    static final String QUERY_PARAMS_ONE_QUESTION_MARK = "The query parameters cannot have multiple ?";
    
    // Illegal query parameters
    static final String ILLEGAL_QUERY_MSG =
        "%s is not a valid query param, it must be of the form paramName:paramType optionally followed by a plus sign";
    
    private final Map<String, Map<String, PathElement>> services = new HashMap<>();
    
    // ==== Construct
    
    ServerBuilder() {
        //
    }
    
    // ==== Helpers
    
    /**
     * Parse a url into a list of {@link PathElement}s.
     * @param url
     * @return
     */
    static PathElement parseURL(final String url) {
        if (Objects.requireNonNull(url, "url").isEmpty()) {
            throw new IllegalArgumentException(URL_CANNOT_BE_EMPTY);
        }
        
        // - Remove leading /, if any
        // - Replace all occurences of multiple consecutive slashes with a single slash
        // - break up on remaining /, if any
        final String coalescedSlashes = url.replaceAll("/{2,}", "/");
        final String[] parts =
            (coalescedSlashes.charAt(0) == '/' ? coalescedSlashes.substring(1) : coalescedSlashes).
            split("/");

        
        // Track first path part, as we need to return it. Create variable part and map of next parts in it.
        final PathElement firstPathPart = new PathElement();
//        firstPathPart.variablePart = new VariablePart();
        firstPathPart.nextParts = new HashMap<>();
        
        // Track last part so we can check it for query params later 
        String lastPart = parts[0];
        // Track prev path part so we can link it to this path part via map
        PathElement prevPathPart = firstPathPart;
        // Track last path part so we can add any query params to it 
        PathElement lastPathPart = firstPathPart;
        
        // Iterate all parts from first to last
        for (int i = 0; i < parts.length; i++) {
            // Advance last part every loop, so it is the last part after the loop ends
            lastPart = parts[i];
            
            // Is it a variable path part?
            final Matcher m = VARIABLE_PATH_PART.matcher(lastPart);
            if (!m.find()) {
                // If not, must be fixed
//                lastPathPart.fixedPart = lastPart;
            } else {
                // If so, get variable type
//                lastPathPart.variablePart.pathParam = Optional.of(VariableParamType.from(m.group(1)));
            }
            
            // Add path part to map if it is not the first part
            if (i > 0) {
                prevPathPart.nextParts.put(prevPathPart, lastPathPart);
            }

            // Create a new last path part if at least one more part exists
            if (i < parts.length - 1) {
                lastPathPart = new PathElement();
            }
        }
        
        // Is there a single question mark in the URL after one or more path parts?
        final String[] querySplit = lastPart.split("[?]");
        if (querySplit.length > 2) {
            throw new IllegalArgumentException(QUERY_PARAMS_ONE_QUESTION_MARK);
        }
        if (querySplit.length == 2) {
            // One ?, so a string before and a string after
            // Split further ampersand between name:type pairs
            final String[] params = querySplit[1].split("&");
            for (final String param : params) {
                final Matcher m = QUERY_PARAMS_PART.matcher(param);
                if (!m.matches()) {
                    throw new IllegalArgumentException(String.format(ILLEGAL_QUERY_MSG, param));
                }
                
                final String paramName = m.group(1);
                final VariableParamType paramType = VariableParamType.from(m.group(2));
                final boolean required = m.group(3) != null;
//                lastPathPart.variablePart.queryParams.put(
//                    paramName,
//                    new QueryParam(paramName, paramType, required)
//                );
            }
        }
        
        return firstPathPart;
    }
    
    /**
     * add an endpoint, given a method and a url, of the following pseudo-regex form:
     * /fixedPart(/fixedPart|/prefix?{variableType}postfix?)*([?]firstParamName=paramType+?(&nextParamName=paramType+?)*)?
     * 
     * where:
     * - fixedPart means a hard coded part
     * - variableType means a path variable in braces, valid types are defined in {@link VariablePathType}
     * - prefix and postfix are hard coded parts before and/or after a path variable in braces
     * - paramType means a query param type, valid types are defined in {@link VariableParamType}
     * 
     * example urls:
     * 
     * /customer
     * /customer?lastName=string+&firstName=string - lastName is required (plus sign), firstName is optional
     * /customer/{uuid}
     * /customer/id-{uuid}/address?type=string
     * 
     * hard-coded parts must contain letters, digits, dots and dashes - these are the only sensible characters in REST urls.
     * 
     * @param method
     * @param url
     * @return
     */
    void addEndpoint(
        final String method,
        final String url
    ) {
        //
    }
    
    /**
     * Create a server for the given endpoints
     * 
     * @return server
     */
    Server end() {
        return new Server(services);
    }
    
    // ==== Builder
    
    /**
     * Begin building a Server
     * 
     * @return builder
     */
    public static ServerBuilder begin() {
        return new ServerBuilder();
    }
    
    /**
     * Add required first endpoint
     */
    public class FirstEndpoint {
        public MoreEndPoints add(
            final String method,
            final String url
        ) {
            ServerBuilder.this.addEndpoint(method, url);
            return new MoreEndPoints();
        }
    }
    
    /**
     * Add optional additional endpoints
     */
    public class MoreEndPoints {
        public MoreEndPoints add(
            final String method,
            final String url
        ) {
            addEndpoint(method, url);
            return new MoreEndPoints();
        }
        
        /**
         * End building, returning a Server for all the provided endpoints.
         * 
         * @return server
         */
        public Server end() {
            return ServerBuilder.this.end();
        }
    }
}
