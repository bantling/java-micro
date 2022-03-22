package me.bantling.micro.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@code PathPart} represents a union of a fixed or variable part (eg "customer" or "{uuid}"),
 * and contains an optional handler and possible more path parts.
 */
final class PathPart {
    
    // ==== Fields
    
    String fixedPart;
    VariablePart variablePart;
    Optional<Consumer<Object[]>> handler;
    Map<PathPart, PathPart> nextParts;
    
    // ==== Construct
    
    PathPart() {
        this.variablePart = new VariablePart();
        this.handler = Optional.empty();
        this.nextParts = new HashMap<>();
    }
    
    // ==== Object

    @Override
    public int hashCode() {
        return Objects.hashCode(fixedPart) + Objects.hashCode(variablePart);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = o == this;
        if ((! equals) && (o instanceof PathPart)) {
            final PathPart obj = (PathPart)(o);
            equals =
                Objects.equals(fixedPart, obj.fixedPart) &&
                Objects.equals(variablePart, obj.variablePart);
        }
        
        return equals;
    }
    
    @Override
    public String toString() {
        return
            PathPart.class.getSimpleName() +
            "[fixedPart=" + fixedPart +
            ",variablePart=" + variablePart +
            ",handler=" + handler +
            ",nextParts=" + nextParts +
            ']';
    }
}
