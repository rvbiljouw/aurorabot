package ms.aurora.api.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rick
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

    String name();

    String author() default "Author unknown.";

    String shortDescription() default "No description available.";

    String longDescription() default "No description available.";

    double version();

    String category() default "Unknown";
}
