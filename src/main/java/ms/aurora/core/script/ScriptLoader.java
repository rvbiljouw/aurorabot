package ms.aurora.core.script;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A quick and dirty local script loader..
 *
 * @author rvbiljouw
 */
public final class ScriptLoader {
    private final static Logger logger = Logger.getLogger(ScriptLoader.class);



    private static final FilenameFilter CLASS_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".class");
        }
    };


    public static class Source {

    }
}
