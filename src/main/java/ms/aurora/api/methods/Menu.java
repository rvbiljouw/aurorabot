package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.input.VirtualKeyboard;
import ms.aurora.input.VirtualMouse;
import org.apache.log4j.Logger;

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
 * @author tobiewarburton
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
        int itemIndex = getIndex(action);
        if (itemIndex != -1) {
            logger.info("Menu index for " + action + " detected as " + itemIndex);
            if (itemIndex == 0 || getHoverAction().equals(action)) {
                logger.info("First entry, left clicking.");
                VirtualMouse.clickMouse(true);
                return true;
            } else {
                if (!isMenuOpen()) {
                    logger.info("Menu is not open, opening..");
                    VirtualMouse.clickMouse(false);
                    sleepNoException(300);
                }
                if (isMenuOpen()) {
                    int menuOptionX = Context.getClient().getMenuX() + (random(10, action.length() * 4));
                    int menuOptionY = Context.getClient().getMenuY() + (21 + (15 * itemIndex));
                    VirtualMouse.clickMouse(menuOptionX, menuOptionY, true);
                    logger.info("Clicking item at " + menuOptionX + "," + menuOptionY);
                    logger.info("Uptext: " + getHoverAction());
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
    public static boolean contains(String action) {
        return getIndex(action) != -1;
    }

    /**
     * Gets the menu item that is in the current "up text" / will be invoked
     * when the user clicks with the current game state and current mouse position.
     *
     * @return hover action
     */
    public static String getHoverAction() {
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

        for (int i = targets.length - 1; i >= 0; i--) {
            if (actions[i] != null && targets[i] != null) {
                menuContent.add(removeFormatting(targets[i] + " " + actions[i]).toLowerCase());
            }
        }
        return menuContent.subList(menuContent.size() - Context.getClient().getMenuCount(),
                menuContent.size());
    }

    /**
     * A simple helper method which standardizes the naming of menu items
     * Also prevents nullpointers
     *
     * @param in the input string
     * @return the formatted string
     */
    private static String removeFormatting(String in) {
        if (in == null)
            return "null";
        return pattern.matcher(in).replaceAll("");
    }


    /**
     * A state predicate which filters by the actionName
     *
     * @param actionName the action name you wish to filter for
     * @return a StatePredicate which filters by the actionName
     */
    public static StatePredicate containsPred(final String actionName) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return contains(actionName);
            }
        };
    }
}
