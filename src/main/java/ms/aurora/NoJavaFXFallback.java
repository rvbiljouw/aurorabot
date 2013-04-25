package ms.aurora;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class NoJavaFXFallback extends JApplet implements ActionListener {

   boolean isInBrowser = false;
   boolean oldJRE = true;
   String requiredJavaFXVersion = null;
   boolean oldJavaFX = false;
   boolean doNotUseJNLPAPI = false;


   public NoJavaFXFallback() {}

   public NoJavaFXFallback(boolean needJREUpgrade, boolean needFXUpgrade, String requiredJavaFX) {
      this.isInBrowser = false;
      this.oldJavaFX = needFXUpgrade;
      this.requiredJavaFXVersion = requiredJavaFX;
      this.oldJRE = needJREUpgrade;
      this.doNotUseJNLPAPI = true;
      this.populate();
   }

   private static float getJavaVersionAsFloat() {
      String versionString = System.getProperty("java.version", "1.5.0");
      StringBuffer sb = new StringBuffer();
      int firstDot = versionString.indexOf(".");
      sb.append(versionString.substring(0, firstDot));
      int secondDot = versionString.indexOf(".", firstDot + 1);
      sb.append(versionString.substring(firstDot + 1, secondDot));
      int underscore = versionString.indexOf("_", secondDot + 1);
      int version;
      if(underscore >= 0) {
         version = versionString.indexOf("-", underscore + 1);
         if(version < 0) {
            version = versionString.length();
         }

         sb.append(versionString.substring(secondDot + 1, underscore)).append(".").append(versionString.substring(underscore + 1, version));
      } else {
         version = versionString.indexOf("-", secondDot + 1);
         if(version < 0) {
            version = versionString.length();
         }

         sb.append(versionString.substring(secondDot + 1, version));
      }

      float version1 = 150.0F;

      try {
         version1 = Float.parseFloat(sb.toString());
      } catch (NumberFormatException var7) {
         ;
      }

      return version1;
   }

   private void test() {
      this.oldJRE = getJavaVersionAsFloat() < 160.18F;

      try {
         Class e = Class.forName("netscape.javascript.JSObject");
         Method m = e.getMethod("getWindow", new Class[]{Applet.class});
         this.isInBrowser = m.invoke((Object)null, new Object[]{this}) != null;
      } catch (Exception var3) {
         ;
      }

   }

   String getText() {
      String text = "This application requires a newer version of the Java runtime. Please download and install the latest Java runtime from java.com.";
      if(this.isInBrowser) {
         text = text + " Then restart the browser.";
      } else {
         text = text + " Then restart the application.";
      }

      return text;
   }

   public void init() {
      this.requiredJavaFXVersion = this.getParameter("requiredFXVersion");
      this.test();
      this.populate();
   }

   private void populate() {
      Container pane = this.getContentPane();
      pane.setLayout(new BorderLayout());
      JTextPane l = new JTextPane();
      l.setText(this.getText());
      l.setEditable(false);
      pane.add(l, "Center");
      if(getJavaVersionAsFloat() > 160.0F || getJavaVersionAsFloat() > 150.0F && !this.doNotUseJNLPAPI) {
         JButton installButton = new JButton("Install Now");
         installButton.addActionListener(this);
         pane.add(installButton, "South");
      }

   }

   public void actionPerformed(ActionEvent ae) {
      try {
         URL e = new URL("http://java.com/");
         if(this.isInBrowser) {
            this.getAppletContext().showDocument(e);
         } else if(!this.doNotUseJNLPAPI) {
            Class d = Class.forName("javax.jnlp.ServiceManager");
            Class bs = Class.forName("javax.jnlp.BasicService");
            Method lookup = d.getMethod("lookup", new Class[]{String.class});
            Method showDoc = bs.getMethod("showDocument", new Class[]{URL.class});
            Object s = lookup.invoke((Object)null, new Object[]{"javax.jnlp.BasicService"});
            showDoc.invoke(s, new Object[]{e});
         } else {
            Desktop d1 = Desktop.getDesktop();
            if(d1 != null) {
               d1.browse(e.toURI());
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
