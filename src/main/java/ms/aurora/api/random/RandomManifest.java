package ms.aurora.api.random;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rick
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomManifest {

    String name();

    String author() default "Team Aurora";

    double version();
}
