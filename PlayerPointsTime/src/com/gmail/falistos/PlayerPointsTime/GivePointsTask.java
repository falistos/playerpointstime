package com.gmail.falistos.PlayerPointsTime;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GivePointsTask implements Runnable
{
	private PlayerPointsTime plugin;
	private int given;

	public GivePointsTask(PlayerPointsTime plugin, int given)
	{
		this.plugin = plugin;
		this.given = given;
	}

	public void run()
	{
		Permission perm = plugin.getPerm();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (perm.has(player, "playerpointstime.getreward"))
			{
				this.plugin.getPlayerPoints().getAPI().set(player.getName(), this.plugin.getPlayerPoints().getAPI().look(player.getName()) + given);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("message").replace("%points", ""+this.given)));
			}
		}
	}
}