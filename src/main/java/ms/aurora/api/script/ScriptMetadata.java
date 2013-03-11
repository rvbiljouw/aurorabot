package ms.aurora.api.script;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tobiewarburton
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptMetadata {
    public String name();

    public String[] contributors();

    public double version();

    public String description();

}
