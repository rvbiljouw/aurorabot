package ms.aurora.api.event;

import ms.aurora.Application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isStatic;

/**
 * A self-mapping EventBus.
 *
 * @author rvbiljouw
 */
public class EventBus {
    private final List<EventHandlerBridge> bridges = new ArrayList<EventHandlerBridge>();
    private final ThreadGroup threadGroup;

    public EventBus(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public EventBus() {
        this(null);
    }

    public void register(Object object) {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                EventHandlerBridge bridge = new EventHandlerBridge(object, method);
                if (bridge.validate()) {
                    bridges.add(bridge);
                    Application.logger.info("Registered event handler: " + object.getClass().getSimpleName() + "." + method.toString());
                } else {
                    throw new IllegalArgumentException("Invalid EventHandler: " + method.getName());
                }
            }
        }
    }

    public void submit(Object object) {
        for (EventHandlerBridge bridge : bridges) {
            if (bridge.accept(object.getClass())) {
                Runnable runnable = invokable(bridge, object);
                Thread thread = null;
                if(threadGroup != null) {
                    thread = new Thread(threadGroup, runnable);
                } else {
                    thread = new Thread(runnable);
                }

                thread.run();
            }
        }
    }

    public void deregister(Object object) {
        List<EventHandlerBridge> deprecated = new ArrayList<EventHandlerBridge>();
        for (EventHandlerBridge bridge : bridges) {
            if (bridge.method.getDeclaringClass().equals(object.getClass())) {
                deprecated.add(bridge);
            }
        }
        bridges.removeAll(deprecated);
    }

    private Runnable invokable(final EventHandlerBridge bridge, final Object arg) {
        return new Runnable() {
            @Override
            public void run() {
                bridge.handle(arg);
            }
        };
    }

    private class EventHandlerBridge {
        private Object object;
        private Method method;

        public EventHandlerBridge(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public boolean accept(Class<?> eventType) {
            Class<?> argType = method.getParameterTypes()[0];
            return argType.equals(eventType);
        }

        public boolean validate() {
            return method.getParameterTypes().length == 1 && !isStatic(method.getModifiers());
        }

        public void handle(Object arg) {

            try {
                method.invoke(this.object, arg);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Handler invocation failed.", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Handler invocation failed.", e);
            }
        }

        @Override
        public String toString() {
            return "Object: " + object.getClass() + " Method: " + method.toString();
        }
    }
}