package axion.web;

import axion.domain.base.BasePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public TestPage(PageParameters params) {
        super(params);
        // Parallel Array Sorting
        String[] test = new String[]{"foo", "bar", "baz"};
        Arrays.parallelSort(test);
        // TODO: Compare the timing of this to regular sort

        // Standardized Base64 Encoding/Decoding
        String encoded = Base64.getEncoder().encodeToString("I lurve my dog".getBytes());
        String decoded = new String(Base64.getDecoder().decode(encoded));

        // Nashorn
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("function sum(a, b) { return a + b; }");
            System.out.println(engine.eval("sum(1, 2);"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        // TODO: Integrate into and out of Java

        // NIO Files
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get("test.txt");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        // TODO: Learn more about streams

        // java.time
        Duration threeDays = Duration.ofDays(3);
        Instant now = Instant.now();
        LocalDate withoutTimezone = LocalDate.of(2014, Month.AUGUST, 23);
        LocalTime noTimezone = LocalTime.of(12, 15, 28, 340);
        LocalDateTime withoutZone = LocalDateTime.of(withoutTimezone, noTimezone);
        ZonedDateTime timezone = ZonedDateTime.of(withoutTimezone, noTimezone, ZoneId.of("Europe/Paris"));
        // TODO: Work with converting timezones

        // Date formatting
        String formattedDate = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(withoutTimezone);
        TemporalAccessor date = DateTimeFormatter.ofPattern("MM/dd/yyyy").parseBest(formattedDate);
        // TODO: Work with Temporal Accessor, Convert to date?

        // Enhanced Typing
        List<String> stringList = new ArrayList<>();
        stringList.add("A");
        stringList.addAll(Arrays.asList());  // This used to fail because asList returned a collection no objects

        // Reflection
        for(Method method :getClass().getDeclaredMethods()) {
            for(Parameter param : method.getParameters()) {
                System.out.format("Parameter Name: %s", param.getName());
            }
        }
        // TODO: Requires compilation with -parameters

        // Number Literals with underscores for readability
        int million = 1_000_000;

        // Strings in case statements
        switch("dog") {
            case "cat":
                throw new IllegalArgumentException("can't happen");

            case "dog":
                System.out.format("You got it!");
        }

        // No longer have to declare inferred types on creation
        Map<String, Object> testMap = new HashMap<>(); // The open/closed diamond will set it to the appropriate type
        // TODO: Play with using this in methods, instead of just declarations

        // Try with resources on Closeable or AutoCloseable interfaces
        Path outputFilePath = Paths.get("testFile.txt");
        // Open zip file and create output file with try-with-resources statement
        try (
                ZipFile zf = new ZipFile("testFile.zip");
                BufferedWriter writer = Files.newBufferedWriter(outputFilePath, charset)
        ) {
            for(Enumeration entries = zf.entries(); entries.hasMoreElements();) {
                String newLine = System.getProperty("line.separator");
                String zipEntryName = ((ZipEntry)entries.nextElement()).getName() + newLine;
                writer.write(zipEntryName, 0, zipEntryName.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: Create your own auto closeable resource

        // Exception catching/rethrowing
        try {
            createException();
        } catch (IllegalStateException | IllegalArgumentException inner) {
            // handling either
            System.err.format("Got an error: %s", inner.getMessage());
        }

        // Lambda and Streams
        List<String> names = new ArrayList<>();
        names.add("bob");
        names.add("frank");
        names.stream().filter(n -> n.length() > 3 && n.contains("a"))
                .map(n -> n.toUpperCase())
                .forEach(n -> System.out.println(n));

        names.stream().limit(1).filter(n -> !n.isEmpty()).collect(Collectors.counting());

        names.stream().flatMapToInt(n -> IntStream.of(n.length())).average().getAsDouble();
        names.stream().mapToInt(String::length).average().getAsDouble();
        // TODO: Get a better grasp of the double colon operator (method reference)
        // TODO: Understand steam options

        // Get or default method in collection utils
        Map<String, Object> map = new HashMap<>();
        Object foo = map.getOrDefault("dog", "cat");
        System.out.format("Foo: %s", foo);

        // Error handling within a stream
        List<String> urlString = Arrays.asList(
                "http://www.microsoft.com",
                "http://www.google.com",
                "http://www.amazon.com"
        );

        // Fails because the constructor for URL throws an exception
//        List<URL> urls = urlString.stream().map(URL::new).collect(Collectors.toList());
        // Succeeds because the compiler can't figure out that the exception is not a runtime exception
        List<URL> urls = urlString.stream().map(Silent.function(URL::new)).collect(Collectors.toList());
    }

    private void createException() throws IllegalStateException, IllegalArgumentException {
        try {
            throw new IllegalArgumentException("No arg!");
        } catch (Exception e) {
            // Note that even though we are throwing a type Exception, the compiler knows it's really either IllegalStateException or an IllegalArgumentException
            throw e;
        }
    }

    private static class Silent {
        @FunctionalInterface
        public static interface SilentFunction<T, R> {
            R apply(T t) throws Exception;
        }

        public static <T, R> Function<T, R> function(SilentFunction<T, R> silentFunction) {
            return o  -> {
                try {
                    return silentFunction.apply(o);
                } catch (Exception e) {
                    wrapException(e);
                    return null;
                }
            };
        }

        @SuppressWarnings("unchecked")
        private static <T extends Throwable> T wrapException(Throwable t) throws T {
            throw (T)t;
        }
    }
}
