package com.zazsona.pitemp;

import org.bukkit.plugin.Plugin;

public class ConfigManager
{
    private static Plugin plugin = Core.getPlugin(Core.class);

    private static String ENABLED_TAG = "Enabled";
    private static String WARNING_ENABLED_TAG = "WarningEnabled";
    private static String WARNING_TEMPERATURE_TAG = "WarningTempCelsius";
    private static String WARNING_MESSAGE_TAG = "WarningMessage";
    private static String SHUTDOWN_ENABLED_TAG = "ShutdownEnabled";
    private static String SHUTDOWN_TEMPERATURE_TAG = "ShutdownTempCelsius";
    private static String SHUTDOWN_MESSAGE_TAG = "ShutdownMessage";

    public static void save()
    {
        plugin.saveConfig();
    }

    public static void setEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(ENABLED_TAG, newEnabled);
        save();
    }

    public static boolean isEnabled()
    {
        return (boolean) plugin.getConfig().get(ENABLED_TAG);
    }

    public static void setWarningEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(WARNING_ENABLED_TAG, newEnabled);
        save();
    }

    public static boolean isWarningEnabled()
    {
        return (boolean) plugin.getConfig().get(WARNING_ENABLED_TAG);
    }

    public static void setWarningTemperature(int celsius)
    {
        plugin.getConfig().set(WARNING_TEMPERATURE_TAG, celsius);
        save();
    }

    public static int getWarningTemperature()
    {
        return (int) plugin.getConfig().get(WARNING_TEMPERATURE_TAG);
    }

    public static void setWarningMessage(String message)
    {
        plugin.getConfig().set(WARNING_MESSAGE_TAG, message);
        save();
    }

    public static String getWarningMessage()
    {
        return (String) plugin.getConfig().get(WARNING_MESSAGE_TAG);
    }

    public static void setShutdownEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(SHUTDOWN_ENABLED_TAG, newEnabled);
        save();
    }

    public static boolean isShutdownEnabled()
    {
        return (boolean) plugin.getConfig().get(SHUTDOWN_ENABLED_TAG);
    }

    public static void setShutdownTemperature(int celsius)
    {
        plugin.getConfig().set(SHUTDOWN_TEMPERATURE_TAG, celsius);
        save();
    }

    public static int getShutdownTemperature()
    {
        return (int) plugin.getConfig().get(SHUTDOWN_TEMPERATURE_TAG);
    }

    public static void setShutdownMessage(String message)
    {
        plugin.getConfig().set(SHUTDOWN_MESSAGE_TAG, message);
        save();
    }

    public static String getShutdownMessage()
    {
        return (String) plugin.getConfig().get(SHUTDOWN_MESSAGE_TAG);
    }
}
