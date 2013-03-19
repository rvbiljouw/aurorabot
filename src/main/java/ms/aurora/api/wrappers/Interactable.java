package ms.aurora.api.wrappers;

/**
 * Date: 19/03/13
 * Time: 09:01
 *
 * @author A_C/Cov
 */
public interface Interactable {

    boolean applyAction(String action);

    boolean hover();

    boolean click(boolean left);

}
