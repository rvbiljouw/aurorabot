package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Stream {

    int getOffset();

    byte[] getPayload();

}
