package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Stream {

    public int getOffset();

    public byte[] getPayload();

}
