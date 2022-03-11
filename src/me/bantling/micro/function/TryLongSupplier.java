package me.bantling.micro.function;

// A LongSupplier that can throw any exception
@FunctionalInterface
public interface TryLongSupplier {
	public long getAsLong() throws Throwable;
}
