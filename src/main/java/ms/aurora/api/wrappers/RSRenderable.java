package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Model;
import ms.aurora.api.rt3.Renderable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author rvbiljouw
 */
public class RSRenderable {
    public final ClientContext context;
    private final Renderable wrapped;

    public RSRenderable(ClientContext context, Renderable wrapped) {
        this.context = context;
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
                    } catch (IllegalAccessException e) {
                        //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InvocationTargetException e) {
                        //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
        return null;
    }

}
