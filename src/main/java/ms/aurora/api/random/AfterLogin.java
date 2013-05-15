package ms.aurora.api.random;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to determine whether this random
 * should be processed before or after login.
 * @author rvbiljouw
 */
@Retention(RetentionPolicy.CLASS)
public @interface AfterLogin {
}
