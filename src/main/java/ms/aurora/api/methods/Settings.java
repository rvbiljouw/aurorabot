package ms.aurora.api.methods;

import static ms.aurora.api.Context.getClient;

/**
 * Setting related functions
 *
 * @author tobiewarburton
 */
public final class Settings {

    /**
     * Retrieves a client setting at the supplied index
     *
     * @param index index of the setting
     * @return setting value
     */
    public static int get(int index) {
        int[] settings = getClient().getWidgetSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }

}
