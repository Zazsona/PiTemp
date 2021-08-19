package com.zazsona.pitemp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.logging.Level;

public class ConfigCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission("PiTemp.Config"))
        {
            if (args.length == 0)
            {
                String helpBody =  "/PiTemp Check\n"+
                        "/PiTemp [Enable | Disable]\n"+
                        "/PiTemp Warning [Enable | Disable | Temp | Message] (Value)\n" +
                        "/PiTemp Shutdown [Enable | Disable | Temp | Message] (Value)\n";
                String page = addHeader(Core.PLUGIN_NAME, helpBody);
                sender.sendMessage(page);
            }
            else if (args[0].equalsIgnoreCase("check"))
            {
                checkTemperature(sender);
            }
            else if (args[0].equalsIgnoreCase("warning"))
            {
                parseConfigSelection(args, sender, false);
            }
            else if (args[0].equalsIgnoreCase("shutdown"))
            {
                parseConfigSelection(args, sender, true);
            }
            else
            {
                if (args[0].equalsIgnoreCase("enable"))
                    setPluginEnabled(sender, true);
                else if (args[0].equalsIgnoreCase("disable"))
                    setPluginEnabled(sender, false);
            }
        }
        return false;
    }

    private void checkTemperature(CommandSender sender)
    {
        try
        {
            int temp = TemperatureMonitor.getTemperatureCelsius();
            int warningTemp = ConfigManager.getWarningTemperature();
            ChatColor tempColor = ChatColor.GREEN;
            if (temp >= warningTemp - 10 && temp < warningTemp)
                tempColor = ChatColor.GOLD;
            if (temp >= warningTemp)
                tempColor = ChatColor.RED;

            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + "The system is currently at "+tempColor+temp+"*C"));
        }
        catch (IOException e)
        {
            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.RED + String.format("Could not get temperature: %s", e.getLocalizedMessage())));
        }
    }

    private void setPluginEnabled(CommandSender sender, boolean enable)
    {
        ConfigManager.setEnabled(enable);
        String state = ((enable) ? ChatColor.GREEN+"enabled" : ChatColor.RED+"disabled");
        sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + String.format("%s is now %s.", Core.PLUGIN_NAME, state)));
    }

    private void parseConfigSelection(String[] args, CommandSender sender, boolean isShutdown)
    {
        if (args.length > 1)
        {
            switch (args[1].toUpperCase())
            {
                case "ENABLE":
                case "ACTIVE":
                    setFeatureEnabled(sender, isShutdown, true);
                    break;
                case "DISABLE":
                case "INACTIVE":
                    setFeatureEnabled(sender, isShutdown, false);
                    break;
                case "TEMP":
                case "TEMPERATURE":
                case "CELSIUS":
                    setTemperature(sender, isShutdown, args);
                    break;
                case "MESSAGE":
                case "OUTPUT":
                    setMessage(sender, isShutdown, args);
                    break;
            }
        }
        else
        {
            sendInstructions(sender, isShutdown);
        }
    }

    private void setFeatureEnabled(CommandSender sender, boolean isShutdown, boolean enable)
    {
        String context = (isShutdown) ? "Shutdown" : "Warning";
        if (isShutdown)
            ConfigManager.setShutdownEnabled(enable);
        else
            ConfigManager.setWarningEnabled(enable);

        String state = ((enable) ? ChatColor.GREEN+"enabled" : ChatColor.RED+"disabled");
        sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + String.format("%s is now %s.", context, state)));
    }

    private void setTemperature(CommandSender sender, boolean isShutdown, String[] args)
    {
        if (args.length > 2 && args[2].matches("[0-9]+"))
        {
            int celsius = Integer.parseInt(args[2]);
            if (isShutdown)
                ConfigManager.setShutdownTemperature(celsius);
            else
                ConfigManager.setWarningTemperature(celsius);

            String context = (isShutdown) ? "Shutdown" : "Warning";
            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + context + " set to " + ChatColor.BLUE+celsius+"*C"));
        }
        else
            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + "No celsius value was specified."));
    }

    private void setMessage(CommandSender sender, boolean isShutdown, String[] args)
    {
        if (args.length > 2)
        {
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 2; i<args.length; i++)
                messageBuilder.append(args[i]).append(" ");
            String message = messageBuilder.toString();

            if (isShutdown)
                ConfigManager.setShutdownMessage(message);
            else
                ConfigManager.setWarningMessage(message);

            String context = (isShutdown) ? "Shutdown" : "Warning";
            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + context + " message set to:\n" + ChatColor.BLUE + message));
        }
        else
            sender.sendMessage(addHeader(Core.PLUGIN_NAME, ChatColor.WHITE + "No message was specified."));
    }

    private void sendInstructions(CommandSender sender, boolean isShutdown)
    {
        String context = (isShutdown) ? "Shutdown" : "Warning";
        String pageBody = "/PiTemp "+context+" Enable - Enables "+context+"s\n" +
                "/PiTemp "+context+" Disable - Disables "+context+"s\n" +
                "/PiTemp "+context+" Temp - Set activation temperature (Celsius)\n" +
                "/PiTemp "+context+" Message - Message to send on activation.";
        String page = addHeader(Core.PLUGIN_NAME, pageBody);
        sender.sendMessage(page);
    }

    public static String addHeader(String headerText, String content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.YELLOW).append("---------");
        sb.append(ChatColor.WHITE).append(" ").append(headerText).append(" ");
        sb.append(ChatColor.YELLOW).append("--------------------");
        sb.append(ChatColor.WHITE).append("\n");
        sb.append(content);
        return sb.toString();
    }
}
