package utils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainRunner {

    private final String className;
    private final Class<?> reloadClazz;
    private final List<URL> extraUrls;

    public MainRunner(String className, Class<?> reloadClazz, List<URL> extraUrls) {
        this.className = className;
        this.reloadClazz = reloadClazz;
        this.extraUrls = extraUrls;
    }

    public ExecutionResult runJavaClass(String input, String... args) throws IOException {

        InputStream origIn = System.in;
        PrintStream origOut = System.out;
        PrintStream origErr = System.err;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        // https://stackoverflow.com/a/20094513/854540
        ArrayList<URL> allUrls = new ArrayList<>(extraUrls);
        allUrls.add(reloadClazz.getProtectionDomain().getCodeSource().getLocation());
        URL[] urls = allUrls.toArray(new URL[0]);

        try (URLClassLoader classLoader = new URLClassLoader(urls, reloadClazz.getClassLoader().getParent())){

            // https://stackoverflow.com/a/1119559/854540
            System.setIn(in);
            System.setOut(new PrintStream(out));
            System.setErr(new PrintStream(err));

            Class<?> clazz = classLoader.loadClass(className);

            Method mainMethod;
            Object[] invokeArgs;
            try {
                mainMethod = getMainMethod(clazz, String[].class);
                invokeArgs = new Object[]{args};
            }
            catch (NoSuchMethodException _) {
                mainMethod = getMainMethod(clazz);
                invokeArgs = new Object[0];
            }

            Object invokeObj;
            if (Modifier.isStatic(mainMethod.getModifiers())) {
                invokeObj = null;
            }
            else {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                invokeObj = constructor.newInstance();
            }

            mainMethod.setAccessible(true);
            mainMethod.invoke(invokeObj, invokeArgs);

            return new ExecutionResult(out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e); // avoid changing all throws signatures although ideally should
        }
        catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace(); // ugh
            return new ExecutionResult(out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
        }
        finally {
            System.setIn(origIn);
            System.setOut(origOut);
            System.setErr(origErr);
        }
    }
    
    private Method getMainMethod(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        while (clazz != null) {
            try {
                return clazz.getDeclaredMethod("main", parameterTypes);
            }
            catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }

    public record ExecutionResult(String out, String err) {
    }
}
