package ms.aurora.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author tobiewarburton
 */
public class JarInputStreamClassLoader extends ClassLoader {
    private InputStream stream;
    private Map<String, byte[]> classByteMap = new HashMap<String, byte[]>();

    public JarInputStreamClassLoader(ClassLoader parentClassLoader, InputStream stream) {
        super(parentClassLoader);
        this.stream = stream;
        populate();
    }

    private void populate() {
        try {
            File tempFile = File.createTempFile("wut", "lefux");
            tempFile.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buffer = new byte[2048];
            int read;
            while ((read = stream.read(buffer, 0, 2048)) != -1) {
                fos.write(buffer, 0, read);
                fos.flush();
            }
            fos.close();

            JarFile file = new JarFile(tempFile);
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    byte[] classBytes = new byte[(int) entry.getSize()];
                    InputStream io = file.getInputStream(entry);
                    io.read(classBytes);
                    classByteMap.put(entry.getName().replaceAll("/", ".").replace(".class", ""), classBytes);
                    io.close();
                }
            }

            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            result = findLoadedClass(name);
            if (result != null) {
                return result;
            }
            if (classByteMap.containsKey(name)) {
                byte[] data = classByteMap.get(name);
                result = defineClass(name, data, 0, data.length);
                if (result != null) {
                    resolveClass(result);
                    return result;
                }
            }
            result = getClass().getClassLoader().loadClass(name);
            if (result != null) {
                return result;
            }
        } catch (ClassFormatError classFormatError) {
            classFormatError.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Loads a class based on it's manifest.
     * @param manifest
     * @param parent
     * @return
     */
    public Class<?> loadClassWithManifest(Class manifest, Class<?> parent) {
        try {
            for (String name : getClassNames()) {
                Class<?> clazz = loadClass(name);
                if (clazz.getAnnotation(manifest) != null) {
                    if (parent.isAssignableFrom(clazz)) {
                        return clazz;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<String> getClassNames() {
        return classByteMap.keySet();
    }
}