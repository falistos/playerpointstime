package com.gmail.falistos.PlayerPointsTime;

import net.milkbowl.vault.permission.Permission;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerPointsTime extends JavaPlugin implements Listener {
	
	private Permission perm = null;
	private PlayerPoints playerPoints;
	private String prefix = ChatColor.RED + "[" + ChatColor.GOLD + "PlayerPointsTime" + ChatColor.RED + "] " + ChatColor.GREEN;
	private int taskId;
	
	private String version = "0.1";
	
	public void onEnable() {
		getLogger().info("Enabled");

		getServer().getPluginManager().registerEvents(this, this);
		
		// Configuration
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		// Permissions
		setupPermissions();
		
		// PlayerPoints hook
		hookPlayerPoints();
		
		// Launch task
		this.executeTask();
	}

	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            this.perm = permissionProvider.getProvider();
        }
        return (getPerm() != null);
    }
	
	private boolean hookPlayerPoints() {
	    final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
	    playerPoints = PlayerPoints.class.cast(plugin);
	    return playerPoints != null; 
	}
	
	public PlayerPoints getPlayerPoints() {
	    return playerPoints;
	}
	
	public void executeTask()
	{
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GivePointsTask(this, this.getConfig().getInt("points")), this.getConfig().getInt("time") * 20, this.getConfig().getInt("time") * 20);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("pointstime")) {
			if (args[0].equalsIgnoreCase("reload"))
			{
				this.reloadConfig();
				
				Bukkit.getScheduler().cancelTask(this.taskId);
				this.executeTask();
				
				sender.sendMessage(this.prefix + "Reloaded");
				return true;
			}
			else if (args[0].equalsIgnoreCase("info"))
			{
				sender.sendMessage(this.prefix + "Version " + this.version + " - Created by Falistos/BritaniaCraft (falistos@gmail.com)");
				return true;
			}
		}
		return false;
	}

	public void onDisable() {
		getLogger().info("Disabled");
	}

	public Permission getPerm() {
		return perm;
	}
	
}