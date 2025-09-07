package ac.anticheat.taco.utils;

import ac.anticheat.taco.TacoAC;

public class ConfigUtil {
    private static TacoAC plugin = TacoAC.getInstance();

    public static String getString(String path, String def) {
        String str = plugin.getConfig().getString(path);
        if (str == null) {
            return def;
        }
        return str;
    }

    public static boolean getBoolean(String path, boolean def) {
        if (plugin.getConfig().contains(path)) {
            return plugin.getConfig().getBoolean(path);
        }
        return def;
    }

    public static int getInt(String path, int def) {
        if (plugin.getConfig().contains(path)) {
            return plugin.getConfig().getInt(path);
        }
        return def;
    }

    public static double getDouble(String path, double def) {
        if (plugin.getConfig().contains(path)) {
            return plugin.getConfig().getDouble(path);
        }
        return def;
    }

    public static void reload() {
        plugin.reloadConfig();
    }
}
