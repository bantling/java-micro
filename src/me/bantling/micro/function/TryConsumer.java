package me.bantling.micro.function;

// A side effect that can throw any exception
@FunctionalInterface
public interface TryConsumer<T> {
	public void accept(T t) throws Throwable;
}
