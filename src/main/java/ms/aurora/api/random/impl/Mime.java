package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.Predicates;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Widget;

import static ms.aurora.api.util.Utilities.random;

/**
 * Solves the mime random
 *
 * @author Rick, iJava
 */
@AfterLogin
@RandomManifest(name = "Mime", version = 1.0)
public class Mime extends Random {
    private static final int WIDGET_PARENT_ID = 188;
    private Animation lastAnimation;
    private Animation lastPerformed;


    @Override
    public boolean activate() {
        return Npcs.get(Predicates.NPC_NAME("Mime")) != null;
    }

    @Override
    public int loop() {
        if (!activate()) {
            return -1;
        }
        NPC mime = Npcs.get(Predicates.NPC_NAME("Mime"));
        if (mime != null) {
            Animation anim = Animation.forAnim(mime.getAnimation());
            if (anim != null && anim != lastAnimation) {
                lastAnimation = anim;
            }
        }

        if (lastPerformed == lastAnimation) {
            return 50;
        }

        Widget widget = Widgets.getWidget(WIDGET_PARENT_ID, lastAnimation.widget);
        if (widget != null) {
            widget.click(true);
            Utilities.sleepNoException(2000);
            Utilities.sleepWhile(new StatePredicate() {
                @Override
                public boolean apply() {
                    return Players.getLocal().getAnimation() != -1;
                }
            }, 7000);
            lastPerformed = lastAnimation;
            return 50;
        }
        return random(100, 200);
    }

    private static enum Animation {

        CRY(860, 2),
        THINK(857, 3),
        LAUGH(861, 4),
        DANCE(866, 5),
        CLIMB_ROPE(1130, 6),
        LEAN(1129, 7),
        GLASS_WALL(1128, 8),
        GLASS_BOX(1131, 9);

        int anim, widget;

        Animation(int anim, int widget) {
            this.anim = anim;
            this.widget = widget;
        }

        static Animation forAnim(int anim) {
            for (Animation animation : values()) {
                if (anim == animation.anim) {
                    return animation;
                }
            }
            return null;
        }
    }
}
