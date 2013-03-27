package ms.aurora.api.methods;

import com.google.common.collect.Lists;
import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Utilities;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Menu {
    private final ClientContext ctx;
    
    public Menu(ClientContext ctx) {
        this.ctx = ctx;
    }

    public int getIndex(String item) {

        Pattern pattern = Pattern.compile(item);
        for (String menuEntry : getMenuContent()) {

            Matcher matcher = pattern.matcher(menuEntry);
            if (matcher.find()) {
                return getMenuContent().indexOf(menuEntry);
            }
        }
        return -1;
    }

    public boolean isMenuOpen() {
        return ctx.getClient().isMenuOpen();
    }

    public boolean click(String action) {
        int itemIndex = getIndex(action);
        if (itemIndex != -1) {
            if (itemIndex == 0) {
                ctx.input.getMouse().clickMouse(true);
                return true;
            } else {
                int tries = 0;
                while (!isMenuOpen() && tries < 5) {
                    ctx.input.getMouse().clickMouse(false);
                    tries++;
                    Utilities.sleepNoException(100);
                }

                if (isMenuOpen()) {
                    int menuOptionX = ctx.getClient().getMenuX() + 10;
                    int menuOptionY = ctx.getClient().getMenuY() + 21
                            + (15 * itemIndex - 1);
                    ctx.input.getMouse().clickMouse(menuOptionX, menuOptionY,
                            true);
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getMenuContent() {
        String[] actions = ctx.getClient().getMenuActions();
        String[] targets = ctx.getClient().getMenuTargets();
        List<String> menuContent = Lists.newArrayList();

        for (int index = 0; index < ctx.getClient().getMenuCount(); index++) {
            if (actions[index] != null && targets[index] != null) {
                menuContent.add(actions[index] + " " + targets[index]);
            }
        }
        Collections.reverse(menuContent);
        return menuContent;
    }
}
