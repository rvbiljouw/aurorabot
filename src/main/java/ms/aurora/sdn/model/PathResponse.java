package ms.aurora.sdn.model;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import ms.aurora.api.wrappers.RSTile;

/**
 * @author tobiewarburton
 */
public class PathResponse {
    /**
     * if the path generation was successful
     */
    private boolean success;
    /**
     * the path if success is true else this will be null
     */
    private RSTile[] path;

    public PathResponse(boolean success, RSTile[] path) {
        this.success = success;
        this.path = path;
    }

    public PathResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RSTile[] getPath() {
        return path;
    }

    public void setPath(RSTile[] path) {
        this.path = path;
    }

    private static JSONDeserializer<PathResponse> deserializer = new JSONDeserializer<PathResponse>();

    public static PathResponse deserialize(String json) {
        return deserializer.deserialize(json);
    }

    public static String serialize(PathResponse response) {
        return new JSONSerializer().deepSerialize(response);
    }
}
