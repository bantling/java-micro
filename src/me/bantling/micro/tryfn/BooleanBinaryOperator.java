package me.bantling.micro.tryfn;

@FunctionalInterface
public interface BooleanBinaryOperator {
    boolean applyAsBoolean(boolean left, boolean right);
}
