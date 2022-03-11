package me.bantling.micro.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import me.bantling.micro.util.Collections;

/**
 * {@code VariableParamType} is a case-insensitive type name of a query parameter
 */
enum VariableParamType {
    /**
     * BOOLEAN is represented as {bool} or {boolean}
     */
    BOOLEAN,

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
     * FLOAT is represented as {float}
     */
    FLOAT,
    
    /**
     * DOUBLE is represented as {double}
     */
    DOUBLE,

    /**
     * UUID is represented as {uuid}
     */
    UUID;
    
    private static final Map<String, VariableParamType> STRING_TO_PARAM_TYPE = Collections.mapOf(
        new HashMap<String, VariableParamType>()
    ).
        add("{bool}",    BOOLEAN).
        add("{boolean}", BOOLEAN).
        add("{str}",     STRING).
        add("{string}",  STRING).
        add("{int}",     INT).
        add("{integer}", INT).
        add("{long}",    LONG).
        add("{float}",   FLOAT).
        add("{double}",  DOUBLE).
        add("{uuid}",    UUID).
    done();
    
    /**
     * @return an appropriate enum constant given a case-insensitive string
     * @throws NoSuchElementException
     */
    public static VariableParamType from(final String str) {
        return STRING_TO_PARAM_TYPE.computeIfAbsent(
            str.toLowerCase(),
            $ -> { throw new NoSuchElementException("No VariableParamType enum constant associated with " + str); }
        );
    }
}
