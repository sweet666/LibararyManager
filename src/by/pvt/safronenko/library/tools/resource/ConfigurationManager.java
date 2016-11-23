package by.pvt.safronenko.library.tools.resource;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private static ResourceBundle resourceBundle = null;

    // класс извлекает информацию из файла config. properties
    private ConfigurationManager() {
    }

    public static synchronized String getProperty(String key) {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("config");
        }
        return resourceBundle.getString(key);
    }
}