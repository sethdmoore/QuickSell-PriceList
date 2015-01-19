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
			getLogger().info("Trying to grab QuickSell config...");
			config = getServer().getPluginManager().getPlugin("QuickSell").getConfig();
			//Configuration config = pc.getConfig();
			//config = qs.getConfig();

			priceList = config.getConfigurationSection("shops.global.price").getValues(false);
			getLogger().info("Grabbed the config!");

			Set set = priceList.entrySet();
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				//getLogger().info(me.getKey() + ": " + me.getValue());
				//getLogger().info(BeanUtils.describe(bean));
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

		Set set = priceList.entrySet();
		Iterator i = set.iterator();

		while (i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			String candidate = me.getKey().toString();
			
			if (candidate.contains(searchterm.toUpperCase())) {
				candidate = WordUtils.capitalize(
								candidate.replace("_",  " ").toLowerCase());
				results.add(ChatColor.GRAY + candidate + ": " +
							ChatColor.GOLD + me.getValue());
			}
		}
		return results;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pricelist")) {
			if (args.length == 0) {
				printUsage(sender);
				return false;
			}

			combined_args = StringUtils.join(args, "_");
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

			//getLogger().info("Command Issued");
			return true;
		}
		return false;
	}

	public void printUsage(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "Pricelist: " + 
						   ChatColor.GRAY + "Usage is /pricelist <item>");
		sender.sendMessage("EG: /pricelist stone");
	}
}
