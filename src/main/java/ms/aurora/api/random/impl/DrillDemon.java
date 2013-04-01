package ms.aurora.api.random.impl;

import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSTile;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 31/03/13
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class DrillDemon extends Random {
    public int demonID = 2790;
    public int sign1;
    public int sign2;
    public int sign3;
    public int sign4;

    @Override
    public boolean activate() {
        return playerInArea(3167, 4822, 3159, 4818);
    }

    @Override
    public int loop() {

        if (players.getLocal().isMoving() || (players.getLocal().getAnimation() != -1)) {
            return Utilities.random(1200, 1500);
        }

        final RSNPC demon = npcs.get(NpcFilters.ID(demonID));
        final RSObject mat1 = objects.get(ObjectFilters.ID(10076));
        final RSObject mat2 = objects.get(ObjectFilters.ID(10077));
        final RSObject mat3 = objects.get(ObjectFilters.ID(10078));
        final RSObject mat4 = objects.get(ObjectFilters.ID(10079));

        if (demon == null)
            return -1;

        myClickContinue();
        Utilities.sleepNoException(750, 1000);

        if (widgets.getWidgets(148).isValid()) {
            switch (settings.getSetting(531)) {
                case 668:
                    sign1 = 1;
                    sign2 = 2;
                    sign3 = 3;
                    sign4 = 4;
                    break;
                case 675:
                    sign1 = 2;
                    sign2 = 1;
                    sign3 = 3;
                    sign4 = 4;
                    break;
                case 724:
                    sign1 = 1;
                    sign2 = 3;
                    sign3 = 2;
                    sign4 = 4;
                    break;
                case 738:
                    sign1 = 3;
                    sign2 = 1;
                    sign3 = 2;
                    sign4 = 4;
                    break;
                case 787:
                    sign1 = 2;
                    sign2 = 3;
                    sign3 = 1;
                    sign4 = 4;
                    break;
                case 794:
                    sign1 = 3;
                    sign2 = 2;
                    sign3 = 1;
                    sign4 = 4;
                    break;
                case 1116:
                    sign1 = 1;
                    sign2 = 2;
                    sign3 = 4;
                    sign4 = 3;
                    break;
                case 1123:
                    sign1 = 2;
                    sign2 = 1;
                    sign3 = 4;
                    sign4 = 3;
                    break;
                case 1228:
                    sign1 = 1;
                    sign2 = 4;
                    sign3 = 2;
                    sign4 = 3;
                    break;
                case 1249:
                    sign1 = 4;
                    sign2 = 1;
                    sign3 = 2;
                    sign4 = 3;
                    break;
                case 1291:
                    sign1 = 2;
                    sign2 = 4;
                    sign3 = 1;
                    sign4 = 3;
                    break;
                case 1305:
                    sign1 = 4;
                    sign2 = 2;
                    sign3 = 1;
                    sign4 = 3;
                    break;
                case 1620:
                    sign1 = 1;
                    sign2 = 3;
                    sign3 = 4;
                    sign4 = 2;
                    break;
                case 1634:
                    sign1 = 3;
                    sign2 = 1;
                    sign3 = 4;
                    sign4 = 2;
                    break;
                case 1676:
                    sign1 = 1;
                    sign2 = 4;
                    sign3 = 3;
                    sign4 = 2;
                    break;
                case 1697:
                    sign1 = 4;
                    sign2 = 1;
                    sign3 = 3;
                    sign4 = 2;
                    break;
                case 1802:
                    sign1 = 3;
                    sign2 = 4;
                    sign3 = 1;
                    sign4 = 2;
                    break;
                case 1809:
                    sign1 = 4;
                    sign2 = 3;
                    sign3 = 1;
                    sign4 = 2;
                    break;
                case 2131:
                    sign1 = 2;
                    sign2 = 3;
                    sign3 = 4;
                    sign4 = 1;
                    break;
                case 2138:
                    sign1 = 3;
                    sign2 = 2;
                    sign3 = 4;
                    sign4 = 1;
                    break;
                case 2187:
                    sign1 = 2;
                    sign2 = 4;
                    sign3 = 3;
                    sign4 = 1;
                    break;
                case 2201:
                    sign1 = 4;
                    sign2 = 2;
                    sign3 = 3;
                    sign4 = 1;
                    break;
                case 2250:
                    sign1 = 3;
                    sign2 = 4;
                    sign3 = 2;
                    sign4 = 1;
                    break;
                case 2257:
                    sign1 = 4;
                    sign2 = 3;
                    sign3 = 2;
                    sign4 = 1;
                    break;
            }
        }

        if (widgets.getWidget(148, 1).getText().contains("jumps")) {
            if (sign1 == 1) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3167, 4820)) < 2) {
                    walking.clickMap(new RSTile(3160, 4820));
                    mat1.applyAction("Use");
                } else {
                    mat1.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            } else if (sign2 == 1) {
                mat2.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign3 == 1) {
                mat3.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign4 == 1) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3159, 4820)) < 2) {
                    walking.clickMap(new RSTile(3166, 4820));
                    mat4.applyAction("Use");
                } else {
                    mat4.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            }
        } else if (widgets.getWidget(148, 1).getText().contains("push ups")) {
            if (sign1 == 2) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3167, 4820)) < 2) {
                    walking.clickMap(new RSTile(3160, 4820));
                    mat1.applyAction("Use");
                } else {
                    mat1.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            } else if (sign2 == 2) {
                mat2.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign3 == 2) {
                mat3.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign4 == 2) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3159, 4820)) < 2) {
                    walking.clickMap(new RSTile(3166, 4820));
                    mat4.applyAction("Use");
                } else {
                    mat4.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            }
        } else if (widgets.getWidget(148, 1).getText().contains("sit ups")) {
            if (sign1 == 3) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3167, 4820)) < 2) {
                    walking.clickMap(new RSTile(3160, 4820));
                    mat1.applyAction("Use");
                } else {
                    mat1.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            } else if (sign2 == 3) {
                mat2.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign3 == 3) {
                mat3.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign4 == 3) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3159, 4820)) < 2) {
                    walking.clickMap(new RSTile(3166, 4820));
                    mat4.applyAction("Use");
                } else {
                    mat4.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            }
        } else if (widgets.getWidget(148, 1).getText().contains("jog on")) {
            if (sign1 == 4) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3167, 4820)) < 2) {
                    walking.clickMap(new RSTile(3160, 4820));
                    mat1.applyAction("Use");
                } else {
                    mat1.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            } else if (sign2 == 4) {
                mat2.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign3 == 4) {
                mat3.applyAction("Use");
                return Utilities.random(1000, 1500);
            } else if (sign4 == 4) {
                if (calculations.distance(players.getLocal().getLocation(), new RSTile(3159, 4820)) < 2) {
                    walking.clickMap(new RSTile(3166, 4820));
                    mat4.applyAction("Use");
                } else {
                    mat4.applyAction("Use");
                }
                return Utilities.random(1000, 1500);
            }
        }

        if (!myClickContinue()) {
            demon.applyAction("Talk-to");
        }
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean myClickContinue() {
        Utilities.sleepNoException(800, 1000);
        return widgets.getWidget(243, 7).click(true)
                || widgets.getWidget(241, 5).click(true)
                || widgets.getWidget(242, 6).click(true)
                || widgets.getWidget(244, 8).click(true)
                || widgets.getWidget(64, 5).click(true);
    }

    public boolean playerInArea(final int maxX, final int maxY, final int minX, final int minY) {
        final int x = players.getLocal().getLocation().getX();
        final int y = players.getLocal().getLocation().getY();
        if ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY))
            return true;
        return false;
    }
}
