package me.bantling.micro.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import me.bantling.micro.function.BooleanBinaryOperator;
import me.bantling.micro.function.Try;
import me.bantling.micro.function.TryBooleanSupplier;
import me.bantling.micro.function.TryConsumer;
import me.bantling.micro.function.TryDoubleSupplier;
import me.bantling.micro.function.TryIntSupplier;
import me.bantling.micro.function.TryLongSupplier;
import me.bantling.micro.function.TrySideEffect;
import me.bantling.micro.function.TrySupplier;

@SuppressWarnings("static-method")
public class TestTry {
    private static final Integer INT_ONE = Integer.valueOf(1);
    
    @Test
    public void constructor() {
        try {
            final Constructor<Try> cons = Try.class.getDeclaredConstructor();
            cons.setAccessible(true);
            cons.newInstance();
            fail("Must die");
        } catch (@SuppressWarnings("unused") final Throwable t) {
            //
        }
    }
    
    @Test
    public void generateRuntimeException() {
        // Must have cause
        try {
            assertNull(Try.generateRuntimeException(null, null));
        } catch (final Exception e) {
            assertTrue(e instanceof NullPointerException);
            assertEquals("cause", e.getMessage());
        }

        // No exceptionSupplier, runtime returned as is
        {
            final Throwable cause = new RuntimeException();
            assertEquals(cause, Try.generateRuntimeException(null, cause));
        }

        // No exceptionSupplier, runtime wraps non-runtime given
        {
            final Throwable cause = new Throwable();
            final RuntimeException generated = Try.generateRuntimeException(null, cause); 
            assertTrue(generated.getClass() == RuntimeException.class);
            assertTrue(generated.getCause() == cause);
        }

        // ExceptionSupplier provides runtime, wraps runtime cause
        {
            final Throwable cause = new Throwable();
            final RuntimeException supplied = new NullPointerException(); 
            final Supplier<Throwable> supplier = () -> supplied;
            
            final RuntimeException generated = Try.generateRuntimeException(supplier, cause); 
            assertTrue(generated.getClass() == NullPointerException.class);
            assertTrue(generated.getCause() == cause);
        }

        // ExceptionSupplier provides non-runtime, wraps non-runtime supplied, which wraps non-runtime cause
        {
            final Throwable cause = new Throwable();
            final Exception supplied = new IOException(); 
            final Supplier<Throwable> supplier = () -> supplied;
            
            final RuntimeException generated = Try.generateRuntimeException(supplier, cause); 
            assertTrue(generated.getClass() == RuntimeException.class);
            assertTrue(generated.getCause() == supplied);
            assertTrue(supplied.getCause() == cause);
        }

        // Overload (throwable), same as (null, throwable)
        {
            final Throwable cause = new Throwable();
            final RuntimeException generated = Try.generateRuntimeException(cause); 
            assertTrue(generated.getClass() == RuntimeException.class);
            assertTrue(generated.getCause() == cause);
        }
    }

    // Assert that the first throwable provided is the same class as thrownType.
    // If causes are provided, assert that the throwable a chain of causes with same class types.
    private void assertDeath(
        final Throwable thrown,
        final Class<?> thrownType,
        final Class<?>... causes
    ) {
        assertTrue(thrown instanceof RuntimeException);
        assertTrue(thrown.getClass() == thrownType);
        
        if (causes.length == 0) {
            assertNull(thrown.getCause());
        } else {                
            assertTrue(thrown.getCause().getClass() == causes[0]);
            
            if (causes.length == 1) {
                assertNull(thrown.getCause().getCause());
            } else {
                assertTrue(thrown.getCause().getCause().getClass() == causes[1]);
            }
        }
    }
    
