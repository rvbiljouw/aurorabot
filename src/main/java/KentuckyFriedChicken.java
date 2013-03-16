import ms.aurora.api.Npcs;
import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;

/**
 * @author rvbiljouw
 */
@ScriptMetadata(name = "KFC Niggers!")
public class KentuckyFriedChicken extends LoopScript {
    @Override
    public int loop() {
        final RSNPC closestChicken = Npcs.get(CHICKEN);
        if (closestChicken != null) {
            closestChicken.applyAction("Attack");
        }
        return 100;
    }

    private static final Predicate<RSNPC> CHICKEN = new Predicate<RSNPC>() {

        @Override
        public boolean apply(RSNPC object) {
            return object.getName().equals("Chicken") && !object.isInCombat();
        }

    };
}
