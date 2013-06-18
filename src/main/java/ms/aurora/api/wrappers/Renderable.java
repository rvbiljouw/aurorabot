package ms.aurora.api.wrappers;

import ms.aurora.rt3.IModel;
import ms.aurora.rt3.IRenderable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Rick
 */
public class Renderable {
    private final IRenderable wrapped;

    public Renderable(IRenderable wrapped) {
        this.wrapped = wrapped;
    }

    public final int getHeight() {
        return -(wrapped.getHeight() / 2);
    }

    protected final IModel _getModel() {
        Class<?> clazz = wrapped.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (IModel.class.isAssignableFrom(method.getReturnType())) {
                if (method.getParameterTypes().length == 0) {
                    try {
                        method.setAccessible(true);
                        Object result = method.invoke(wrapped);
                        if (result != null && result instanceof IModel) {
                            return (IModel) result;
                        }
                    } catch (IllegalAccessException e) {
                        // e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
