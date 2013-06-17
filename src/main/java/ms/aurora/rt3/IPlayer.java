package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface IPlayer extends IEntity {

    IPlayerComposite getComposite();

    IModel getModel();

    boolean isVisible();

    String getName();

}
