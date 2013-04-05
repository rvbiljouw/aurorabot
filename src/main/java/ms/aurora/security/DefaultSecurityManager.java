package ms.aurora.security;

import ms.aurora.api.random.impl.AutoLogin;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.gui.account.AccountOverview;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class DefaultSecurityManager extends SecurityManager {
    public static final Map<String, Class<?>[]> packages = newHashMap();

    public void checkConnect(String host, int port, Object context) {
    }

    public void checkConnect(String host, int port) {
    }

    public void checkPermission(Permission perm) {
    }

    public void checkPermission(Permission perm, Object context) {
    }

    public void checkCreateClassLoader() {
    }

    public void checkAccess(Thread t) {

    }

    public void checkAccess(ThreadGroup g) {

    }

    public void checkExit(int status) {

    }

    public void checkExec(String cmd) {

    }

    public void checkLink(String lib) {

    }

    public void checkRead(FileDescriptor fd) {

    }

    public void checkRead(String file) {

    }

    public void checkRead(String file, Object context) {

    }

    public void checkWrite(FileDescriptor fd) {

    }

    public void checkWrite(String file) {

    }

    public void checkDelete(String file) {

    }

    public void checkListen(int port) {

    }

    public void checkAccept(String host, int port) {

    }

    public void checkMulticast(InetAddress maddr) {

    }

    public void checkPropertiesAccess() {

    }

    public void checkPropertyAccess(String key) {

    }

    public void checkPrintJobAccess() {

    }

    public void checkSystemClipboardAccess() {

    }

    public void checkAwtEventQueueAccess() {

    }

    public void checkPackageAccess(String pkg) {
        try {
            if (pkg.equals("ms.aurora.core.model")) {
                Class<?> source = Class.forName(getCallerClassName(4));
                Class<?>[] allowed = packages.get(pkg);
                if (allowed != null) {
                    boolean permit = false;
                    for (Class<?> clazz : allowed) {
                        if (clazz == source) permit = true;
                    }

                    if (!permit && !source.getName().startsWith("java.lang") && !source.getName().startsWith("sun.misc")) {
                        throw new SecurityException("Accessing this package is not allowed from " + source.getName());
                    } else {
                        System.out.println("Granted: " + source.getName());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SecurityException("Couldn't validate your classfile, so we can't grant you access to " + pkg);
        }
    }

    public void checkPackageDefinition(String pkg) {

    }

    public void checkSetFactory() {

    }

    public void checkMemberAccess(Class<?> clazz, int which) {
        if (clazz == DefaultSecurityManager.class) {
            String callerName = getCallerClassName(4);
            try {
                Class<?> caller = Class.forName(callerName);
                if (caller != DefaultSecurityManager.class) {
                    throw new SecurityException("Security violation! Respect my authoritay!");
                }
            } catch (ClassNotFoundException e) {
                throw new SecurityException("Couldn't validate your classfile, so we can't grant you access.");
            }
        }
    }

    public void checkSecurityAccess(String target) {

    }

    protected String getCallerClassName(int callStackDepth) {
        return getClassContext()[callStackDepth].getName();
    }

    static {
        packages.put("ms.aurora.core.model", new Class[]{AutoLogin.class, PluginManager.class,
                ScriptManager.class, PluginOverview.class, ScriptOverview.class, AccountOverview.class});
    }
}
