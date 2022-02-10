package me.bantling.micro.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A JSON number, which can have any number of digits for the integer, fractional, and exponent parts.
public final class JSONNumber {
    // Floats, doubles, and bigdecimal vary in their representation as a string
    // The fractional and exponent parts may not be present if they are 0
    // If the fractional part is present, the exponent part may not be present if it is 0
    // The exponent char may be e or E
    // The sign after the exponent char may not be present
    private static final Pattern INT_LONG_FLOAT_DOUBLE_BIGDECIMAL_REGEX =
        Pattern.compile("^(-)?([0-9]+)(?:[.]([0-9]+)(?:[eE]([-+])?([0-9]+))?)?$");
    
	private final String string;
	private boolean positive;
	private String integer;
	private String fractional;
	private boolean positiveExponent;
	private String exponent;
	
	//====  Construct
	
	JSONNumber(
		final String string,
		final boolean positive,
		final String integer,
		final String fractional,
		final boolean positiveExponent,
		final String exponent
	) {
		this.string = Objects.requireNonNull(string, "string");
		this.positive = positive;
		this.integer = Objects.requireNonNull(integer, "integer");
		this.fractional = fractional == null ? "" : fractional;
		this.positiveExponent = positiveExponent;
		this.exponent = exponent == null ? "" : exponent;
	}
	
	static JSONNumber ofIntLongFloatDoubleBigDecimal(final String str) {
	    final Matcher m = INT_LONG_FLOAT_DOUBLE_BIGDECIMAL_REGEX.matcher(str);
	    if (! m.matches()) {
	        throw new IllegalArgumentException(str + " is not a correctly formatted JSON number");
	    }

	    // (-)?([0-9]+)(?:[.]([0-9]+)(?:[eE]([-+])?([0-9]+))?)?;
	    final String positive = m.group(1);
	    final String integer = m.group(2);
	    final String fractional = m.group(3);
	    final String positiveExponent = m.group(4);
	    final String exponent = m.group(5);
	    
	    return new JSONNumber(
            str,
            positive == null,
            integer,
            fractional,
            ((positiveExponent == null) || (positiveExponent.charAt(0) == '+')),
            exponent
        );
	}
    
    public static JSONNumber of(final int i) {
        return ofIntLongFloatDoubleBigDecimal(Integer.toString(i));
    }
    
    public static JSONNumber of(final Integer i) {
        return ofIntLongFloatDoubleBigDecimal(i.toString());
    }
    
    public static JSONNumber of(final long l) {
        return ofIntLongFloatDoubleBigDecimal(Long.toString(l));
    }
    
    public static JSONNumber of(final Long l) {
        return ofIntLongFloatDoubleBigDecimal(l.toString());
    }
    
    public static JSONNumber of(final float f) {
        return ofIntLongFloatDoubleBigDecimal(Float.valueOf(f).toString());
    }
    
    public static JSONNumber of(final Float f) {
        return ofIntLongFloatDoubleBigDecimal(f.toString());
    }
    
    public static JSONNumber of(final double d) {
        return ofIntLongFloatDoubleBigDecimal(Double.valueOf(d).toString());
    }
    
    public static JSONNumber of(final Double d) {
        return ofIntLongFloatDoubleBigDecimal(d.toString());
    }

    public static JSONNumber of(final BigInteger b) {
        final String str = b.toString();
        String integer = str;

        boolean positive = true;
        if (integer.charAt(0) == '-') {
            positive = false;
            integer = integer.substring(1);
        }
        
        return new JSONNumber(str, positive, integer, null, true, null);
    }
    
    public static JSONNumber of(final BigDecimal b) {
        return ofIntLongFloatDoubleBigDecimal(b.toString());
    }
	
	// ==== Object
	
	@Override
	public int hashCode() {
		return
			(31 * (31 * (31 * (31 * 
			  Boolean.hashCode(positive))
			+ integer.hashCode())
			+ fractional.hashCode())
			+ Boolean.hashCode(positiveExponent))
			+ exponent.hashCode();
	}
	
	@Override
	public String toString() {
		return string;
	}
	
	@Override
	public boolean equals(final Object o) {
		boolean equals = o == this;
		if ((! equals) && (o instanceof JSONNumber)) {
			final JSONNumber obj = (JSONNumber)(o);
			equals =
				(positive == obj.positive) &&
				integer.equals(obj.integer) &&
				fractional.equals(obj.fractional) &&
				(positiveExponent == obj.positiveExponent) &&
				exponent.equals(obj.exponent);
		}
		
		return equals;
	}
	
	// ==== Conversions
	
	public int asInt() {
		return Integer.parseInt(integer);
	}
	
	public int fractionalAsInt() {
		return fractional.isEmpty() ? 0 : Integer.parseInt(fractional);
	}
	
	public int exponentAsInt() {
		return exponent.isEmpty() ? 0 : Integer.parseInt(exponent);
	}
	
	public long asLong() {
		return Long.parseLong(integer);
	}
	
	public long fractionalAsLong() {
		return fractional.isEmpty() ? 0L : Long.parseLong(fractional);
	}
	
	public long exponentAsLong() {
		return exponent.isEmpty() ? 0L : Long.parseLong(exponent);
	}
	
	public BigInteger asBigInteger() {
		return new BigInteger(integer);
	}
	
	public BigInteger fractionalAsBigInteger() {
		return fractional.isEmpty() ? BigInteger.ZERO : new BigInteger(fractional);
	}
	
	public BigInteger exponentAsBigInteger() {
		return exponent.isEmpty() ? BigInteger.ZERO : new BigInteger(exponent);
	}
	
	public float asFloat() {
		return Float.parseFloat(string);
	}
	
	public double asDouble() {
		return Double.parseDouble(string);
	}
	
	public BigDecimal asBigDecimal() {
		return new BigDecimal(string);
	}
	
	// Accessors

	public boolean isPositive() {
		return positive;
	}

	public String getInteger() {
		return integer;
	}

	public String getFractional() {
		return fractional;
	}

	public boolean isPositiveExponent() {
		return positiveExponent;
	}

	public String getExponent() {
		return exponent;
	}
}
