package hide92795.bukkit.plugin.mcbansdetector.listener;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerLogoutListener implements Listener {
	public MCBansDetector plugin;

	public PlayerLogoutListener(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		new BukkitRunnable() {
			public void run() {
				plugin.updateSidebar();
			}
		}.runTaskLater(plugin, 20L);
	}
}
