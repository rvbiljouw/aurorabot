package ms.aurora.rt3;

/**
 * @author rvbiljouw
 */
public interface Stream {

    int getOffset();

    byte[] getPayload();

}
