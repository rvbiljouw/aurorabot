package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.filters.Filters;
import ms.aurora.api.methods.tabs.Logout;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;

/**
 * @author rvbiljouw
 */
@RandomManifest(name = "Unsupported", author = "Rick", version = 1.0)
public class Unsupported extends Random {
    private static final String[] UNSUPPORTED_NPC_NAMES = {
            "Sergeant Damien"
    };
    private static final int[] UNSUPPORTED_OBJ_IDS = {};


    @Override
    public boolean activate() {
        return Npcs.getAll(Filters.NPC_NAME(UNSUPPORTED_NPC_NAMES)).length > 0
                || Objects.getAll(Filters.OBJECT_ID(UNSUPPORTED_OBJ_IDS)).length > 0;
    }

    @Override
    public int loop() {
        getSession().getScriptManager().stop();
        Logout.logout();
        return 10;
    }
}
