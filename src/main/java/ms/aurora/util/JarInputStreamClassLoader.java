package ms.aurora.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * @author tobiewarburton
 */
public class JarInputStreamClassLoader extends ClassLoader {
    private List<JarInputStream> streams;

    private Map<String, byte[]> classByteMap = new HashMap<String, byte[]>();

    public JarInputStreamClassLoader(ClassLoader parentClassLoader, List<JarInputStream> streams) {
        super(parentClassLoader);
        this.streams = streams;

        populate();
    }

    private void populate() {
        try {
            for (JarInputStream stream : streams) {
                ZipEntry current = null;

                while ((current = stream.getNextEntry()) != null) {
                    if (current.getName().endsWith(".class")) {
                        byte[] b = new byte[2048];
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        int len;
                        while ((len = stream.read(b)) > 0) {
                            out.write(b, 0, len);
                        }
                        byte[] bs = out.toByteArray();
                        System.out.println(current.getName() + " len=" + bs.length);
                        classByteMap.put(current.getName().replaceAll("/", "\\.").replace(".class", ""), bs);
                    }
                }
            }
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
            classFormatError.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

    public Collection<String> getClassNames() {
        return classByteMap.keySet();
    }
}