    @Test
    public void to() {
        final int[] counter = {0};
        final TrySideEffect inc = () -> {counter[0]++;};
        final TrySideEffect dieNotRuntimeException = () -> {throw new IOException();};
        final TrySideEffect dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> void to(
         *      final Supplier<X> exceptionSupplier,
         *      final TrySideEffect sideEffect,
         *      final TrySideEffect... moreSideEffects
         *  )
         */
        counter[0] = 0;
        
        // no exception
        Try.to(supplierRuntimeException, inc, inc);
        assertEquals(2, counter[0]);
        
        // no exception
        Try.to(supplierNotRuntimeException, inc, inc);
        assertEquals(4, counter[0]);
        
        // first dies, 1 cause
        try {
            Try.to(supplierRuntimeException, dieRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(4, counter[0]);
        
        // second dies, 1 cause
        try {
            Try.to(supplierRuntimeException, inc, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // first dies, 2 causes
        try {
            Try.to(supplierNotRuntimeException, dieNotRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // second dies, 2 causes
        try {
            Try.to(supplierNotRuntimeException, inc, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(6, counter[0]);
        
        // overload(sideEffect, sideEffect...), same as (null, sideEffect, ,sideEffect...)
        
        // no exception
        Try.to(inc, inc);
        assertEquals(8, counter[0]);
        
        // die
        try {
            Try.to(inc, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        }
        assertEquals(9, counter[0]);
    }
    
    @Test
    public void consume() {
        final int[] counter = {0};
        final TryConsumer<Integer> add = t -> {counter[0] += t.intValue();};
        final TryConsumer<Integer> dieNotRuntimeException = t -> {throw new IOException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <T, X extends Throwable> void consume(
         *      final Supplier<X> exceptionSupplier,
         *      final T value,
         *      final TryConsumer<T> consumer,
         *      final TryConsumer<T>... moreConsumers
         *  )
         */
        counter[0] = 0;
        
        // no exception
        Try.consume(supplierRuntimeException, INT_ONE, add, add);
        assertEquals(2, counter[0]);
        
        // no exception
        Try.consume(supplierNotRuntimeException, INT_ONE, add, add);
        assertEquals(4, counter[0]);
        
        // first dies, 1 cause
        try {
            Try.consume(supplierRuntimeException, INT_ONE, dieNotRuntimeException, add);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(4, counter[0]);
        
        // second dies, 1 cause
        try {
            Try.consume(supplierRuntimeException, INT_ONE, add, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // first dies, 2 causes
        try {
            Try.consume(supplierNotRuntimeException, INT_ONE, dieNotRuntimeException, add);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // second dies, 2 causes
        try {
            Try.consume(supplierNotRuntimeException, INT_ONE, add, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(6, counter[0]);
        
        // overload(value, consumer, consumer...) same as (null, value, consumer, consumer...)

        // no exception
        Try.consume(INT_ONE, add, add);
        assertEquals(8, counter[0]);
        
        try {
            Try.consume(INT_ONE, add, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        }
        assertEquals(9, counter[0]);
    }
    
    @Test
    public void get() {
        final int[] counter = {0};
        final BinaryOperator<String> acc = (s1, s2) -> s1 + s2;
        final TrySupplier<String> inc = () -> Integer.toString(++counter[0]);
        final TrySupplier<String> dieNotRuntimeException = () -> {throw new IOException();};
        final TrySupplier<String> dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <T, X extends Throwable> Supplier<T> get(
         *      final Supplier<X> exceptionSupplier,
         *      final BinaryOperator<T> accumulator,
         *      final TrySupplier<T> supplier,
         *      final TrySupplier<T>... moreSuppliers
         *  )
         */
        
        // one, no exception
        counter[0] = 0;
        assertEquals("1", Try.get(supplierRuntimeException, null, inc));
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        counter[0] = 0;
        try {
            Try.get(supplierRuntimeException, null, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(0, counter[0]);
        
        // one, die 2 causes
        counter[0] = 0;
        try {
            Try.get(supplierNotRuntimeException, null, dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, first succeeds
        counter[0] = 0;
        assertEquals("12", Try.get(supplierRuntimeException, acc, inc, inc));
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        counter[0] = 0;
        try {
            Try.get(supplierRuntimeException, acc, dieNotRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both die, 1 cause
        counter[0] = 0;
        try {
            Try.get(supplierRuntimeException, null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (accumulator, supplier, supplier...) ssame as (null, accumulator, supplier, supplier...)
        
        // no exception
        counter[0] = 0;
        Try.get(acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.get(null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (supplier) same as (null, null, supplier, null)

        // no exception
        counter[0] = 0;        
        Try.get(inc);
        assertEquals(1, counter[0]);
        
        counter[0] = 0;
        try {
            Try.get(dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
    }
    
    @Test
    public void getBoolean() {
        final int[] counter = {0};
        final BooleanBinaryOperator acc = (b1, b2) -> b1 && b2;
        final TryBooleanSupplier inc = () -> { ++counter[0]; return true; };
        final TryBooleanSupplier dieNotRuntimeException = () -> {throw new IOException();};
        final TryBooleanSupplier dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> BooleanSupplier toBooleanSupplierWith(
         *      final Supplier<X> exceptionSupplier,
         *      final BooleanBinaryOperator accumulator,
         *      final TryBooleanSupplier supplier,
         *      final TryBooleanSupplier... moreSuppliers
         *  )
         */
        
        // one, no exception
        counter[0] = 0;
        assertTrue(Try.getBoolean(supplierRuntimeException, null, inc));
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        counter[0] = 0;
        try {
            Try.getBoolean(supplierRuntimeException, null, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(0, counter[0]);
        
        // one, die 2 causes
        counter[0] = 0;
        try {
            Try.getBoolean(supplierNotRuntimeException, null, dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, first succeeds
        counter[0] = 0;
        assertTrue(Try.getBoolean(supplierRuntimeException, acc, inc, inc));
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        counter[0] = 0;
        try {
            Try.getBoolean(supplierRuntimeException, acc, dieRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both die, 1 cause
        counter[0] = 0;
        try {
            Try.getBoolean(supplierRuntimeException, acc, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (accumulator, supplier, supplier...) same as (null, accumulator, supplier, supplier...)

        // no exception
        counter[0] = 0;
        Try.getBoolean(acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getBoolean(null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (supplier) same as (null, null, supplier, null)

        // no exception
        counter[0] = 0;
        Try.getBoolean(inc);
        assertEquals(1, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getBoolean(dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
    }
    
    @Test
    public void getInt() {
        final int[] counter = {0};
        final IntBinaryOperator acc = (i1, i2) -> i1 + i2;
        final TryIntSupplier inc = () -> ++counter[0];
        final TryIntSupplier dieNotRuntimeException = () -> {throw new IOException();};
        final TryIntSupplier dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> IntSupplier getInt(
         *      final Supplier<X> exceptionSupplier,
         *      final IntBinaryOperator accumulator,
         *      final TryIntSupplier supplier,
         *      final TryIntSupplier... moreSuppliers
         *  )
         */        
        // one, no exception
        counter[0] = 0;
        assertEquals(1, Try.getInt(supplierRuntimeException, null, inc));
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        counter[0] = 0;
        try {
            Try.getInt(supplierRuntimeException, null, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(0, counter[0]);
        
        // one, die 2 causes
        counter[0] = 0;
        try {
            Try.getInt(supplierNotRuntimeException, null, dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both succeed
        counter[0] = 0;
        assertEquals(3, Try.getInt(supplierRuntimeException, acc, inc, inc));
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        counter[0] = 0;
        try {
            Try.getInt(supplierNotRuntimeException, acc, dieRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both die, 1 cause
        counter[0] = 0;
        try {
            Try.getInt(supplierRuntimeException, acc, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (accumulator, supplier, supplier...) ssame as (null, accumulator, supplier, supplier...)

        // no exception
        counter[0] = 0;
        Try.getInt(acc, inc, inc);
        assertEquals(2, counter[0]);
        
        counter[0] = 0;
        try {
            Try.getInt(null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (supplier) same as (null, null, supplier, null)

        // no exception
        counter[0] = 0;
        Try.getInt(inc);
        assertEquals(1, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getInt(dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
    }
    
    @Test
    public void longSupplier() {
        final int[] counter = {0};
        final LongBinaryOperator acc = (i1, i2) -> i1 + i2;
        final TryLongSupplier inc = () -> ++counter[0];
        final TryLongSupplier dieNotRuntimeException = () -> {throw new IOException();};
        final TryLongSupplier dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> LongSupplier toLongSupplierWith(
         *      final Supplier<X> exceptionSupplier,
         *      final LongBinaryOperator accumulator,
         *      final TryLongSupplier supplier,
         *      final TryLongSupplier... moreSuppliers
         *  )
         */
        
        // one, no exception
        counter[0] = 0;
        assertEquals(1, Try.getLong(supplierRuntimeException, null, inc));
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        counter[0] = 0;
        try {
            Try.getLong(supplierRuntimeException, null, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(0, counter[0]);
        
        // one, die 2 causes
        counter[0] = 0;
        try {
            Try.getLong(supplierNotRuntimeException, null, dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, first succeeds
        counter[0] = 0;
        assertEquals(3, Try.getLong(supplierRuntimeException, acc, inc, inc));
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        counter[0] = 0;
        try {
            Try.getLong(supplierRuntimeException, acc, dieNotRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both die, 1 cause
        counter[0] = 0;
        try {
            Try.getLong(supplierRuntimeException, acc, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);

        // No error
        counter[0] = 0;
        Try.getLong(supplierRuntimeException, acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // overload (accumulator, supplier, supplier...) ssame as (null, accumulator, supplier, supplier...)

        // no exception
        counter[0] = 0;
        Try.getLong(acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getLong(null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (supplier) same as (null, null, supplier, null)

        // no exception
        counter[0] = 0;
        Try.getLong(inc);
        assertEquals(1, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getLong(dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
    }
    
    @Test
    public void doubleSupplier() {
        final int[] counter = {0};
        final DoubleBinaryOperator acc = (i1, i2) -> i1 + i2;
        final TryDoubleSupplier inc = () -> ++counter[0];
        final TryDoubleSupplier dieNotRuntimeException = () -> {throw new IOException();};
        final TryDoubleSupplier dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> DoubleSupplier toDoubleSupplierWith(
         *      final Supplier<X> exceptionSupplier,
         *      final DoubleBinaryOperator accumulator,
         *      final TryDoubleSupplier supplier,
         *      final TryDoubleSupplier... moreSuppliers
         *  )
         */
        
        // one, no exception
        counter[0] = 0;
        assertEquals(1, Try.getDouble(supplierRuntimeException, null, inc), 0);
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        counter[0] = 0;
        try {
            Try.getDouble(supplierRuntimeException, null, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(0, counter[0]);
        
        // one, die 2 causes
        counter[0] = 0;
        try {
            Try.getDouble(supplierNotRuntimeException, null, dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, first succeeds
        counter[0] = 0;
        assertEquals(3, Try.getDouble(supplierRuntimeException, acc, inc, inc), 0);
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        counter[0] = 0;
        try {
            Try.getDouble(supplierNotRuntimeException, acc, dieNotRuntimeException, inc);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(0, counter[0]);
        
        // two, both die, 1 cause
        counter[0] = 0;
        try {
            Try.getDouble(supplierRuntimeException, null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // No error
        counter[0] = 0;
        Try.getDouble(supplierRuntimeException, acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // overload (accumulator, supplier, supplier...) ssame as (null, accumulator, supplier, supplier...)
        
        // no exception
        counter[0] = 0;
        Try.getDouble(acc, inc, inc);
        assertEquals(2, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getDouble(null, dieRuntimeException, dieNotRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
        
        // overload (supplier) same as (null, null, supplier, null)
        
        // no exception
        counter[0] = 0;
        Try.getDouble(inc);
        assertEquals(1, counter[0]);
        
        // die
        counter[0] = 0;
        try {
            Try.getDouble(dieRuntimeException);
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(0, counter[0]);
    }
}
