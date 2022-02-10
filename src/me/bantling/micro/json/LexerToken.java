package me.bantling.micro.json;

import java.util.Objects;

// A single Token
public final class LexerToken {// The types of tokens to read
	public enum Type {
		STRING,
		NUMBER,
		TRUE,
		FALSE,
		NULL,
		OPEN_BRACE,
		CLOSE_BRACE,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		COLON,
		COMMA
	}
	
	// Singletons for tokens with static text
	public static final LexerToken TRUE_TOKEN = new LexerToken(Type.TRUE, "true");
	public static final LexerToken FALSE_TOKEN = new LexerToken(Type.FALSE, "false");
	public static final LexerToken NULL_TOKEN = new LexerToken(Type.NULL, "null");
	public static final LexerToken COMMA_TOKEN = new LexerToken(Type.COMMA, ",");
	public static final LexerToken OPEN_BRACE_TOKEN = new LexerToken(Type.OPEN_BRACE, "{");
	public static final LexerToken COLON_TOKEN = new LexerToken(Type.COLON, ":");
	public static final LexerToken CLOSE_BRACE_TOKEN = new LexerToken(Type.CLOSE_BRACE, "}");
	public static final LexerToken OPEN_BRACKET_TOKEN = new LexerToken(Type.OPEN_BRACKET, "[");
	public static final LexerToken CLOSE_BRACKET_TOKEN = new LexerToken(Type.CLOSE_BRACKET, "]");
	
	public Type type;
	public String token = "";
	public boolean positive;
	public String integer = "";
	public String fractional = "";
	public boolean positiveExponent;
	public String exponent = "";
	
	// Construct
	LexerToken(
		final Type type,
		final String token
	) {
		this.type = Objects.requireNonNull(type);
		this.token = Objects.requireNonNull(token);
	}
	
	// Construct number
	LexerToken(
		final String token,
		boolean positive,
		String integer,
		String fractional,
		boolean positiveExponent,
		String exponent
	) {
		this.type = LexerToken.Type.NUMBER;
		this.token = Objects.requireNonNull(token);
		this.positive = positive;
		this.integer = Objects.requireNonNull(integer);
		this.fractional = Objects.requireNonNull(fractional);
		this.positiveExponent = positiveExponent;
		this.exponent = Objects.requireNonNull(exponent);
	}
	
	@Override
	public int hashCode() {
		return (31 * type.hashCode()) + token.hashCode();
	}
	
	@Override
	public boolean equals(final Object o) {
		boolean equals = o == this;
		if (
		        (! equals) && 
		        (o instanceof LexerToken)) {
			final LexerToken obj = (LexerToken)(o);
			equals =
				(type == obj.type) &&
				(token.equals(obj.token)) &&
				(positive == obj.positive) &&
				(integer.equals(obj.integer)) &&
				(fractional.equals(obj.fractional)) &&
				(positiveExponent == obj.positiveExponent) &&
				(exponent.equals(obj.exponent));
		}
		
		return equals;
	}
	
	@Override
	public String toString() {
		return
			"LexerToken[type=" + type.toString() +
			",token=" + token +
			",positive=" + positive +
			",integer=" + integer +
			",fractional=" + fractional +
			",positiveExponent=" + positiveExponent +
			",exponent=" + exponent +
			"]";
	}
}
