package ms.aurora.api.wrappers;

import ms.aurora.rt3.Model;
import ms.aurora.rt3.Renderable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Rick
 */
public class RSRenderable {
    private final Renderable wrapped;

    public RSRenderable(Renderable wrapped) {
        this.wrapped = wrapped;
    }

    public final int getHeight() {
        return -(wrapped.getHeight() / 2);
    }

    protected final Model _getModel() {
        Class<?> clazz = wrapped.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Model.class.isAssignableFrom(method.getReturnType())) {
                if (method.getParameterTypes().length == 0) {
                    try {
                        method.setAccessible(true);
                        Object result = method.invoke(wrapped);
                        if (result != null && result instanceof Model) {
                            return (Model) result;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                       // e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
