package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.*;

import static ms.aurora.api.methods.filters.NpcFilters.NAMED;
import static ms.aurora.api.methods.filters.ObjectFilters.ID;
import static ms.aurora.api.methods.filters.ObjectFilters.LOCATION;

/**
 * TODO: This shit seems unsolvable..
 * @author rvbiljouw
 */
@RandomManifest(name = "DrillDemon", author = "Rick", version = 1.0)
public class DrillDemon extends Random {
    private Instruction currInstruction;

    @Override
    public boolean activate() {
        return Npcs.get(NAMED("Sergeant Damien")) != null;
    }

    @Override
    public int loop() {
        RSNPC demon = Npcs.get(NAMED("Sergeant Damien"));
        if (demon != null) {
            Instruction insn = test();
            if (insn != null && insn != currInstruction) {
                currInstruction = insn;
                Widgets.clickContinue();
            }


            if (Players.getLocal().isIdle()) {
                if (currInstruction != null) {
                    RSObject sign = Objects.get(ID(currInstruction.getId()));
                    if (sign != null) {
                        RSTile location = sign.getLocation();
                        location.setY(location.getY() - 2);
                        RSObject mat = Objects.get(LOCATION(location));
                        if (mat != null) {
                            mat.applyAction("Use");
                            currInstruction = null;
                            return 4000;
                        }
                    }
                } else {
                    demon.applyAction("Talk");
                    return 3000;
                }
            } else {
                return 2000;
            }

        }
        return -1;
    }

    private final Instruction test() {
        RSWidgetGroup[] widgetGroups = Widgets.getAll();
        for (RSWidgetGroup group : widgetGroups) {
            if (group == null) continue;
            for (RSWidget widget : group.getWidgets()) {
                if (widget == null) continue;
                Instruction insn = Instruction.fromSentence(widget.getText());
                if (insn != null) {
                    return insn;
                }
            }
        }
        return null;
    }

    private static enum Instruction {
        STARJUMP(3627, "star"), PUSHUP(3626, "push"), SITUP(3629, "sit"), JOG(3628, "jog");
        private int id;
        private String insnKey;

        private Instruction(int id, String insnKey) {
            this.id = id;
            this.insnKey = insnKey;
        }

        private static Instruction fromSentence(String sentence) {
            for (Instruction insn : values()) {
                if (sentence.contains(insn.getInsnKey())) {
                    return insn;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public String getInsnKey() {
            return insnKey;
        }
    }
}
