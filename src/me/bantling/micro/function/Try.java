package me.bantling.micro.function;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.LongBinaryOperator;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public final class Try {
    private Try() {
        throw new RuntimeException();
    }
    
    // ==== Common methods
    
    /**
     * Generate a RuntimeException, even if a non-RuntimeException is provided.
     * There are four cases, resulting in a generated chain of 1 to 3 exceptions:
     * - exceptionSupplier is null, cause is a RuntimeException:
     *   returns cause
     * - exceptionSupplier is null, cause is not a RuntimeException:
     *   returns new RuntimeException(cause)
     * - exceptionSupplier result is a RuntimeException, cause is any kind of exception:
     *   returns supplied exception(cause)
     * - exceptionSupplier result is not a RuntimeException, cause is any kind of RuntimeException:
     *   returns new RuntimeException(supplied exception(cause))
     * 
     * Caller has to write a throw statement to throw the exception, which means:
     * - if the caller has to return a result, Java will not expect the caller to have a return statement afterwards
     * - if the caller does not have to return a result, Java knows no further code can be executed afterwards
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides 
     * @param exceptionSupplier supply a Throwable of type X
     * @param cause the original cause of the exception
     * @return a {@link RuntimeException} for the caller to throw
     */
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

    /**
     * Call {@link #generateRuntimeException(Supplier, Throwable)}, passing null for the exceptionSupplier
     * 
     * @param cause the original cause of the exception
     * @return a {@link RuntimeException} for the caller to throw
     */
    public static RuntimeException generateRuntimeException(
        final Throwable cause
    ) {
        return generateRuntimeException(null, cause);
    }
    
    // ==== SideEffect

    /**
     * Convert TrySideEffect(s) into a SideEffect that fails on the first exception, and ensures any exception is a
     * RuntimeException. Execute the resulting SideEffect.
     * The exceptionSupplier may be null.
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param sideEffect the first side effect
     * @param moreSideEffects any additional side effects
     */
    public static <X extends Throwable> void to(
        final Supplier<X> exceptionSupplier,
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        try {
            sideEffect.perform();
            for (final TrySideEffect t : moreSideEffects) {
                t.perform();
            }
        } catch (final Throwable cause) {
            throw generateRuntimeException(exceptionSupplier, cause);
        }
    }

    /**
     * Call {@link #to(Supplier, TrySideEffect, TrySideEffect...)}, passing null for the exceptionSupplier
     * @param sideEffect the first side effect
     * @param moreSideEffects any additional side effects
     */
    public static void to(
        final TrySideEffect sideEffect,
        final TrySideEffect... moreSideEffects
    ) {
        to(null, sideEffect, moreSideEffects);
    }
    
    // ==== Consumer

    /**
     * Convert TryConsumer(s) into a Consumer that fails on the first exception, and ensures any exception is a
     * RuntimeException. Execute the resulting consumer with the given value.
     * The exceptionSupplier may be null.
     * 
     * @param <T> the type of value to consume
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param value the value to consume
     * @param consumer the first consumer
     * @param moreConsumers any additional consumers
     */
    @SafeVarargs
    public static <T, X extends Throwable> void consume(
        final Supplier<X> exceptionSupplier,
        final T value,
        final TryConsumer<T> consumer,
        final TryConsumer<T>... moreConsumers
    ) {
        final Consumer<T> consume = t -> {
            try {
                consumer.accept(t);
                for (final TryConsumer<T> o : moreConsumers) {
                    o.accept(t);
                }
            } catch (final Throwable cause) {
                throw generateRuntimeException(exceptionSupplier, cause);
            }
        };
        consume.accept(value);
    }
    
    /**
     * Call {@link #consume(Supplier, Object, TryConsumer, TryConsumer...)}, passing null for the exceptionSupplier
     * 
     * @param <T> the type of value to consume
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param value the value to consume
     * @param consumer the first consumer
     * @param moreConsumers any additional consumers
     */
    @SafeVarargs
    public static <T> void consume(
        final T value,
        final TryConsumer<T> consumer,
        final TryConsumer<T>... moreConsumers
    ) {
        consume(null, value, consumer, moreConsumers);
    }
    
    // ==== Supplier
    
    /**
     * Convert TrySupplier(s) into a Supplier that combines results with an accumulator, ensuring any exception is a
     * RuntimeException.
     * The accumulator is used to operate on the first and second supplied values, then operate on that result and the
     * third supplied value, etc.
     * Execute the resulting supplier and return the result.
     * The exceptionSupplier may be null.
     * If there is only one supplier, the accumulator is not used, and may be null.
     * 
     * @param <T> The type of value to return
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <T, X extends Throwable> T get(
        final Supplier<X> exceptionSupplier,
        final BinaryOperator<T> accumulator,
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        final Supplier<T> supply = () -> {
            try {
                T result = supplier.get();
                if (moreSuppliers != null) {
                    for (final TrySupplier<T> nextSupplier : moreSuppliers) {
                        result = accumulator.apply(result, nextSupplier.get());
                    }
                }
                return result;
            } catch (final Throwable t) {
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
        
        return supply.get();
    }

    /**
     * Calls {@link #get(Supplier, BinaryOperator, TrySupplier, TrySupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers). 
     * 
     * @param <T> The type of value to return
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <T> T get(
        final BinaryOperator<T> accumulator,
        final TrySupplier<T> supplier,
        final TrySupplier<T>... moreSuppliers
    ) {
        return get(null, accumulator, supplier, moreSuppliers);
    }
    
    /**
     * Calls {@link #get(Supplier, BinaryOperator, TrySupplier, TrySupplier...)},
     * passing (null, null, accumulator, null).
     * 
     * @param <T> The type of value to return
     * @param supplier the one supplier
     * @return result of the one supplier
     */
    public static <T> T get(
        final TrySupplier<T> supplier
    ) {
        return get(null, null, supplier, (TrySupplier<T>[])(null));
    }
    
    // ==== BooleanSupplier
    
    /**
     * Convert TryBooleanSupplier(s) into a BooleanSupplier that combines results with an accumulator, ensuring any
     * exception is a RuntimeException.
     * The accumulator is used to operate on the first and second supplied values, then operate on that result and the
     * third supplied value, etc.
     * Execute the resulting supplier and return the result.
     * The exceptionSupplier may be null.
     * If there is only one supplier, the accumulator is not used, and may be null.
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <X extends Throwable> boolean getBoolean(
        final Supplier<X> exceptionSupplier,
        final BooleanBinaryOperator accumulator,
        final TryBooleanSupplier supplier,
        final TryBooleanSupplier... moreSuppliers
    ) {
        final BooleanSupplier supply = () -> {
            try {
                boolean result = supplier.getAsBoolean();
                if (moreSuppliers != null) {
                    for (final TryBooleanSupplier nextSupplier : moreSuppliers) {
                        result = accumulator.applyAsBoolean(result, nextSupplier.getAsBoolean());
                    }
                }
                return result;
            } catch (final Throwable t) {
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
        
        return supply.getAsBoolean();
    }

    /**
     * Call {@link #getBoolean(Supplier, BooleanBinaryOperator, TryBooleanSupplier, TryBooleanSupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static boolean getBoolean(
        final BooleanBinaryOperator accumulator,
        final TryBooleanSupplier supplier,
        final TryBooleanSupplier... moreSuppliers
    ) {
        return getBoolean(null, accumulator, supplier, moreSuppliers);
    }

    /**
     * Call {@link #getBoolean(Supplier, BooleanBinaryOperator, TryBooleanSupplier, TryBooleanSupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    public static boolean getBoolean(
        final TryBooleanSupplier supplier
    ) {
        return getBoolean(null, null, supplier, (TryBooleanSupplier[])(null));
    }
    
    // ==== IntSupplier

    /**
     * Convert TryIntSupplier(s) into an IntSupplier that combines results with an accumulator, ensuring any
     * exception is a RuntimeException.
     * The accumulator is used to operate on the first and second supplied values, then operate on that result and the
     * third supplied value, etc.
     * Execute the resulting supplier and return the result.
     * The exceptionSupplier may be null.
     * If there is only one supplier, the accumulator is not used, and may be null.
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <X extends Throwable> int getInt(
        final Supplier<X> exceptionSupplier,
        final IntBinaryOperator accumulator,
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        final IntSupplier supply = () -> {
            try {
                int result = supplier.getAsInt();
                if (moreSuppliers != null) {
                    for (final TryIntSupplier nextSupplier : moreSuppliers) {
                        result = accumulator.applyAsInt(result, nextSupplier.getAsInt());
                    }
                }
                return result;
            } catch (final Throwable t) {
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
        
        return supply.getAsInt();
    }

    /**
     * Call {@link #getInt(Supplier, IntBinaryOperator, TryIntSupplier, TryIntSupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static int getInt(
        final IntBinaryOperator accumulator,
        final TryIntSupplier supplier,
        final TryIntSupplier... moreSuppliers
    ) {
        return getInt(null, accumulator, supplier, moreSuppliers);
    }

    /**
     * Call {@link #getInt(Supplier, IntBinaryOperator, TryIntSupplier, TryIntSupplier...)},
     * passing (null, null, supplier, null).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    public static int getInt(
        final TryIntSupplier supplier
    ) {
        return getInt(null, null, supplier, (TryIntSupplier[])(null));
    }
    
    // ==== LongSupplier

    /**
     * Convert TryLongSupplier(s) into a LongSupplier that combines results with an accumulator, ensuring any
     * exception is a RuntimeException.
     * The accumulator is used to operate on the first and second supplied values, then operate on that result and the
     * third supplied value, etc.
     * Execute the resulting supplier and return the result.
     * The exceptionSupplier may be null.
     * If there is only one supplier, the accumulator is not used, and may be null.
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <X extends Throwable> long getLong(
        final Supplier<X> exceptionSupplier,
        final LongBinaryOperator accumulator,
        final TryLongSupplier supplier,
        final TryLongSupplier... moreSuppliers
    ) {
        final LongSupplier supply = () -> {
            try {
                long result = supplier.getAsLong();
                if (moreSuppliers != null) {
                    for (final TryLongSupplier nextSupplier : moreSuppliers) {
                        result = accumulator.applyAsLong(result, nextSupplier.getAsLong());
                    }
                }
                return result;
            } catch (final Throwable t) {
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
        
        return supply.getAsLong();
    }

    /**
     * Call {@link #getLong(Supplier, LongBinaryOperator, TryLongSupplier, TryLongSupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static long getLong(
        final LongBinaryOperator accumulator,
        final TryLongSupplier supplier,
        final TryLongSupplier... moreSuppliers
    ) {
        return getLong(null, accumulator, supplier, moreSuppliers);
    }

    /**
     * Call {@link #getLong(Supplier, LongBinaryOperator, TryLongSupplier, TryLongSupplier...)},
     * passing (null, null, supplier, null).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    public static long getLong(
        final TryLongSupplier supplier
    ) {
        return getLong(null, null, supplier, (TryLongSupplier[])(null));
    }
    
    // ==== DoubleSupplier

    /**
     * Convert TryDoubleSupplier(s) into a DoubleSupplier that combines results with an accumulator, ensuring any
     * exception is a RuntimeException.
     * The accumulator is used to operate on the first and second supplied values, then operate on that result and the
     * third supplied value, etc.
     * Execute the resulting supplier and return the result.
     * The exceptionSupplier may be null.
     * If there is only one supplier, the accumulator is not used, and may be null.
     * 
     * @param <X> the type of Throwable the exceptionSupplier provides
     * @param exceptionSupplier supply a Throwable of type X
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static <X extends Throwable> double getDouble(
        final Supplier<X> exceptionSupplier,
        final DoubleBinaryOperator accumulator,
        final TryDoubleSupplier supplier,
        final TryDoubleSupplier... moreSuppliers
    ) {
        final DoubleSupplier supply = () -> {
            try {
                double result = supplier.getAsDouble();
                if (moreSuppliers != null) {
                    for (final TryDoubleSupplier nextSupplier : moreSuppliers) {
                        result = accumulator.applyAsDouble(result, nextSupplier.getAsDouble());
                    }
                }
                return result;
            } catch (final Throwable t) {
                throw generateRuntimeException(exceptionSupplier, t);
            }
        };
        
        return supply.getAsDouble();
    }

    /**
     * Call {@link #getDouble(Supplier, DoubleBinaryOperator, TryDoubleSupplier, TryDoubleSupplier...)},
     * passing (null, accumulator, supplier, moreSuppliers).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    @SafeVarargs
    public static double getDouble(
        final DoubleBinaryOperator accumulator,
        final TryDoubleSupplier supplier,
        final TryDoubleSupplier... moreSuppliers
    ) {
        return getDouble(null, accumulator, supplier, moreSuppliers);
    }

    /**
     * Call {@link #getDouble(Supplier, DoubleBinaryOperator, TryDoubleSupplier, TryDoubleSupplier...)},
     * passing (null, null, supplier, null).
     * 
     * @param accumulator a binary operator that combines two values into a single new value
     * @param supplier first suppler
     * @param moreSuppliers any additional suppliers
     * @return result of combining all the supplied values
     */
    public static double getDouble(
        final TryDoubleSupplier supplier
    ) {
        return getDouble(null, null, supplier, (TryDoubleSupplier[])(null));
    }
}
