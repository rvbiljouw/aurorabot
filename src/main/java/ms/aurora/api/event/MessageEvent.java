package ms.aurora.api.event;

/**
 * @author rvbiljouw
 */
public class MessageEvent {
    private final String message;

    public MessageEvent(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
