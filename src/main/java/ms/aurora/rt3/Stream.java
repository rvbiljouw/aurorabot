package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface Stream {

    int getOffset();

    byte[] getPayload();

}
