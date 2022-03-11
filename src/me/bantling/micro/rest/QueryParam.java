package me.bantling.micro.rest;

import java.util.Objects;

/**
 * A url query parameter, which has a name, type, and optionality.
 * query parameters compare only on the name, as two parameters can't have the same name.
 */
public class QueryParam {
    
    // ==== Fields
    
    /**
     * The query param name
     */
    private final String name;
    
    /**
     * The query param type
     */
    private final VariableParamType type;
    
    /**
     * The optionality of the param
     */
    private final boolean required;
    
    // ==== Construct

    /**
     * Construct
     * 
     * @param name
     * @param type
     * @param required
     */
    public QueryParam(
        final String name,
        final VariableParamType type,
        final boolean required
    ) {
       this.name = Objects.requireNonNull(name, "name");
       this.type = Objects.requireNonNull(type, "type");
       this.required = required;
    }
    
    // ==== Object
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = o == this;
        if ((! equals) && (o instanceof QueryParam)) {
            final QueryParam obj = (QueryParam)(o);
            equals = name.equals(obj.name);
        }
        
        return equals;
    }
    
    @Override
    public String toString() {
        return
            QueryParam.class.getSimpleName() +
            "[name=" + name +
            ",type=" + type +
            ",required=" + required +
            ']';
    }
}
