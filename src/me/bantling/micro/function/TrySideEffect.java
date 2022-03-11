package me.bantling.micro.function;

// A side effect that can throw any exception
@FunctionalInterface
public interface TrySideEffect {
	public void perform() throws Throwable;
}
