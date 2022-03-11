package me.bantling.micro.function;

// A Supplier that can throw any exception
@FunctionalInterface
public interface TrySupplier<T> {
	public T get() throws Throwable;
}
