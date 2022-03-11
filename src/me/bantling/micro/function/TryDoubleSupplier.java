package me.bantling.micro.function;

// A DoubleSupplier that can throw any exception
@FunctionalInterface
public interface TryDoubleSupplier {
	public double getAsDouble() throws Throwable;
}
