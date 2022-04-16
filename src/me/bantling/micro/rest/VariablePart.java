package me.bantling.micro.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * VariablePart describes a single path part variable, and/or zero or more query parameters.
 * There must be at least one variable defined.
 */
public class VariablePart {
    
    // ==== Fields
    
    /**
     * Zero or one path parameters. 
     */
    Optional<VariableParamType> pathParam;
    
    /**
     * Zero or more query parameters.
     */
    Map<String, QueryParam> queryParams;
    
    // ==== Construct
    
    VariablePart() {
        this.pathParam = Optional.empty();
        this.queryParams = new HashMap<>();
    }
    
    // ==== Object
    
    /**
     * Only the path parameter is hashed, to enable matching up against another path parameter.
     * A given path part is either variable or it isn't, and if it is variable, it must be one specific type of variable.
     * As such, the hash code is 1231 if it has no variable path part, else 1237 if it does.
     */
    @Override
    public int hashCode() {
        return pathParam.isPresent() ? 1231 : 1237;
    }
    
    /**
     * Similar to {@link hashCode}, this method is coalesced into true if both objects have the same value for
     * pathParam presence - either they both have a path param, or neither has a path param. 
     */
    @Override
    public boolean equals(final Object o) {
        boolean equals = o == this;
        if ((! equals) && (o instanceof VariablePart)) {
            final VariablePart obj = (VariablePart)(o);
            equals = pathParam.isPresent() == obj.pathParam.isPresent();
        }
        
        return equals;
    }
    
    @Override
    public String toString() {
        return
            VariablePart.class.getSimpleName() + 
            '[' +
            "pathParam=" + (pathParam.isPresent() ? pathParam.get().name() : "") +
            ",queryParams=" + queryParams +
            ']';
    }
}
