package ms.aurora.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rvbiljouw
 */
public class SessionProperties {
    private final Map<String, String> propertyMap = new HashMap<>();

    public void setProperty(String property, String value) {
        if(propertyMap.containsKey(property)) {
            propertyMap.remove(property);
        }
        propertyMap.put(property, value);
    }

    public String getProperty(String property) {
        return propertyMap.get(property);
    }
}
