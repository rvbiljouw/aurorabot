package ms.aurora.api.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author rvbiljouw
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

    public String name();

    public String author() default "Author unknown.";

    public String shortDescription() default "No description available.";

    public String longDescription() default "No description available.";

    public double version();

}
