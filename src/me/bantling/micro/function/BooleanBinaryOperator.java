package me.bantling.micro.function;

@FunctionalInterface
public interface BooleanBinaryOperator {
    boolean applyAsBoolean(boolean left, boolean right);
}
