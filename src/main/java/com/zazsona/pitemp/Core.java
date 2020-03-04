package com.zazsona.pitemp;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;

public class Core extends JavaPlugin
{
    private Timer temperTimer;

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.getCommand("PiTemp").setExecutor(new ConfigCommand());
        temperTimer = new Timer();
        temperTimer.scheduleAtFixedRate(new TemperatureMonitor(), 1000*20, 1000*20);
    }

    @Override
    public void onDisable()
    {
        temperTimer.cancel();
    }
}
