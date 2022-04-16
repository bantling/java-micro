package me.bantling.micro.rest;

import java.util.Map;
import java.util.NoSuchElementException;

import me.bantling.micro.util.Collections;
import me.bantling.micro.util.Tuple;

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
        Tuple.of("{bool}",    BOOLEAN),
        Tuple.of("{boolean}", BOOLEAN),
        Tuple.of("{str}",     STRING),
        Tuple.of("{string}",  STRING),
        Tuple.of("{int}",     INT),
        Tuple.of("{integer}", INT),
        Tuple.of("{long}",    LONG),
        Tuple.of("{float}",   FLOAT),
        Tuple.of("{double}",  DOUBLE),
        Tuple.of("{uuid}",    UUID)
    );
    
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
