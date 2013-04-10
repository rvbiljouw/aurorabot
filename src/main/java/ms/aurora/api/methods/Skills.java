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

    // Table of levels. Index is the level and the value is the experience required
    private static final int[] expTable = {
            0 , 0, 83, 174, 276, 388, 512, 650, 801,
            969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973,
            4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031,
            13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408,
            33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636,
            184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599,
            407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200,
            1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
            3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253,
            7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };

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
     * Retrieves the remaining XP required to reach a level, based on the current level
     *
     * @param skill Skill to calculate for
     * @param level Level to reach
     * @return remaining xp to level
     */
    public static int getExperienceToLevel(Skill skill, int level) {
        if(level < 0 || level > 99)
            return -1;
        int experience = getExperience(skill);
        if(experience == -1)
            return -1;
        return expTable[level] - experience;
    }

}

