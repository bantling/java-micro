package me.bantling.micro.tryfn;

// A Supplier that can throw any exception
@FunctionalInterface
public interface TryIntSupplier {
	public int getAsInt() throws Throwable;
}
