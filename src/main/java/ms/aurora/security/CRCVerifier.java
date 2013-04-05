package ms.aurora.security;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Verifier that uses CRC checksums of the entire client to determine
 * whether the version we're running has been tampered with
 *
 * @author rvbiljouw
 */
public class CRCVerifier {
    private static final boolean PRODUCTION = false;
    private final Map<String, Long> lastEdited = newHashMap();
    private final Map<String, Long> checksum = newHashMap();
    private final Map<String, Long> size = newHashMap();
    private final Map<String, Long> compressedSize = newHashMap();
    private final List<String> files = newArrayList();

    public CRCVerifier() {
        populate();
    }

    private void populate() {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            // The file should ALWAYS be in a jar when we verify it.
            if(!decodedPath.endsWith(".jar")) {
                // TODO: Send error code to server.
                System.exit(255);
                return;
            }

            JarFile file = new JarFile(decodedPath, true);
            Enumeration<JarEntry> entries = file.entries();
            while(entries.hasMoreElements()) {
                JarEntry current = entries.nextElement();
                files.add(current.getName());
                lastEdited.put(current.getName(), current.getTime());
                checksum.put(current.getName(), current.getCrc());
                size.put(current.getName(), current.getSize());
                compressedSize.put(current.getName(), current.getCompressedSize());
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 Encoding is not supported.");
        } catch (IOException e) {
            throw new RuntimeException("IO exception.", e);
        }
    }

}

