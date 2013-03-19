package ms.aurora.api.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author rvbiljouw
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginManifest {

    String name();

    String author() default "Author unknown.";

    String shortDescription() default "No description available.";

    String longDescription() default "No description available.";

    double version();

}
