package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 01/05/13
 * Time: 14:28
 *
 * @author A_C/Cov
 */
public class MouseEventChain {

    private List<MouseEvent> eventList;
    private List<Integer> sleepTimes;

    public MouseEventChain(int size) {
        this.eventList = new ArrayList<MouseEvent>(size);
        this.sleepTimes = new ArrayList<Integer>(size);
    }

    public void add(MouseEvent event, int sleepTime) {
        this.eventList.add(event);
        this.sleepTimes.add(sleepTime);
    }

    public MouseEvent[] getMouseEvents() {
        return this.eventList.toArray(new MouseEvent[this.eventList.size()]);
    }

    public int[] getSleepTimes() {
        int[] sleeps = new int[this.sleepTimes.size()];
        for (int i = 0; i < sleeps.length; i++) {
            sleeps[i] = this.sleepTimes.get(i);
        }
        return sleeps;
    }

    private static Component getComponent() {
        return Context.getClient().getCanvas();
    }

    private static int getButtonModifiers(int button) throws IllegalArgumentException {
        switch (button) {
            case MouseEvent.BUTTON1:
                return MouseEvent.BUTTON1_MASK;
            case MouseEvent.BUTTON2:
                return MouseEvent.BUTTON2_MASK;
            case MouseEvent.BUTTON3:
                return MouseEvent.BUTTON3_MASK;
            default:
                throw new IllegalArgumentException("Invalid button");
        }
    }

    private static int getRandomClickTime() {
        return 46 + Utilities.random(0, 60); // Blessed be this method by satan.
    }

    private static int getRandomMoveTime() {
        return 1 + Utilities.random(0, 3); // more of the devils handiwork
    }

    private static int getRandomDragTime() {
        return 3 + Utilities.random(0, 60); // his all over this shit, gonna need jesus to save us soon
    }

    public static MouseEventChain createMousePressed(int x, int y, int button) {
        MouseEventChain chain = new MouseEventChain(1);
        long time = System.currentTimeMillis();
        int buttonModifiers = getButtonModifiers(button);
        chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED, time, buttonModifiers, x, y, 1, false, button), 0);
        return chain;
    }

    public static MouseEventChain createMouseReleased(int x, int y, int button) {
        MouseEventChain chain = new MouseEventChain(1);
        long time = System.currentTimeMillis();
        int buttonModifiers = getButtonModifiers(button);
        chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED, time, buttonModifiers, x, y, 1, false, button), 0);
        return chain;
    }

    public static MouseEventChain createMouseDrag(Point[] points, int button) {
        MouseEventChain chain = new MouseEventChain(points.length );
        long time = System.currentTimeMillis();
        int lag = getRandomDragTime();
        for (int i = 1; i < points.length - 1; i++) {
            chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_DRAGGED, time, button, points[i].x, points[i].y, 0, false, 0), lag);
            lag += getRandomDragTime();
            time += lag;
        }
        return chain;
    }

    public static MouseEventChain createMousePath(Point[] points) {
        MouseEventChain chain = new MouseEventChain(points.length);
        long time = System.currentTimeMillis();
        int lag = 0;
        for (Point point : points) {
            chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_MOVED, time, 0, point.x, point.y, 0, false, 0), lag);
            lag = getRandomMoveTime();
            time += lag;
        }
        return chain;
    }

    public static MouseEventChain createMouseClick(Point p, int button, int clickCount) {
        MouseEventChain chain = new MouseEventChain(clickCount * 3);
        int buttonModifiers = getButtonModifiers(button);
        long time = System.currentTimeMillis();
        int lag = 0;
        int count = 1;
        for (int i = 0; i < clickCount * 3; i += 3) {
            chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            lag = getRandomClickTime();
            time += lag;
            chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            chain.add(new MouseEvent(getComponent(), MouseEvent.MOUSE_CLICKED, time, buttonModifiers, p.x, p.y, count, false, button), lag);
            lag = getRandomClickTime();
            time += lag;
            count++;
        }
        return chain;
    }

}
