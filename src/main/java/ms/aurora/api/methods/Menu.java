package ms.aurora.api.methods;

import com.google.common.collect.Lists;
import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Menu {

    public static int getIndex(String item) {
        Pattern pattern = Pattern.compile(item);
        for (String menuEntry : getMenuContent()) {
            Matcher matcher = pattern.matcher(menuEntry);
            if (matcher.find()) {
                return getMenuContent().indexOf(menuEntry);
            }
        }
        return -1;
    }

    public static boolean isMenuOpen() {
        return Context.get().getClient().isMenuOpen();
    }

    public static boolean click(String action) {
        int itemIndex = getIndex(action);
        if (itemIndex != -1) {
            if (itemIndex == 0) {
                Context.get().input.getMouse().clickMouse(true);
                return true;
            } else {
                int tries = 0;
                while (!isMenuOpen() && tries < 5) {
                    Context.get().input.getMouse().clickMouse(false);
                    tries++;
                    Utilities.sleepNoException(100);
                }

                if (isMenuOpen()) {
                    int menuOptionX = Context.get().getClient().getMenuX() + 10;
                    int menuOptionY = Context.get().getClient().getMenuY() + 21
                            + (15 * itemIndex - 1);
                    Context.get().input.getMouse().clickMouse(menuOptionX, menuOptionY,
                            true);
                    return true;
                }
            }
        }
        return false;
    }

    private static List<String> getMenuContent() {
        String[] actions = Context.get().getClient().getMenuActions();
        String[] targets = Context.get().getClient().getMenuTargets();
        List<String> menuContent = Lists.newArrayList();

        for (int index = 0; index < Context.get().getClient().getMenuCount(); index++) {
            if (actions[index] != null && targets[index] != null) {
                menuContent.add(actions[index] + " " + targets[index]);
            }
        }
        Collections.reverse(menuContent);
        return menuContent;
    }
}
