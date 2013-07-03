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

    /**
     * Register methods of an object that have the EventHandler annotation
     *
     * @param object object to be registered.
     */
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

    /**
     * Submit an object to be handled by the appropriate EventHandler.
     *
     * @param object object to be handled.
     */
    public void submit(Object object) {
        for (EventHandlerBridge bridge : bridges) {
            if (bridge.accept(object.getClass())) {
                bridge.handle(object);
            }
        }
    }

    /**
     * Deregister any EventHandlers of the object.
     *
     * @param object object to remove the listeners of.
     */
    public void deregister(Object object) {
        List<EventHandlerBridge> deprecated = new ArrayList<EventHandlerBridge>();
        for (EventHandlerBridge bridge : bridges) {
            if (bridge.method.getDeclaringClass().equals(object.getClass())) {
                deprecated.add(bridge);
            }
        }
        bridges.removeAll(deprecated);
    }

    /**
     * Internal wrapper class to help map EventHandler methods to the correct event.
     */
    private class EventHandlerBridge {
        private Object object;
        private Method method;

        public EventHandlerBridge(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        /**
         * Checks if the EventHandler can handle the class type.
         *
         * @param eventType class type to check.
         * @return true if the class type can be handled, else false.
         */
        public boolean accept(Class<?> eventType) {
            Class<?> argType = method.getParameterTypes()[0];
            return argType.equals(eventType);
        }

        /**
         * Checks if the method is a valid EventHandler
         *
         * @return true if it is a valid EventHandler else false.
         */
        public boolean validate() {
            return method.getParameterTypes().length == 1 && !isStatic(method.getModifiers());
        }

        /**
         * Calls the EventHandler method and passes the object as the argument.
         *
         * @param arg object to pass into the method.
         */
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