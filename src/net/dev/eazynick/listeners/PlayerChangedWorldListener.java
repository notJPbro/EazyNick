package net.dev.eazynick.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import net.dev.eazynick.EazyNick;
import net.dev.eazynick.api.NickManager;
import net.dev.eazynick.utils.FileUtils;
import net.dev.eazynick.utils.Utils;

public class PlayerChangedWorldListener implements Listener {

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
		EazyNick eazyNick = EazyNick.getInstance();
		Utils utils = eazyNick.getUtils();
		FileUtils fileUtils = eazyNick.getFileUtils();
		
		Player p = e.getPlayer();
		NickManager api = new NickManager(p);
		
		if (!(utils.getWorldBlackList().contains(p.getWorld().getName().toUpperCase()))) {
			if (fileUtils.cfg.getBoolean("NickOnWorldChange")) {
				if (utils.getNickOnWorldChangePlayers().contains(p.getUniqueId())) {
					if (!(api.isNicked()))
						p.chat("/renick");
					else {
						Bukkit.getScheduler().runTaskLater(EazyNick.getInstance(), new Runnable() {

							@Override
							public void run() {
								api.unnickPlayer();
								
								Bukkit.getScheduler().runTaskLater(EazyNick.getInstance(), new Runnable() {

									@Override
									public void run() {
										p.chat("/renick");
									}
								}, 10 + (fileUtils.cfg.getBoolean("RandomDisguiseDelay") ? (20 * 2) : 0));
							}
						}, 10);
					}
				}
			}
		}
	}

}
