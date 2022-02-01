package me.bantling.micro.tryfn;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class Try {
    private Try() {
        throw new RuntimeException();
    }
    
    // ==== Common methods
    
    // Generate a RuntimeException, even if a non-RuntimeException is provided
    // Caller has to write a throw statement to throw the exception, which means:
    // - if the caller has to return a result, Java will not expect the caller to have a return statement afterwards
    // - if the caller does not have to return a result, Java knows no further code can be executed afterwards
    public static <X extends Throwable> RuntimeException generateRuntimeException(
        final Supplier<X> exceptionSupplier,
        final Throwable cause
    ) {
        Objects.requireNonNull(cause, "cause");
        
        final Throwable t;
        if (exceptionSupplier == null) {
            t = cause;
        } else {
            t = exceptionSupplier.get();
            t.initCause(cause);
        }
        
        return (t instanceof RuntimeException) ?
            (RuntimeException)(t) :
            new RuntimeException(t);
    }
    
    // ==== SideEffect

    // Convert TrySideEffect(s) into a SideEffect that fails on the first exception, and ensures any exception is a RuntimeException
    public static <X extends Throwable> SideEffect toSideEffectWith(
        final Supplier<X> exceptionSupplier,
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        return () -> {
            try {
                sideEffect.perform();
                for (final TrySideEffect t : moreSideEffects) {
                    t.perform();
                }
            } catch (final Throwable cause) {
                throw generateRuntimeException(exceptionSupplier, cause);
            }     
        };
    }

    // Convert TrySideEffect(s) into a SideEffect thats ensures any exception is a RuntimeException
    public static SideEffect toSideEffect(
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        return toSideEffectWith(null, sideEffect, moreSideEffects);
    }

    // Perform TrySideEffect(s) immediately, ensuring any exception is a RuntimeException
    public static <X extends Throwable> void toWith(
        final Supplier<X> exceptionSupplier,
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        toSideEffectWith(exceptionSupplier, sideEffect, moreSideEffects).perform();
    }

    // Perform TrySideEffect(s) immediately, ensuring any exception is a RuntimeException
    public static void to(
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        toSideEffectWith(null, sideEffect, moreSideEffects).perform();
    }
    
    // ==== Supplier
    
    // Convert TrySupplier(s) into a Supplier that returns first result that does not throw.
    // If all TrySupplier(s) throw, then ensure last exception thrown is a RuntimeException.
    @SafeVarargs
    public static <T, X extends Throwable> Supplier<T> toSupplierWith(
        final Supplier<X> exceptionSupplier,
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        return () -> {
            try {
                return supplier.get();
            } catch (final Throwable ft) {
                Throwable t = ft;
                
                for (final TrySupplier<T> nextSupplier : moreSuppliers) {
                    try {
                        return nextSupplier.get();
                    } catch (final Throwable nt) {
                        t = nt;
                    }
                }
                    
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
    }

    // Convert TrySupplier(s) into a Supplier that returns first result that does not throw.
    // If all TrySupplier(s) throw, then ensure last exception thrown is a RuntimeException.
    @SafeVarargs
    public static <T> Supplier<T> toSupplier(
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        return toSupplierWith(null, supplier, moreSuppliers);
    }
    
    // Get result of the first TrySupplier that succeeds, ensuring last exceptions ia a RuntimeException
    @SafeVarargs
    public static <T, X extends Throwable> T getWith(
        final Supplier<X> exceptionSupplier,
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        return toSupplierWith(exceptionSupplier, supplier, moreSuppliers).get();
    }
    
    // Get result of the first TrySupplier that succeeds, ensuring last exceptions ia a RuntimeException
    @SafeVarargs
    public static <T> T get(
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        return toSupplierWith(null, supplier, moreSuppliers).get();
    }
    
    // ==== IntSupplier

    // Convert TryIntSupplier(s) into an IntSupplier that returns TryIntSupplier that does not throw.
    // If all TryIntSupplier(s) throw, then ensure last exception thrown is a RuntimeException.
    @SafeVarargs
    public static <X extends Throwable> IntSupplier toIntSupplierWith(
        final Supplier<X> exceptionSupplier,
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        return () -> {
            try {
                return supplier.getAsInt();
            } catch (final Throwable ft) {
                Throwable t = ft;
                
                for (final TryIntSupplier nextSupplier : moreSuppliers) {
                    try {
                        return nextSupplier.getAsInt();
                    } catch (final Throwable nt) {
                        t = nt;
                    }
                }
                    
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
    }

    // Convert TryIntSupplier(s) into an IntSupplier that returns TryIntSupplier that does not throw.
    // If all TryIntSupplier(s) throw, then ensure last exception thrown is a RuntimeException.
    @SafeVarargs
    public static IntSupplier toIntSupplier(
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        return toIntSupplierWith(null, supplier, moreSuppliers);
    }

    // Get result of the first TryIntSupplier that succeeds, ensuring last exception is a RuntimeException
    @SafeVarargs
    public static <X extends Throwable> int getAsIntWith(
        final Supplier<X> exceptionSupplier,
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        return toIntSupplierWith(exceptionSupplier, supplier, moreSuppliers).getAsInt();
    }

    // Get result of the first TryIntSupplier that succeeds, ensuring last exception is a RuntimeException
    @SafeVarargs
    public static int getAsInt(
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        return toIntSupplierWith(null, supplier, moreSuppliers).getAsInt();
    }
}
