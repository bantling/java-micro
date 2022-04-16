package me.bantling.micro.rest;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

import me.bantling.micro.util.Tuple;

/**
 * {@code PathPart} represents a union of a fixed or variable part (eg "customer" or "{uuid}"),
 * and contains an optional handler and possible more path parts.
 */
final class PathElement {
    
    // ==== Fields
    /**
     * String or variable fixed part.
     */
    Tuple.UnionTwo<String, VariableParamType> fixedOrVariablePart;
    
    /**
     * Zero or more further path elements after this one.
     * The map can express multiple branches of different lengths.
     */
    Map<PathElement, PathElement> nextParts;
    
    /**
     * Zero or more query parameters.
     * Only relevant if this is the last path element in a given branch.
     */
    Map<String, QueryParam> queryParams;
    
    /**
     * The handler that accepts an {@link HttpExchange}, followed by any additional parameters
     */
    Optional<MethodHandle> handler;
    
    // ==== Construct
    
    PathElement() {
        this.fixedOrVariablePart = null; //Tuple.Union.ofNullable(null, null);
        this.queryParams = new HashMap<>();
        this.handler = Optional.empty();
        this.nextParts = new HashMap<>();
    }
    
    // ==== Object

    @Override
    public int hashCode() {
        return Objects.hashCode(fixedOrVariablePart);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = o == this;
        if ((! equals) && (o instanceof PathElement)) {
            final PathElement obj = (PathElement)(o);
            equals = Objects.equals(fixedOrVariablePart, obj.fixedOrVariablePart);
        }
        
        return equals;
    }
    
    @Override
    public String toString() {
        return
            PathElement.class.getSimpleName() +
            "[fixedOrVariablePart=" + fixedOrVariablePart +
            ",handler=" + handler +
            ",nextParts=" + nextParts +
            ']';
    }
}
