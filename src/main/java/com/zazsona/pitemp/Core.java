package com.zazsona.pitemp;

import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable()
    {
    }
}
