package ms.aurora.api.wrappers;

/**
 * Date: 19/03/13
 * Time: 09:01
 *
 * @author A_C/Cov
 */
public interface Interactable {

    public boolean applyAction(String action);
    public boolean hover();
    public boolean click(boolean left);

}
