package me.bantling.micro.function;

// An IntSupplier that can throw any exception
@FunctionalInterface
public interface TryIntSupplier {
	public int getAsInt() throws Throwable;
}
