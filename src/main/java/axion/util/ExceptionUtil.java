package axion.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionUtil {

    @FunctionalInterface
    public interface Consumer_WithExceptions<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface Function_WithExceptions<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public interface Supplier_WithExceptions<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface Runnable_WithExceptions {
        void accept() throws Exception;
    }

    /** .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println)); */
    public static <T> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T> consumer) {
        return t -> {
            try { consumer.accept(t); }
            catch (Exception exception) { throwAsUnchecked(exception); }
        };
    }

    /** .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println)); */
    public static <T> Consumer<T> ignoreConsumer(Consumer_WithExceptions<T> consumer) {
        return t -> {
            try { consumer.accept(t); }
            catch (Exception ignore) { }
        };
    }

    /** .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName)) */
    public static <T, R> Function<T, R> rethrowFunction(Function_WithExceptions<T, R> function) {
        return t -> {
            try { return function.apply(t); }
            catch (Exception exception) { throwAsUnchecked(exception); return null; }
        };
    }

    /** .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName)) */
    public static <T, R> Function<T, R> ignoreFunction(Function_WithExceptions<T, R> function) {
        return t -> {
            try { return function.apply(t); }
            catch (Exception ignore) { return null; }
        };
    }

    /** rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))), */
    public static <T> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T> function) {
        return () -> {
            try { return function.get(); }
            catch (Exception exception) { throwAsUnchecked(exception); return null; }
        };
    }

    /** ignoreSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))), */
    public static <T> Supplier<T> ignoreSupplier(Supplier_WithExceptions<T> function) {
        return () -> {
            try { return function.get(); }
            catch (Exception exception) { return null; }
        };
    }

    /** uncheck(() -> Class.forName("xxx")); */
    public static void uncheck(Runnable_WithExceptions t) {
        try { t.accept(); }
        catch (Exception exception) { throwAsUnchecked(exception); }
    }

    /** ignore(() -> Class.forName("xxx")); */
    public static void ignore(Runnable_WithExceptions t) {
        try { t.accept(); }
        catch (Exception ignore) { }
    }

    /** uncheck(() -> Class.forName("xxx")); */
    public static <R> R uncheck(Supplier_WithExceptions<R> supplier) {
        try { return supplier.get(); }
        catch (Exception exception) { throwAsUnchecked(exception); return null; }
    }

    /** ignore(() -> Class.forName("xxx")); */
    public static <R> R ignore(Supplier_WithExceptions<R> supplier) {
        try { return supplier.get(); }
        catch (Exception ignore) { return null; }
    }

    /** uncheck(Class::forName, "xxx"); */
    public static <T, R> R uncheck(Function_WithExceptions<T, R> function, T t) {
        try { return function.apply(t); }
        catch (Exception exception) { throwAsUnchecked(exception); return null; }
    }

    /** ignore(Class::forName, "xxx"); */
    public static <T, R> R ignore(Function_WithExceptions<T, R> function, T t) {
        try { return function.apply(t); }
        catch (Exception ignore) { return null; }
    }

    public static <R extends RuntimeException> R runtime(Exception e) {
        if(e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings ("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E { throw (E)exception; }
}
