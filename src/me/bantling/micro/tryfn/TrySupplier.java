package me.bantling.micro.tryfn;

// A Supplier that can throw any exception
@FunctionalInterface
public interface TrySupplier<T> {
	public T get() throws Throwable;
}
