package com.zazsona.pitemp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.TimerTask;

public class TemperatureMonitor extends TimerTask
{
    private long lastWarningSec = 0;

    @Override
    public void run()
    {
        try
        {
            File tempFile = new File("/sys/class/thermal/thermal_zone0/temp");
            if (!tempFile.exists())
                throw new IOException();
            String tempFileContents = new String(Files.readAllBytes(tempFile.toPath()));
            int temper = (Integer.parseInt(tempFileContents)/1000);

            if (temper >= ConfigManager.getShutdownTemperature())
            {
                try
                {
                    Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+ConfigManager.getShutdownMessage());
                    Thread.sleep(8000);
                    Bukkit.getServer().shutdown();
                }
                catch (InterruptedException e)
                {
                    Bukkit.getServer().shutdown();
                }
            }
            else if (temper >= ConfigManager.getWarningTemperature() && Instant.now().getEpochSecond()-lastWarningSec > 60*15)
            {
                lastWarningSec = Instant.now().getEpochSecond();
                Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+ConfigManager.getWarningMessage());
            }
        }
        catch (IOException | NumberFormatException e)
        {
            Bukkit.getLogger().info("This Linux distro does not have a valid temperature file.");
        }
    }
}
