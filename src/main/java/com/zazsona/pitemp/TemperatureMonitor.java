package com.zazsona.pitemp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.TimerTask;

public class TemperatureMonitor
{
    private static long lastWarningSec = 0;

    public static void performTemperatureControlAction() throws IOException
    {
        int cel = getTemperatureCelsius();
        performTemperatureControlAction(cel);
    }

    public static void performTemperatureControlAction(int celsius) throws IOException
    {
        if (celsius >= ConfigManager.getShutdownTemperature())
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
        else if (celsius >= ConfigManager.getWarningTemperature() && ((Instant.now().getEpochSecond() - lastWarningSec) > 60*15))
        {
            lastWarningSec = Instant.now().getEpochSecond();
            Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+ConfigManager.getWarningMessage());
        }
    }



    public static int getTemperatureCelsius() throws IOException
    {
        try
        {
            File tempFile = new File("/sys/class/thermal/thermal_zone0/temp");
            if (!tempFile.exists())
                throw new IOException("Temperature file does not exist.");
            String tempFileContents = new String(Files.readAllBytes(tempFile.toPath())).replaceAll("[^0-9a-zA-Z]", "");
            int temperatureValue = Integer.parseInt(tempFileContents);
            int celsius = Math.round(temperatureValue / 1000.0f);
            return celsius;
        }
        catch (NumberFormatException e)
        {
            throw new IOException("Invalid temperature data.");
        }
    }
}
