package com.zazsona.pitemp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class Core extends JavaPlugin
{
    public static final String PLUGIN_NAME = "PiTemp";
    private Timer temperatureActionTimer;

    @Override
    public void onEnable()
    {
        try
        {
            getConfig().options().copyDefaults(true);
            saveConfig();
            this.getCommand(PLUGIN_NAME).setExecutor(new ConfigCommand());
            TemperatureMonitor.getTemperatureCelsius(); // Initial test so we can report failure and abort on boot for invalid OS configurations.

            // Below code only executes if initial test passes
            startTemperatureActionTimer();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, String.format("[%s] The temperature file configuration is invalid. The plugin will be disabled.", PLUGIN_NAME));
            Bukkit.getLogger().log(Level.SEVERE, String.format("[%s] Reason: %s", PLUGIN_NAME, e.getLocalizedMessage()));
            onDisable();
        }
    }

    private void startTemperatureActionTimer()
    {
        temperatureActionTimer = new Timer();
        temperatureActionTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    TemperatureMonitor.performTemperatureControlAction();
                }
                catch (IOException e)
                {
                    Bukkit.getLogger().log(Level.SEVERE, String.format("[%s] Failed to enact temperature action: %s", PLUGIN_NAME, e.getLocalizedMessage()));
                }
            }
        }, 1000 * 15, 1000 * 15);
    }

    @Override
    public void onDisable()
    {
        if (temperatureActionTimer != null)
            temperatureActionTimer.cancel();
    }
}
