package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;

/**
 * Date: 27/03/13
 * Time: 16:51
 *
 * @author A_C/Cov
 */
public class Skills {

    private final ClientContext ctx;

    public Skills(ClientContext ctx) {
        this.ctx = ctx;
    }

    public enum Skill {
        ATACK(0),
        STRENGTH(2),
        DEFENCE(1),
        RANGED(4),
        PRAYER(5),
        MAGIC(6),
        RUNECRAFT(20),
        HITPOINTS(3),
        AGILITY(16),
        HERBLORE(15),
        THIEVING(17),
        CRAFTING(12),
        FLETCHING(9),
        SLAYER(18),
        MINING(14),
        SMITHING(13),
        FISHING(10),
        COOKING(7),
        FIREMAKING(11),
        WOODCUTTING(8),
        FARMING(19);

        public final int index;

        private Skill(int index){
            this.index = index;
        }
    }

    public int getExperience(Skill skill) {
        return this.ctx.getClient().getSkillExperiences()[skill.index];
    }

    public int getLevel(Skill skill) {
        return this.ctx.getClient().getSkillLevels()[skill.index];
    }

    public int getXpForLevel(int level) {
        double total = 0;
        for (int i = 1; i < level; i++) {
            total += Math.floor(i + 300 * Math.pow(2, i / 7));
        }
        return (int) Math.floor(total / 4);
    }

    public int getExperienceToLevel(Skill skill, int level) {
        int xpForLevel = this.getXpForLevel(level > 99 ? 99 : level);
        return xpForLevel - this.getExperience(skill);
    }

}

