package com.zazsona.pitemp;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.getCommand("PiTemp").setExecutor(new ConfigCommand());
    }

    @Override
    public void onDisable()
    {
    }
}
