package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Player extends Character {

    public PlayerComposite getComposite();

    public Model getModel();

    public boolean isVisible();

    public String getName();

}
