package com.mcapplbee.pricelist;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class PriceList extends JavaPlugin {
    private String combined_args = new String("");
    private Configuration config;
    private Map<String, Object> priceList;

    
    @Override
    public void onEnable() {
        readQSConfig();
        getLogger().info("Loaded");
    }


    @Override
    public void onDisable() {
        getLogger().info("Unloaded");
    }

    
    public void readQSConfig() {
        try {
            config = getServer().getPluginManager().getPlugin("QuickSell").getConfig();

            priceList = config.getConfigurationSection("shops.global.price").getValues(false);
            getLogger().info("Successfully loaded QuickSell config");

            Set set = priceList.entrySet();
            Iterator i = set.iterator();

            while (i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
            }

        } catch (NullPointerException e) {
            getLogger().info("ERROR: Exception grabbing a config");
            getLogger().info("ERROR: Make sure QuickSell is enabled, and working");
            getLogger().info("ERROR: Make sure shops.global is defined, and at"
                             + " least one item listed under price");
        } finally {
        }
    }
    

    public ArrayList<String> searchPrices(String searchterm) {
        ArrayList<String> results = new ArrayList<String>();
        String value = new String();

        Set set = priceList.entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            String candidate = me.getKey().toString();
            
            value = me.getValue().toString();
            
            if (value.equals("0.0")) {
                value = ChatColor.RED + "No Value (Do not sell!)";
            } else {
                value = ChatColor.GOLD + value;
            }
            
            if (candidate.contains(searchterm.toUpperCase())) {
                candidate = WordUtils.capitalize(
                                candidate.replace("_",  " ").toLowerCase());
                results.add(ChatColor.GRAY + candidate + ": " + value);
            }
        }
        return results;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pricelist")) {
            // Input validation
            if (args.length == 0) {
                printUsage(sender);
                return true;
            }


            // Match the format of keys in the Material enum
            combined_args = StringUtils.join(args, "_");

            if (combined_args.length() < 3) {
                sender.sendMessage(ChatColor.YELLOW + "PriceList" + ChatColor.WHITE + 
                                   ": Please enter at least 3 characters");
                return true;
            }
            
            searchPrices(combined_args);
            
            ArrayList<String> results = searchPrices(combined_args);
            
            if (results.size() > 0) {
                sender.sendMessage(ChatColor.YELLOW + "PriceList" + 
                                   ChatColor.WHITE + " results ---");
                    for (String s : results) {
                        sender.sendMessage(s);
                    } 
            } else {
                sender.sendMessage("PriceList: No results for " + StringUtils.join(args, " "));
            }
            
            try {
                //findBlocks = Material.matchMaterial(combined_args).toString();    
                
                //Material.matchMaterial(combined_args).toString();
            } catch (NullPointerException e) {
                sender.sendMessage("Error: Couldn't find " +
                    combined_args.replace("_",  " "));
                printUsage(sender);
            } finally {
                
            }
            return true;
        }
        return true;
    }

    public void printUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Pricelist: " + 
                           ChatColor.GRAY + "Usage is /pricelist <item>");
        sender.sendMessage("EG: /pricelist stone");
    }
}
