package me.bantling.micro.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.function.Supplier;

import me.bantling.micro.tryfn.Try;

// Various useful methods to make Unicode easier in Java
public final class Unicode {
    Unicode() {
        throw new RuntimeException();
    }
    
    final static String HIGH_SURROGATE_EOF           = "Invalid Unicode at char %s, a high surrogate cannot be last character";
    final static String HIGH_SURROGATE_LOW_SURROGATE = "Invalid Unicode at char %s, a high surrogate must be followed by a low surrogate";
    
	// NO_POSITION supplies an empty string suitable for nextCodePoint position parameter.
	final static Supplier<String> NO_POSITION = () -> "";
	
	// Given a reader, which provides UTF-16 characters,
	// Read one or two UTF-16 characters as needed into a UTF-32 int value.
	// If a high surrogate is not followed by a low surrogate, and IOException is thrown.
	// If the reader returns EOF as the next character, EOF is returned.
	public static int nextCodePoint(
		final Reader reader,
		final Supplier<String> position
	) {
		return Try.getInt(() -> {
			int theChar = reader.read();
			if (theChar < 0) {
				return -1;
			}
			
			if (Character.isHighSurrogate((char)(theChar))) {
				int char2 = reader.read();
				if (char2 < 0) {
					throw new IOException(String.format(HIGH_SURROGATE_EOF, position.get()));
				}
				
				if (! Character.isLowSurrogate((char)(char2))) {
					throw new IOException(String.format(HIGH_SURROGATE_LOW_SURROGATE, position.get()));
				}
				
				// isHighSurrogate(theChar) and isLowSurrogate(char2) implies isValidCodePoint(theChar, char2)
				theChar = Character.toCodePoint((char)(theChar), (char)(char2));
			}

			return theChar;
		});
	}
	
	// Unread a code point, that may occupy one or two char values
	public static void unreadCodePoint(
		final PushbackReader reader,
		final int theChar
	) {
		Try.to(() -> {
			final char[] bytes = Character.toChars(theChar);
			if (bytes.length == 2) {
				reader.unread(bytes[1]);
			}
			reader.unread(bytes[0]);
		});
	}
	
	// Format a code point as one or two \\uXXXX escape sequences that are valid in a Java string
	public static String formatAsUnicodeEscapes(final int codePoint) {
		final char[] chars = Character.toChars(codePoint);
		String escaped = String.format("\\u%04x", Integer.valueOf(chars[0]));
		if (chars.length == 2) {
			escaped += String.format("\\u%04x", Integer.valueOf(chars[1]));
		}
		
		return escaped;
	}
}
