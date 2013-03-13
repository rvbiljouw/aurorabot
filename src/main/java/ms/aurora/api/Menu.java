package ms.aurora.api;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import static ms.aurora.api.ClientContext.get;

public class Menu {

    public static int getIndex(String item) {
        for (String menuEntry : getMenuContent()) {
            if (menuEntry.toLowerCase().contains(item.toLowerCase())) {
                return getMenuContent().indexOf(menuEntry);
            }
        }
        return -1;
    }

    public static boolean isMenuOpen() {
        return get().getClient().isMenuOpen();
    }

    public static boolean click(String action) {
        int itemIndex = getIndex(action);
        if (itemIndex != -1) {
            if (itemIndex == 0) {
                get().input.getMouse().clickMouse(true);
            } else {
                int menuOptionX = get().getClient().getMenuX() + 10;
                int menuOptionY = get().getClient().getMenuY() + 24
                        + (15 * itemIndex - 1);
                get().input.getMouse().clickMouse(menuOptionX, menuOptionY,
                        true);
            }
            return true;
        }
        return false;
    }

    private static List<String> getMenuContent() {
        System.out.println("Acc clientcontext local for thread " + Thread.currentThread().getName());
        String[] actions = get().getClient().getMenuActions();
        String[] targets = get().getClient().getMenuTargets();
        List<String> menuContent = Lists.newArrayList();

        for (int index = 0; index < get().getClient().getMenuCount(); index++) {
            if (actions[index] != null && targets[index] != null) {
                menuContent.add(actions[index] + " " + targets[index]);
            }
        }
        Collections.reverse(menuContent);
        return menuContent;
    }
}
