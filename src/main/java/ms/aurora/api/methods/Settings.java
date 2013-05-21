package ms.aurora.api.methods;

import static ms.aurora.api.Context.getClient;

/**
 * Setting related functions
 *
 * @author tobiewarburton
 */
public final class Settings {

    /**
     * Retrieves a client setting
     *
     * @param index index of the setting
     * @return setting value
     */
    public static int getSetting(int index) {
        int[] settings = getClient().getWidgetSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }

}
