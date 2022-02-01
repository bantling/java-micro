package me.bantling.micro.tryfn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public class TestTry {
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
    public void toSideEffect() {
        final int[] counter = {0};
        final TrySideEffect inc = () -> {counter[0]++;};
        final TrySideEffect dieNotRuntimeException = () -> {throw new IOException();};
        final TrySideEffect dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> SideEffect toSideEffectWith(
         *      final Supplier<X> exceptionSupplier,
         *      final TrySideEffect sideEffect,
         *      final TrySideEffect... moreSideEffects
         *  )
         */
        counter[0] = 0;
        
        // no exception
        Try.toSideEffectWith(supplierRuntimeException, inc, inc).perform();
        assertEquals(2, counter[0]);
        
        // no exception
        Try.toSideEffectWith(supplierNotRuntimeException, inc, inc).perform();
        assertEquals(4, counter[0]);
        
        // first dies, 1 cause
        try {
            Try.toSideEffectWith(supplierRuntimeException, dieNotRuntimeException, inc).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(4, counter[0]);
        
        // second dies, 1 cause
        try {
            Try.toSideEffectWith(supplierRuntimeException, inc, dieNotRuntimeException).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // first dies, 2 causes
        try {
            Try.toSideEffectWith(supplierNotRuntimeException, dieNotRuntimeException, inc).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(5, counter[0]);
        
        // second dies, 2 causes
        try {
            Try.toSideEffectWith(supplierNotRuntimeException, inc, dieNotRuntimeException).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, IOException.class);
        }
        assertEquals(6, counter[0]);
        
        /*
         * public static SideEffect toSideEffect(
         *     final TrySideEffect sideEffect,
         *     final TrySideEffect... moreSideEffects
         * )
         */
        counter[0] = 0;
        
        // no exception
        Try.toSideEffect(inc, inc).perform();
        assertEquals(2, counter[0]);
        
        // first dies, 1 cause
        try {
            Try.toSideEffect(dieNotRuntimeException, inc).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        }
        assertEquals(2, counter[0]);
        
        // second dies, 1 cause
        try {
            Try.toSideEffect(inc, dieNotRuntimeException).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        }
        assertEquals(3, counter[0]);
        
        // first dies, no causes
        try {
            Try.toSideEffect(dieRuntimeException, inc).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(3, counter[0]);
        
        // second dies, no causes
        try {
            Try.toSideEffect(inc, dieRuntimeException).perform();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        }
        assertEquals(4, counter[0]);
    }
    
    @Test
    public void toSupplier() {
        final int[] counter = {0};
        final TrySupplier<String> inc = () -> Integer.toString(++counter[0]);
        final TrySupplier<String> dieNotRuntimeException = () -> {throw new IOException();};
        final TrySupplier<String> dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <T, X extends Throwable> Supplier<T> toSupplierWith(
         *      final Supplier<X> exceptionSupplier,
         *      final TrySupplier<T> supplier,
         *      final TrySupplier<T>... moreSuppliers
         *  )
         */
        counter[0] = 0;
        
        // one, no exception
        assertEquals("1", Try.toSupplierWith(supplierRuntimeException, inc).get());
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        try {
            Try.toSupplierWith(supplierRuntimeException, dieNotRuntimeException).get();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(1, counter[0]);
        
        // one, die 2 causes
        try {
            Try.toSupplierWith(supplierNotRuntimeException, dieRuntimeException).get();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(1, counter[0]);
        
        // two, first succeeds
        assertEquals("2", Try.toSupplierWith(supplierRuntimeException, inc, inc).get());
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        assertEquals("3", Try.toSupplierWith(supplierRuntimeException, dieNotRuntimeException, inc).get());
        assertEquals(3, counter[0]);
        
        // two, both die, 1 cause
        try {
            Try.toSupplierWith(supplierRuntimeException, dieRuntimeException, dieNotRuntimeException).get();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(3, counter[0]);
        
        /*
         *  public static <T> Supplier<T> toSupplier(
         *      final TrySupplier<T> supplier,
         *      final TrySupplier<T>... moreSuppliers
         *  ) {
         *      return toSupplier(null, supplier, moreSuppliers);
         *  }
         */
        counter[0] = 0;
        
        // one, no exception
        assertEquals("1", Try.toSupplier(inc).get());
        assertEquals(1, counter[0]);
        
        // one, die no cause
        try {
            Try.toSupplier(dieRuntimeException).get();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        } 
        assertEquals(1, counter[0]);
        
        // two, first no exception
        assertEquals("2", Try.toSupplier(inc, dieRuntimeException).get());
        assertEquals(2, counter[0]);
        
        // two, first exception, second no exception
        assertEquals("3", Try.toSupplier(dieRuntimeException, inc).get());
        assertEquals(3, counter[0]);
        
        // two, both die, one cause
        try {
            Try.toSupplier(dieRuntimeException, dieNotRuntimeException).get();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        } 
        assertEquals(3, counter[0]);
    }
    
    @Test
    public void intSupplier() {
        final int[] counter = {0};
        final TryIntSupplier inc = () -> ++counter[0];
        final TryIntSupplier dieNotRuntimeException = () -> {throw new IOException();};
        final TryIntSupplier dieRuntimeException = () -> {throw new NumberFormatException();};
        final Supplier<NumberFormatException> supplierRuntimeException = () -> new NumberFormatException();
        final Supplier<InterruptedException> supplierNotRuntimeException = () -> new InterruptedException();
        
        /*
         *  public static <X extends Throwable> IntSupplier toIntSupplierWith(
         *      final Supplier<X> exceptionSupplier,
         *      final TryIntSupplier supplier,
         *      final TryIntSupplier... moreSuppliers
         *  ) {
         *      return () -> {
         *          try {
         *              return supplier.getAsInt();
         *          } catch (final Throwable ft) {
         *              Throwable t = ft;
         *              
         *              for (final TryIntSupplier nextSupplier : moreSuppliers) {
         *                  try {
         *                      return nextSupplier.getAsInt();
         *                  } catch (final Throwable nt) {
         *                      t = nt;
         *                  }
         *              }
         *                  
         *              throw generateRuntimeException(exceptionSupplier, t);
         *          }
         *      };
         *  }
         */
        counter[0] = 0;
        
        // one, no exception
        assertEquals(1, Try.toIntSupplierWith(supplierRuntimeException, inc).getAsInt());
        assertEquals(1, counter[0]);
        
        // one, die 1 cause
        try {
            Try.toIntSupplierWith(supplierRuntimeException, dieNotRuntimeException).getAsInt();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        } 
        assertEquals(1, counter[0]);
        
        // one, die 2 causes
        try {
            Try.toIntSupplierWith(supplierNotRuntimeException, dieRuntimeException).getAsInt();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, InterruptedException.class, NumberFormatException.class);
        }
        assertEquals(1, counter[0]);
        
        // two, first succeeds
        assertEquals(2, Try.toIntSupplierWith(supplierRuntimeException, inc, inc).getAsInt());
        assertEquals(2, counter[0]);
        
        // two, first dies, second succeeds
        assertEquals(3, Try.toIntSupplierWith(supplierRuntimeException, dieNotRuntimeException, inc).getAsInt());
        assertEquals(3, counter[0]);
        
        // two, both die, 1 cause
        try {
            Try.toIntSupplierWith(supplierRuntimeException, dieRuntimeException, dieNotRuntimeException).getAsInt();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class, IOException.class);
        }
        assertEquals(3, counter[0]);
        
        /*
         * public static IntSupplier toIntSupplier(
         *      final TryIntSupplier supplier,
         *      final TryIntSupplier... moreSuppliers
         *  ) {
         *      return toIntSupplierWith(null, supplier, moreSuppliers);
         *  }
         */
        counter[0] = 0;
        
        // one, no exception
        assertEquals(1, Try.toIntSupplier(inc).getAsInt());
        assertEquals(1, counter[0]);
        
        // one, die no cause
        try {
            Try.toIntSupplier(dieRuntimeException).getAsInt();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, NumberFormatException.class);
        } 
        assertEquals(1, counter[0]);
        
        // two, first no exception
        assertEquals(2, Try.toIntSupplier(inc, dieRuntimeException).getAsInt());
        assertEquals(2, counter[0]);
        
        // two, first exception, second no exception
        assertEquals(3, Try.toIntSupplier(dieRuntimeException, inc).getAsInt());
        assertEquals(3, counter[0]);
        
        // two, both die, one cause
        try {
            Try.toIntSupplier(dieRuntimeException, dieNotRuntimeException).getAsInt();
            fail("must die");
        } catch (final Exception e) {
            assertDeath(e, RuntimeException.class, IOException.class);
        } 
        assertEquals(3, counter[0]);        
    }
}
