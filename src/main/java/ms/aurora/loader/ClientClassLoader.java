package ms.aurora.loader;

import flexjson.JSONDeserializer;
import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.sdn.net.encode.Base64;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads the client from a remote JSON file.
 * @author Rick
 * @author tobiewarburton
 */
public final class ClientClassLoader extends ClassLoader {
    private final Logger logger = Logger.getLogger(ClientClassLoader.class);
    private final Map<String, byte[]> classMap = new HashMap<String, byte[]>();

    public ClientClassLoader(Plaintext plaintext) throws IOException {
        super(ClientClassLoader.class.getClassLoader());
        JSONDeserializer<Map<String, String>> deserializer = new JSONDeserializer<Map<String, String>>();
        Map<String, String> nameBaseMap = deserializer.deserialize(plaintext.getText());
        for (Map.Entry<String, String> entry : nameBaseMap.entrySet()) {
            classMap.put(entry.getKey(), Base64.decode(entry.getValue()));
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
