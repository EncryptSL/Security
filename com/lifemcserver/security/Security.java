package com.lifemcserver.security;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.API;

public class Security extends JavaPlugin implements Listener {
		
		public Security() {}
		public AuthMe auth = AuthMe.getInstance();
		
		public int minPasswordLength = 0;
		public int maxPasswordLength = 32;
		public String PasswordToChatBlockedMsg = "§cOh! You tryed to say your password to public chat! Don't panic, we are blocked the message! :)";
	  
		@Override
		public void onEnable() {
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			saveDefaultConfig();
			setConfigVariables();
	  	}
		
		public void setConfigVariables() {
			minPasswordLength = auth.getConfig().getInt("minPasswordLength");
			maxPasswordLength = auth.getConfig().getInt("passwordMaxLength");
			PasswordToChatBlockedMsg = colorize(auth.getConfig().getString("passwordToChatBlockedMsg"));
		}
		
		public String colorize(String s) {
			return ChatColor.translateAlternateColorCodes('&', s);
		}
		
		@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
		public void onChat(AsyncPlayerChatEvent e) {
			if(e.isCancelled() == false) {
				String msg = e.getMessage().trim();
				if(msg.length() >= auth.getConfig().getInt("minPasswordLength")) {
					if(msg.length() <= auth.getConfig().getInt("passwordMaxLength")) {
						if(API.checkPassword(e.getPlayer().getName(), msg) == true) {
							e.setCancelled(true);
							e.getPlayer().sendMessage(PasswordToChatBlockedMsg);
						}
					}
				}	
			}
		}
}
