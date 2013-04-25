package ms.aurora;

import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Bootstrap {
    private static final String BOOTSTRAP_NAME = "ms.aurora.Bootstrap";
    private static boolean verbose = false;
    private static Attributes attrs = null;

    private static URL fileToURL(File file) throws IOException {
        return file.getCanonicalFile().toURI().toURL();
    }

    private static Method findLaunchMethod(File jfxRtPath, String fxClassPath) {
        Class[] argTypes = new Class[]{Class.class, Class.class, (new String[0]).getClass()};

        try {
            ArrayList ex = new ArrayList();
            String cp = System.getProperty("java.class.path");
            int urls;
            String urlClassLoader;
            if (cp != null) {
                for (; cp.length() > 0; cp = cp.substring(urls + 1)) {
                    urls = cp.indexOf(File.pathSeparatorChar);
                    if (urls < 0) {
                        ex.add(fileToURL(new File(cp)));
                        break;
                    }

                    if (urls > 0) {
                        urlClassLoader = cp.substring(0, urls);
                        ex.add(fileToURL(new File(urlClassLoader)));
                    }
                }
            }

            cp = fxClassPath;
            File var11;
            int var12;
            if (fxClassPath != null) {
                var11 = null;

                String launchClass;
                try {
                    urlClassLoader = ms.aurora.Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    launchClass = URLDecoder.decode(urlClassLoader, "UTF-8");
                    var11 = (new File(launchClass)).getParentFile();
                    if (!var11.exists()) {
                        var11 = null;
                    }
                } catch (Exception var9) {
                    ;
                }

                for (; cp.length() > 0; cp = cp.substring(var12 + 1)) {
                    var12 = cp.indexOf(" ");
                    File m;
                    if (var12 < 0) {
                        m = var11 == null ? new File(cp) : new File(var11, cp);
                        ex.add(fileToURL(m));
                        break;
                    }

                    if (var12 > 0) {
                        launchClass = cp.substring(0, var12);
                        m = var11 == null ? new File(launchClass) : new File(var11, launchClass);
                        ex.add(fileToURL(m));
                    }
                }
            }

            if (jfxRtPath != null) {
                var11 = new File(jfxRtPath, "lib");
                ex.add(fileToURL(new File(var11, "jfxrt.jar")));
                ex.add(fileToURL(new File(var11, "deploy.jar")));
                ex.add(fileToURL(new File(var11, "plugin.jar")));
                ex.add(fileToURL(new File(var11, "javaws.jar")));
            }

            URL[] var13 = (URL[]) ((URL[]) ex.toArray(new URL[0]));
            if (verbose) {
                System.err.println("===== URL list");

                for (var12 = 0; var12 < var13.length; ++var12) {
                    System.err.println("" + var13[var12]);
                }

                System.err.println("=====");
            }

            URLClassLoader var14 = new URLClassLoader(var13, (ClassLoader) null);
            Class var15 = Class.forName("com.sun.javafx.application.LauncherImpl", true, var14);
            Method var16 = var15.getMethod("launchApplication", argTypes);
            if (var16 != null) {
                Thread.currentThread().setContextClassLoader(var14);
                return var16;
            }
        } catch (Exception var10) {
            if (jfxRtPath != null) {
                var10.printStackTrace();
            }
        }

        return null;
    }

    private static Method findLaunchMethodInClasspath(String fxClassPath) {
        return findLaunchMethod((File) null, fxClassPath);
    }

    private static Method findLaunchMethodInJar(String jfxRtPathName, String fxClassPath) {
        File jfxRtPath = new File(jfxRtPathName);
        File jfxRtLibPath = new File(jfxRtPath, "lib");
        File jfxRtJar = new File(jfxRtLibPath, "jfxrt.jar");
        if (!jfxRtJar.canRead()) {
            if (verbose) {
                System.err.println("Unable to read " + jfxRtJar.toString());
            }

            return null;
        } else {
            return findLaunchMethod(jfxRtPath, fxClassPath);
        }
    }

    private static int[] convertVersionStringtoArray(String version) {
        int[] v = new int[3];
        if (version == null) {
            return null;
        } else {
            String[] s = version.split("\\.");
            if (s.length == 3) {
                v[0] = Integer.parseInt(s[0]);
                v[1] = Integer.parseInt(s[1]);
                v[2] = Integer.parseInt(s[2]);
                return v;
            } else {
                return null;
            }
        }
    }

    private static int compareVersionArray(int[] a1, int[] a2) {
        boolean isValid1 = a1 != null && a1.length == 3;
        boolean isValid2 = a2 != null && a2.length == 3;
        if (!isValid1 && !isValid2) {
            return 0;
        } else if (!isValid2) {
            return -1;
        } else if (!isValid1) {
            return 1;
        } else {
            for (int i = 0; i < a1.length; ++i) {
                if (a2[i] > a1[i]) {
                    return 1;
                }

                if (a2[i] < a1[i]) {
                    return -1;
                }
            }

            return 0;
        }
    }

    private static String lookupRegistry() {
        if (!System.getProperty("os.name").startsWith("Win")) {
            return null;
        } else {
            String javaHome = System.getProperty("java.home");
            if (verbose) {
                System.err.println("java.home = " + javaHome);
            }

            if (javaHome != null && !javaHome.equals("")) {
                try {
                    File ex = new File(javaHome, "lib");
                    File deployJar = new File(ex, "deploy.jar");
                    URL[] urls = new URL[]{fileToURL(deployJar)};
                    if (verbose) {
                        System.err.println(">>>> URL to deploy.jar = " + urls[0]);
                    }

                    URLClassLoader deployClassLoader = new URLClassLoader(urls, (ClassLoader) null);

                    String winRegistryWrapperClassName;
                    Class winRegistryWrapperClass;
                    Method mGetSubKeys;
                    try {
                        winRegistryWrapperClassName = "com.sun.deploy.config.Config";
                        winRegistryWrapperClass = Class.forName(winRegistryWrapperClassName, true, deployClassLoader);
                        mGetSubKeys = winRegistryWrapperClass.getMethod("getInstance", (Class[]) null);
                        Object HKEY_LOCAL_MACHINE_Field2 = mGetSubKeys.invoke((Object) null, (Object[]) null);
                        mGetSubKeys = winRegistryWrapperClass.getMethod("loadDeployNativeLib", (Class[]) null);
                        mGetSubKeys.invoke(HKEY_LOCAL_MACHINE_Field2, (Object[]) null);
                    } catch (Exception var19) {
                        ;
                    }

                    winRegistryWrapperClassName = "com.sun.deploy.association.utility.WinRegistryWrapper";
                    winRegistryWrapperClass = Class.forName(winRegistryWrapperClassName, true, deployClassLoader);
                    mGetSubKeys = winRegistryWrapperClass.getMethod("WinRegGetSubKeys", new Class[]{Integer.TYPE, String.class, Integer.TYPE});
                    Field var21 = winRegistryWrapperClass.getField("HKEY_LOCAL_MACHINE");
                    int HKEY_LOCAL_MACHINE2 = var21.getInt((Object) null);
                    String registryKey = "Software\\Oracle\\JavaFX\\";
                    String[] fxVersions = (String[]) ((String[]) mGetSubKeys.invoke((Object) null, new Object[]{new Integer(HKEY_LOCAL_MACHINE2), "Software\\Oracle\\JavaFX\\", new Integer(255)}));
                    if (fxVersions == null) {
                        return null;
                    } else {
                        String version = "0.0.0";

                        for (int winRegistryClassName = 0; winRegistryClassName < fxVersions.length; ++winRegistryClassName) {
                            if (fxVersions[winRegistryClassName].startsWith("2.") && fxVersions[winRegistryClassName].compareTo("2.1.0") >= 0) {
                                int[] winRegistryClass = convertVersionStringtoArray(version);
                                int[] mGet = convertVersionStringtoArray(fxVersions[winRegistryClassName]);
                                if (compareVersionArray(winRegistryClass, mGet) > 0) {
                                    version = fxVersions[winRegistryClassName];
                                }
                            } else if (verbose) {
                                System.err.println("  Skip version " + fxVersions[winRegistryClassName] + " (required=" + "2.1.0" + ")");
                            }
                        }

                        if (version.equals("0.0.0")) {
                            return null;
                        } else {
                            String var22 = "com.sun.deploy.util.WinRegistry";
                            Class var24 = Class.forName(var22, true, deployClassLoader);
                            Method var23 = var24.getMethod("getString", new Class[]{Integer.TYPE, String.class, String.class});
                            Field HKEY_LOCAL_MACHINE_Field = var24.getField("HKEY_LOCAL_MACHINE");
                            int HKEY_LOCAL_MACHINE = HKEY_LOCAL_MACHINE_Field.getInt((Object) null);
                            String path = (String) var23.invoke((Object) null, new Object[]{new Integer(HKEY_LOCAL_MACHINE), "Software\\Oracle\\JavaFX\\" + version, "Path"});
                            if (verbose) {
                                System.err.println("FOUND KEY: Software\\Oracle\\JavaFX\\" + version + " = " + path);
                            }

                            return path;
                        }
                    }
                } catch (Exception var20) {
                    var20.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static Attributes getJarAttributes() throws Exception {
        String theClassFile = "Bootstrap.class";
        Class theClass = ms.aurora.Bootstrap.class;
        String classUrlString = theClass.getResource(theClassFile).toString();
        if (classUrlString.startsWith("jar:file:") && classUrlString.contains("!")) {
            String urlString = classUrlString.substring(4, classUrlString.lastIndexOf("!"));
            File jarFile = new File((new URI(urlString)).getPath());
            String jarName = jarFile.getCanonicalPath();
            JarFile jf = null;

            Attributes attr;
            try {
                jf = new JarFile(jarName);
                Manifest ex = jf.getManifest();
                attr = ex.getMainAttributes();
            } finally {
                if (jf != null) {
                    try {
                        jf.close();
                    } catch (Exception var14) {
                        ;
                    }
                }

            }

            return attr;
        } else {
            System.out.println(classUrlString);
            return null;
        }
    }

    private static String decodeBase64(String inp) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = decoder.decodeBuffer(inp);
        return new String(decodedBytes);
    }

    private static String[] getAppArguments(Attributes attrs) {
        LinkedList args = new LinkedList();

        try {
            int ioe = 1;
            // rvbiljouw: Basic fix for non-attributed shit
            if (attrs == null) return new String[0];


            for (String argNamePrefix = "JavaFX-Argument-"; attrs.getValue(argNamePrefix + ioe) != null; ++ioe) {
                args.add(decodeBase64(attrs.getValue(argNamePrefix + ioe)));
            }

            String paramNamePrefix = "JavaFX-Parameter-Name-";
            String paramValuePrefix = "JavaFX-Parameter-Value-";

            for (ioe = 1; attrs.getValue(paramNamePrefix + ioe) != null; ++ioe) {
                String k = decodeBase64(attrs.getValue(paramNamePrefix + ioe));
                String v = null;
                if (attrs.getValue(paramValuePrefix + ioe) != null) {
                    v = decodeBase64(attrs.getValue(paramValuePrefix + ioe));
                }

                args.add("--" + k + "=" + (v != null ? v : ""));
            }
        } catch (IOException var8) {
            System.err.println("Failed to extract application parameters");
            var8.printStackTrace();
        }

        return (String[]) ((String[]) args.toArray(new String[0]));
    }

    private static String getAppName(Attributes attrs, boolean preloader) {
        String propName = preloader ? "javafx.preloader.class" : "javafx.application.class";
        String className = System.getProperty(propName);
        if (className != null && className.length() != 0) {
            return className;
        } else if (attrs == null) {
            return "TEST";
        } else {
            String appName;
            if (preloader) {
                appName = attrs.getValue("JavaFX-Preloader-Class");
                if (appName != null && appName.length() != 0) {
                    return appName;
                } else {
                    if (verbose) {
                        System.err.println("Unable to find preloader class name");
                    }

                    return null;
                }
            } else {
                appName = attrs.getValue("JavaFX-Application-Class");
                if (appName != null && appName.length() != 0) {
                    return appName;
                } else {
                    System.err.println("Unable to find application class name");
                    return null;
                }
            }
        }
    }

    private static Class getAppClass(String appName) {
        try {
            if (verbose) {
                System.err.println("Try calling Class.forName(" + appName + ") using classLoader = " + Thread.currentThread().getContextClassLoader());
            }

            Class cnfe = Class.forName(appName, false, Thread.currentThread().getContextClassLoader());
            if (verbose) {
                System.err.println("found class: " + cnfe);
            }

            return cnfe;
        } catch (NoClassDefFoundError var2) {
            var2.printStackTrace();
            errorExit("Unable to find class: " + appName);
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            errorExit("Unable to find class: " + appName);
        }

        return null;
    }

    private static void tryToSetProxy() {
        try {
            if (System.getProperty("http.proxyHost") != null || System.getProperty("https.proxyHost") != null || System.getProperty("ftp.proxyHost") != null) {
                if (verbose) {
                    System.out.println("Explicit proxy settings detected. Skip autoconfig.");
                    System.out.println("  http.proxyHost=" + System.getProperty("http.proxyHost"));
                    System.out.println("  https.proxyHost=" + System.getProperty("https.proxyHost"));
                    System.out.println("  ftp.proxyHost=" + System.getProperty("ftp.proxyHost"));
                }

                return;
            }

            if (System.getProperty("javafx.autoproxy.disable") != null) {
                if (verbose) {
                    System.out.println("Disable autoproxy on request.");
                }

                return;
            }

            Class e = Class.forName("com.sun.deploy.services.ServiceManager", true, Thread.currentThread().getContextClassLoader());
            Class[] params = new Class[]{Integer.TYPE};
            Method setservice = e.getDeclaredMethod("setService", params);
            String osname = System.getProperty("os.name");
            String servicename = null;
            if (osname.startsWith("Win")) {
                servicename = "STANDALONE_TIGER_WIN32";
            } else if (osname.contains("Mac")) {
                servicename = "STANDALONE_TIGER_MACOSX";
            } else {
                servicename = "STANDALONE_TIGER_UNIX";
            }

            Object[] values = new Object[1];
            Class pt = Class.forName("com.sun.deploy.services.PlatformType", true, Thread.currentThread().getContextClassLoader());
            values[0] = pt.getField(servicename).get((Object) null);
            setservice.invoke((Object) null, values);
            Class dps = Class.forName("com.sun.deploy.net.proxy.DeployProxySelector", true, Thread.currentThread().getContextClassLoader());
            Method m = dps.getDeclaredMethod("reset", new Class[0]);
            m.invoke((Object) null, new Object[0]);
            if (verbose) {
                System.out.println("Autoconfig of proxy is completed.");
            }
        } catch (Exception var9) {
            if (verbose) {
                System.out.println("Failed to autoconfig proxy due to " + var9);
            }
        }

    }

    private static void processUpdateHook(String updateHookName) {
        if (updateHookName != null) {
            try {
                if (verbose) {
                    System.err.println("Try calling Class.forName(" + updateHookName + ") using classLoader = " + Thread.currentThread().getContextClassLoader());
                }

                Class ex = Class.forName(updateHookName, false, Thread.currentThread().getContextClassLoader());
                if (verbose) {
                    System.err.println("found class: " + ex.getCanonicalName());
                }

                Method mainMethod = ex.getMethod("main", new Class[]{(new String[0]).getClass()});
                Object args = null;
                mainMethod.invoke((Object) null, new Object[]{args});
            } catch (Exception var4) {
                if (verbose) {
                    System.err.println("Failed to run update hook: " + var4.getMessage());
                    var4.printStackTrace();
                }
            }

        }
    }

    private static void launchApp(Method launchMethod, String appName, String preloaderName, String updateHookName, String[] args) {
        Class preloaderClass = null;
        if (preloaderName != null) {
            preloaderClass = getAppClass(preloaderName);
        }

        Class appClass = getAppClass(appName);
        Class fxApplicationClass = null;

        try {
            fxApplicationClass = Class.forName("javafx.application.Application", true, Thread.currentThread().getContextClassLoader());
        } catch (NoClassDefFoundError var12) {
            errorExit("Cannot find javafx.application.Application");
        } catch (ClassNotFoundException var13) {
            errorExit("Cannot find javafx.application.Application");
        }

        try {
            if (verbose) {
                System.err.println("Try calling " + appClass.getName() + ".main(String[])");
            }

            Method ex = appClass.getMethod("main", new Class[]{(new String[0]).getClass()});
            ex.invoke((Object) null, new Object[]{args});
        } catch (Exception var9) {
            var9.printStackTrace();
            errorExit("Unable to invoke main method");
        }


    }

    private static void checkJre() {
        String javaVersion = System.getProperty("java.version");
        if (verbose) {
            System.err.println("java.version = " + javaVersion);
            System.err.println("java.runtime.version = " + System.getProperty("java.runtime.version"));
        }

        if (!javaVersion.startsWith("1.6") && !javaVersion.startsWith("1.7") && !javaVersion.startsWith("1.8") && !javaVersion.startsWith("1.9")) {
            showFallback(true);
        }

    }

    private static Method findLaunchMethod(String fxClassPath) {
        Method launchMethod = null;
        if (verbose) {
            System.err.println("1) Try existing classpath...");
        }

        launchMethod = findLaunchMethodInClasspath(fxClassPath);
        if (launchMethod != null) {
            return launchMethod;
        } else {
            if (verbose) {
                System.err.println("2) Try javafx.runtime.path property...");
            }

            String javafxRuntimePath = System.getProperty("javafx.runtime.path");
            if (javafxRuntimePath != null) {
                if (verbose) {
                    System.err.println("    javafx.runtime.path = " + javafxRuntimePath);
                }

                launchMethod = findLaunchMethodInJar(javafxRuntimePath, fxClassPath);
            }

            if (launchMethod != null) {
                return launchMethod;
            } else {
                if (verbose) {
                    System.err.println("3) Look for cobundled JavaFX ... [java.home=" + System.getProperty("java.home"));
                }

                launchMethod = findLaunchMethodInJar(System.getProperty("java.home"), fxClassPath);
                if (launchMethod != null) {
                    return launchMethod;
                } else {
                    if (verbose) {
                        System.err.println("4) Look in the OS platform registry...");
                    }

                    javafxRuntimePath = lookupRegistry();
                    if (javafxRuntimePath != null) {
                        if (verbose) {
                            System.err.println("    Installed JavaFX runtime found in: " + javafxRuntimePath);
                        }

                        launchMethod = findLaunchMethodInJar(javafxRuntimePath, fxClassPath);
                        if (launchMethod != null) {
                            return launchMethod;
                        }
                    }

                    if (verbose) {
                        System.err.println("5) Look in hardcoded paths");
                    }

                    String[] hardCodedPaths = new String[]{"../rt", "../../../../rt", "../../sdk/rt", "../../../artifacts/sdk/rt"};

                    for (int i = 0; i < hardCodedPaths.length; ++i) {
                        javafxRuntimePath = hardCodedPaths[i];
                        launchMethod = findLaunchMethodInJar(javafxRuntimePath, fxClassPath);
                        if (launchMethod != null) {
                            return launchMethod;
                        }
                    }

                    return launchMethod;
                }
            }
        }
    }

    public static void main(String[] args) {
        verbose = Boolean.getBoolean("javafx.verbose");
        checkJre();
        try {
            attrs = getJarAttributes();
        } catch (Exception var7) {
            var7.printStackTrace();
            errorExit("Unable to load jar manifest");
        }

        String appName = getAppName(attrs, false);
        if (verbose) {
            System.err.println("appName = " + appName);
        }

        if (appName == null) {
            errorExit("Unable to find application class name");
        }

        String preloaderName = getAppName(attrs, true);
        if (verbose) {
            System.err.println("preloaderName = " + preloaderName);
        }

        String[] embeddedArgs = getAppArguments(attrs);
        if (verbose) {
            System.err.println("embeddedArgs = " + Arrays.toString(embeddedArgs));
            System.err.println("commandLineArgs = " + Arrays.toString(args));
        }

        String updateHook = attrs.getValue("X-JavaFX-Update-Hook");
        if (verbose && updateHook != null) {
            System.err.println("updateHook = " + updateHook);
        }

        String fxClassPath;
        if (attrs != null) {
            fxClassPath = attrs.getValue("JavaFX-Class-Path");
        } else {
            fxClassPath = "";
        }

        Method launchMethod = findLaunchMethod(fxClassPath);
        if (launchMethod != null) {
            launchApp(launchMethod, appName, preloaderName, updateHook, args.length > 0 ? args : embeddedArgs);
        } else {
            showFallback(false);
        }
    }

    private static void showFallback(boolean jreError) {
        if (jreError) {
            // Rvbiljouw: This couldn't be reverse engineered, so lets just make our own?
            JFrame frame = new JFrame("Woops!");
            NoJavaFXFallback fallback = new NoJavaFXFallback(true, true, "2.1+");
            frame.add(fallback);
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
    }

    private static void errorExit(String msg) {
        // rvbiljouw: Again failed to reverse engineer this one, so we're writing our own
        JOptionPane.showConfirmDialog(null, msg);
        System.exit(255);
    }

}
