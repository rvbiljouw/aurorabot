package ms.aurora.core.random;

import ms.aurora.api.random.Random;
import ms.aurora.core.model.RandomSource;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Rick
 */
public final class RandomLoader {

    private static final Logger logger = Logger.getLogger(RandomLoader.class);

    private RandomLoader() {
    }

    public static List<Random> getRandoms() {
        List<Random> randoms = new ArrayList<Random>();
        for (RandomSource sourceObj : RandomSource.getAll()) {
            String source = sourceObj.getSource();
            File sourceDirectory = new File(source);
            if (!sourceDirectory.exists()) {
                sourceDirectory.mkdirs();
            }
            for (File file : sourceDirectory.listFiles()) {
                if (!file.getName().endsWith(".jar")) continue;
                try {
                    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{
                            new URL("file:" + file.getAbsolutePath())}, Thread.currentThread().getContextClassLoader());
                    JarFile jarFile = new JarFile(file);

                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry current = entries.nextElement();
                        String fileName = current.getName();
                        if (!current.getName().endsWith(".class")) continue;

                        Random random = loadRandom(jarClassLoader, formatClassName(fileName));
                        if (random != null) {
                            randoms.add(random);
                        }
                    }
                } catch (IOException e) {
                    logger.debug("Failed to load Random from JAR " + file.getName(), e);
                }
            }
        }
        return randoms;
    }

    private static Random loadRandom(ClassLoader classLoader, String className) {
        try {
            Class<?> randomClass = classLoader.loadClass(className);
            if (Random.class.isAssignableFrom(randomClass)) {
                Random random = (Random) randomClass.newInstance();
                if (!random.validate()) {
                    throw new IOException("Random does not carry a RandomManifest. ( " + className + " )");
                }
                return random;
            }
        } catch (ReflectiveOperationException e) {
            logger.debug("Malformed class: " + className, e);
        } catch (Exception e) {
            logger.error("Failed to load random", e);
        }
        return null;
    }

    private static String formatClassName(String fileName) {
        return fileName.replaceAll("/", "\\.").replace(".class", "");
    }

    static {
        if (RandomSource.getBySource("./randoms/").size() == 0) {
            RandomSource local = new RandomSource("./randoms/", false);
            local.save();
        }
    }
}
