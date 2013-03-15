package ms.aurora.api.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tobiewarburton
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptMetadata {
    public String name();

    public String[] contributors() default {};

    public double version() default 1.0;

    public String description() default "";

}
