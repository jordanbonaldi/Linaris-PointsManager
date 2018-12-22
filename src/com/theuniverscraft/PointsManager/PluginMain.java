package com.theuniverscraft.PointsManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.theuniverscraft.PointsManager.Managers.PointsManager;

public class PluginMain extends JavaPlugin {
	private static PluginMain instance;
	public static PluginMain getInstance() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		PointsManager.getInstance();
	}
	
	public void onDisable() {
		PointsManager.closeInstance();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		int points = PointsManager.getInstance().getPoints(player);
		String strPts = points > 1 ? "gems" : "gem"; 
		player.sendMessage(ChatColor.GOLD + "Vous avez " + ChatColor.AQUA + points + " " + strPts + ChatColor.GOLD + " !");
		return true;
	}
}
