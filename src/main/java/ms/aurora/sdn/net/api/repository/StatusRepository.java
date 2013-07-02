package ms.aurora.sdn.net.api.repository;

import ms.aurora.api.methods.Skill;
import ms.aurora.api.script.Script;
import ms.aurora.core.Session;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Repository;

/**
 * @author iJava
 */
public class StatusRepository {

    private Session session;
    private SkillInfo[] skills = new SkillInfo[21];
    private long lastCacheTime;

    public StatusRepository(Session session) {
        this.session = session;
        lastCacheTime = System.currentTimeMillis();
        init();
    }

    private void update() {
        for (SkillInfo info : skills) {
            Skill skill = Skill.valueOf(info.skillName);
            info.update(session.getScriptManager().getCurrentScript(),
                    skill.getBaseLevel(), skill.getExperience(), (System.currentTimeMillis() - lastCacheTime));
        }
        lastCacheTime = System.currentTimeMillis();
    }

    private void init() {
        for (Skill skill : Skill.values()) {
            skills[skill.index] = new SkillInfo(skill.name(), skill.getBaseLevel(), skill.getExperience());
        }
    }

    public String getStatus() {
        return "Status :" + (session.getScriptManager().toString() + " : " + (session.getScriptManager().getCurrentScript()
        != null ?session.getScriptManager().toString() : "No Script"));
    }

    public SkillInfo[] getSkills() {
        update();
        return skills;
    }

    private class SkillInfo {

        private String skillName;
        private int baseExp, baseLevel, levelsGained, expGained;
        private long timeBotting;
        private Script currentScript;

        private SkillInfo(String skillName, int baseLevel, int baseExp) {
            this.skillName = skillName;
            this.baseLevel = baseLevel;
            this.baseExp = baseExp;
        }

        public void update(Script currentScript, int currLevel, int currExp, long timeBotting) {
            levelsGained = currLevel - baseLevel;
            this.currentScript = currentScript;
            expGained = currExp - baseExp;
            if (currExp > 0) {
                this.timeBotting = timeBotting;
            }
        }

        public int getLevelsGained() {
            return levelsGained;
        }

        public int getExpGained() {
            return expGained;
        }

        public long getTimeBotting() {
            return timeBotting;
        }

        public Script getCurrentScript() {
            return currentScript;
        }

        public String getSkillName() {
            return skillName;
        }

        public long getScriptId() {
            for(RemoteScript remote : Repository.REMOTE_SCRIPT_LIST) {
                if(remote.name().equals(currentScript.getManifest().name()) &&
                        remote.author().equals(currentScript.getManifest().author()) &&
                        remote.category().equals(currentScript.getManifest().category())) {
                    return remote.getId();
                }
            }
            return -1;
        }
    }
}
