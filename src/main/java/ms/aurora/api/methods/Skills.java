package ms.aurora.api.methods;

import ms.aurora.api.Context;

/**
 * Skill related functions
 *
 * @author A_C/Cov
 */
public final class Skills {

    /**
     * An enum representing all skills
     */
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

        private Skill(int index) {
            this.index = index;
        }
    }

    /**
     * Retrieves the experience for specified skill
     *
     * @param skill skill
     * @return experience
     */
    public static int getExperience(Skill skill) {
        return Context.get().getClient().getSkillExperiences()[skill.index];
    }

    /**
     * Retrieves the level for specified skill
     *
     * @param skill skill
     * @return level
     */
    public static int getLevel(Skill skill) {
        return Context.get().getClient().getSkillLevels()[skill.index];
    }

    /**
     * Retrieves the total XP required for specified level
     *
     * @param level level
     * @return xp required
     */
    public static int getXpForLevel(int level) {
        double total = 0;
        for (int i = 1; i < level; i++) {
            total += Math.floor(i + 300 * Math.pow(2, i / 7));
        }
        return (int) Math.floor(total / 4);
    }

    /**
     * Retrieves the remaining XP required to reach a level, based on the current level
     *
     * @param skill Skill to calculate for
     * @param level Level to reach
     * @return remaining xp to level
     */
    public static int getExperienceToLevel(Skill skill, int level) {
        int xpForLevel = getXpForLevel(level > 99 ? 99 : level);
        return xpForLevel - getExperience(skill);
    }

}

