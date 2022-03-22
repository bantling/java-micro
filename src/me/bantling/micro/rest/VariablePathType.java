package me.bantling.micro.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import me.bantling.micro.util.Collections;

/**
 * {@code VariablePathType} is a case-insensitive type name of a variable path part
 */
enum VariablePathType {
    /**
     * STRING is represented as {str} or {string}
     */
    STRING,

    /**
     * INT is represented as {int} or {integer}
     */
    INT,

    /**
     * LONG is represented as {long}
     */
    LONG,

    /**
     * UUID is represented as {uuid}
     */
    UUID;
    
    private static final Map<String, VariablePathType> STRING_TO_PATH_TYPE = Collections.map(
        new HashMap<String, VariablePathType>()
    ).
        add("{str}",     STRING).
        add("{string}",  STRING).
        add("{int}",     INT).
        add("{integer}", INT).
        add("{long}",    LONG).
        add("{uuid}",    UUID).
    done();
    
    /**
     * @return an appropriate enum constant given a case-insensitive string
     * @throws NoSuchElementException
     */
    public static VariablePathType from(final String str) {
        return STRING_TO_PATH_TYPE.computeIfAbsent(
            str.toLowerCase(),
            $ -> { throw new NoSuchElementException("No VariablePathType enum constant associated with " + str); }
        );
    }
}