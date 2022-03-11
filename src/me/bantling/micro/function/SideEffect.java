package me.bantling.micro.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@code SideEffect} is an operation to perform that requires no data, and returns no result.
 */
@FunctionalInterface
public interface SideEffect {
    /**
     * {@code ConsumerAdapter} is an immutable adapter from a {@code SideEffect} into a {@link Consumer<Void>}
     */
    public static final class ConsumerAdapter implements Consumer<Void> {
        /**
         * {@code sideEffect} is the side effect to perform
         */
        private final SideEffect sideEffect;
        
        /**
         * Constructor
         * 
         * @param sideEffect the side effect to perform
         */
        public ConsumerAdapter(final SideEffect sideEffect) {
            this.sideEffect = Objects.requireNonNull(sideEffect, "sideEffect");
        }
        
        /**
         * {@code accept} is the {@link Consumer} method to ignore the parameter of
         */
        @Override
        public void accept(Void t) {
            sideEffect.perform();
        }
        
        /**
         * @return the hashCode of the underlying {@code SideEffect}
         */
        @Override
        public int hashCode() {
            return sideEffect.hashCode();
        }

        /**
         * @return the result of calling the underlying {@code SideEffect} equals method with the given object
         */
        @Override
        public boolean equals(final Object o) {
            return sideEffect.equals(o);
        }
    }
    
	public void perform();
}
