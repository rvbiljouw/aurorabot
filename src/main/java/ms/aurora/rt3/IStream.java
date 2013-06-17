package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface IStream {

    int getOffset();

    byte[] getPayload();

}
