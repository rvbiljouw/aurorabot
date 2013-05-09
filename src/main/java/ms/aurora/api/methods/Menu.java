package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.input.VirtualKeyboard;
import ms.aurora.input.VirtualMouse;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Menu related functions
 *
 * @author Rick
 */
public final class Menu {
    private static final Logger logger = Logger.getLogger(Menu.class);
    private static final Pattern pattern = Pattern.compile("<.+?>");

    /**
     * Gets the index of a menu item in the list of menu items
     *
     * @param item The item to look for
     * @return The index of the item if it was found, otherwise -1.
     */
    public static int getIndex(String item) {
        Pattern pattern = Pattern.compile(item.toLowerCase());
        for (String menuEntry : getMenuContent()) {
            Matcher matcher = pattern.matcher(menuEntry);
            if (matcher.find()) {
                return getMenuContent().indexOf(menuEntry);
            }
        }
        return -1;
    }

    /**
     * Checks if the menu is open
     *
     * @return true if the menu is open
     */
    public static boolean isMenuOpen() {
        return Context.getClient().isMenuOpen();
    }

    /**
     * Clicks an item in the menu
     * Note: This method opens the menu if it's not already open.
     *
     * @param action Action to click
     * @return true if success, false if failed.
     */
    public static boolean click(String action) {
        VirtualKeyboard.holdControl();
        sleepNoException(200, 300);
        int itemIndex;
        int tries = 0;
        while((itemIndex = getIndex(action)) == -1 && tries < 12 &&
                !Thread.currentThread().isInterrupted()) {
            tries++;
            sleepNoException(50);
        }
        if (itemIndex != -1) {
            if (itemIndex == 0) {
                VirtualMouse.clickMouse(true);
                return true;
            } else {
                if (!isMenuOpen()) {
                    VirtualMouse.clickMouse(false);
                    sleepNoException(300);
                }
                if (isMenuOpen()) {
                    int menuOptionX = Context.getClient().getMenuX() + (random(10, action.length() * 4));
                    int menuOptionY = Context.getClient().getMenuY() + (21 + (15 * itemIndex));
                    VirtualMouse.clickMouse(menuOptionX, menuOptionY, true);
                    VirtualKeyboard.releaseControl();
                    return true;
                }
            }

        }
        VirtualKeyboard.releaseControl();
        return false;
    }

    /**
     * Checks if the current menu content includes a specific action.
     *
     * @param action Action text (may be a regular expression, too!)
     * @return true if present
     */
    public boolean contains(String action) {
        return getIndex(action) != -1;
    }

    /**
     * Gets the menu item that is in the current "up text" / will be invoked
     * when the user clicks with the current game state and current mouse position.
     *
     * @return hover action
     */
    public String getHoverAction() {
        return getMenuContent().get(0);
    }

    /**
     * Retrieves a list of all menu content.
     *
     * @return menu content.
     */
    private static List<String> getMenuContent() {
        String[] actions = Context.getClient().getMenuActions();
        String[] targets = Context.getClient().getMenuTargets();
        List<String> menuContent = new ArrayList<String>();

        for (int index = 0; index < Context.getClient().getMenuCount(); index++) {
            if (actions[index] != null && targets[index] != null) {
                menuContent.add(removeFormatting(targets[index] + " " + actions[index]).toLowerCase());
            }
        }
        Collections.reverse(menuContent);
        return menuContent;
    }

    private static String removeFormatting(String in) {
        if (in == null)
            return "null";
        return pattern.matcher(in).replaceAll("");
    }
}
