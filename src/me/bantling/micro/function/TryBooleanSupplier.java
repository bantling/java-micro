package me.bantling.micro.function;

// A BooleanSupplier that can throw any exception
@FunctionalInterface
public interface TryBooleanSupplier {
	public boolean getAsBoolean() throws Throwable;
}
