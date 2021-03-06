package hide92795.bukkit.plugin.mcbansdetector.listener;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLoginListener implements Listener {
	public MCBansDetector plugin;

	public PlayerLoginListener(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getDisplayName();
		// Notice to Moderator
		plugin.checkCurrentWarnPlayerForModerator(player);

		if (player.hasPermission("mcbansdetector.ignore") || player.isOp()) {
			plugin.getLogger().info("Skipping check player: " + playerName);
			return;
		}

		if (!plugin.valid_config) {
			plugin.getLogger().warning(String.format(plugin.localize.getString(Type.INVALID_CONFIG), playerName));
			return;
		}

		// IPcheck
		plugin.checkCountry(player);
		plugin.checkMCBans(player);
	}
}
