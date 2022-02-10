package me.bantling.micro.tryfn;

// A LongSupplier that can throw any exception
@FunctionalInterface
public interface TryLongSupplier {
	public long getAsLong() throws Throwable;
}
