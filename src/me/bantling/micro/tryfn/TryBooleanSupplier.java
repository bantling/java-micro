package me.bantling.micro.tryfn;

// A BooleanSupplier that can throw any exception
@FunctionalInterface
public interface TryBooleanSupplier {
	public boolean getAsBoolean() throws Throwable;
}
