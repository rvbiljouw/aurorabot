package ms.aurora.transform;


import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public final class TransformingClassLoader extends ClassLoader {
    private final Logger logger = Logger.getLogger(TransformingClassLoader.class);
    private final Map<String, byte[]> classMap = newHashMap();
    private final ClientDefinition clientDef;

    public TransformingClassLoader(ClientDefinition clientDef, JarFile file) throws IOException {
        super(TransformingClassLoader.class.getClassLoader());
        this.clientDef = clientDef;

        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                ByteArrayOutputStream classBytes = new ByteArrayOutputStream();
                InputStream classIn = file.getInputStream(entry);

                byte[] buffer = new byte[4096];
                while (classIn.available() > 0) {
                    int read = classIn.read(buffer, 0, buffer.length);

                    if (read < 0) {
                        break;
                    } else {
                        classBytes.write(buffer, 0, read);
                    }
                }
                classMap.put(entry.getName().replace(".class", ""),
                        classBytes.toByteArray());
            }
        }
    }

    private Class<?> defineSelf(String name, byte[] data) throws ClassFormatError {
        if (data != null && data.length > 0) {
            try {
                logger.debug("Defining class " + name);
                ByteArrayInputStream clazz = new ByteArrayInputStream(data);
                JavaClass javaClass = new ClassParser(clazz, name + ".class").parse();
                ClassGen classGen = new ClassGen(javaClass);
                logger.debug("Loaded class " + name + " into BCEL.");
                ClassDefinition definition = clientDef.lookup(name);
                if (definition != null) {
                    classGen.addInterface(definition.getIface());
                    logger.debug("Added interface " + definition.getIface() +
                            " to " + name);
                    for (AccessorDefinition accessor : definition.getAccessors()) {
                        accessor.inject(classGen);
                        logger.debug("Added getter to " + name);
                    }
                } else {
                    logger.debug("Couldn't find a class definition for " + name);
                }

                if (classGen.getSuperclassName().equals("java.awt.Canvas")) {
                    String canvasSuperclass = "ms/aurora/input/ClientCanvas";
                    ConstantPoolGen constantPool = classGen.getConstantPool();
                    constantPool.setConstant(classGen.getSuperclassNameIndex(),
                            new ConstantClass(constantPool.addUtf8(canvasSuperclass)));
                    logger.debug("Replaced superclass of " + classGen.getClassName() +
                            " with " + canvasSuperclass);
                }
                classGen.update();
                data = classGen.getJavaClass().getBytes();
            } catch (IOException e) {
                logger.error("Failed to load class due to I/O.", e);
            }
            return defineClass(name, data, 0, data.length);
        }

        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> result = defineSelf(name, classMap.get(name));
        if (result != null) {
            return result;
        }
        result = findLoadedClass(name);
        if (result != null) {
            return result;
        }
        result = getClass().getClassLoader().loadClass(name);
        return result;
    }
}
