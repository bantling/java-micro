package me.bantling.micro.json;

import java.math.BigDecimal;
import java.math.BigInteger;

// A JSON number, which can have any number of digits for the integer, fractional, and exponent parts.
public final class JSONNumber {
	private final String string;
	private boolean positive;
	private String integer;
	private String fractional;
	private boolean positiveExponent;
	private String exponent;
	
	//====  Construct
	
	public JSONNumber(
		final String string,
		final boolean positive,
		final String integer,
		final String fractional,
		final boolean positiveExponent,
		final String exponent
	) {
		this.string = string;
		this.positive = positive;
		this.integer = integer;
		this.fractional = fractional;
		this.positiveExponent = positiveExponent;
		this.exponent = exponent;
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
		return Integer.parseInt(fractional);
	}
	
	public int exponentAsInt() {
		return Integer.parseInt(exponent);
	}
	
	public long asLong() {
		return Long.parseLong(integer);
	}
	
	public long fractionalAsLong() {
		return Long.parseLong(fractional);
	}
	
	public long exponentAsLong() {
		return Long.parseLong(exponent);
	}
	
	public BigInteger asBigInteger() {
		return new BigInteger(integer);
	}
	
	public BigInteger fractionalAsBigInteger() {
		return new BigInteger(fractional);
	}
	
	public BigInteger exponentAsBigInteger() {
		return new BigInteger(exponent);
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
