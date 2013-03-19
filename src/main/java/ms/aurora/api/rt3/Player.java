package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Player extends Character {

    PlayerComposite getComposite();

    Model getModel();

    boolean isVisible();

    String getName();

}
