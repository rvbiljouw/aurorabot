package ms.aurora.security;

import ms.aurora.api.random.impl.AutoLogin;
import ms.aurora.core.model.Account;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.gui.account.AccountOverview;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rvbiljouw
 */
public class DefaultSecurityManager extends SecurityManager {
    public static final Map<String, Class<?>[]> packages = new HashMap<String, Class<?>[]>();

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
        if (pkg.equals("ms.aurora.core.model")) {
            if (!isTrusted()) {
                throw new SecurityException("Access denied.");
            }
        }
    }

    public void checkPackageDefinition(String pkg) {

    }

    public void checkSetFactory() {

    }

    public void checkMemberAccess(Class<?> clazz, int which) {
        try {
            if (clazz == DefaultSecurityManager.class) {
                if (!isTrusted()) {
                    throw new SecurityException("Security violation! Respect my authoritay!");
                }
            }
        } catch (Exception e) {
            String callerName = getCallerClassName(4);
            if (!callerName.startsWith("java.lang") && !callerName.startsWith("sun.misc") &&
                    !callerName.startsWith("org.hibernate") && !callerName.startsWith("javassist"))
                throw new SecurityException("Source couldn't be obtained. " + clazz.getName() + " <= " + getCallerClassName(4));
        }
    }

    public void checkSecurityAccess(String target) {
    }

    protected String getCallerClassName(int callStackDepth) {
        return getClassContext()[callStackDepth].getName();
    }

    protected boolean isTrusted() {
        for (int i = 4; i < 6; i++) {
            Class<?> stack = getClassContext()[i];
            if (stack.getClassLoader() != getClass().getClassLoader() &&
                    !stack.getName().startsWith("java.") && !stack.getName().contains("zeroturnaround")) {
                System.out.println(getClassContext()[i].getName() + " is not trusted.");
                return false;
            }
        }
        return true;
    }

    static {
        packages.put("ms.aurora.core.model", new Class[]{AutoLogin.class, PluginManager.class,
                ScriptManager.class, PluginOverview.class, ScriptOverview.class,
                AccountOverview.class, ApplicationGUI.class, Account.class});
    }
}
