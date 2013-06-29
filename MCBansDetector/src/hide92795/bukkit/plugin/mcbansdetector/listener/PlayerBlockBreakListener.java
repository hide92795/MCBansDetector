package hide92795.bukkit.plugin.mcbansdetector.listener;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakListener implements Listener {
	public MCBansDetector plugin;

	public PlayerBlockBreakListener(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (plugin.enableProtect) {
			Player player = event.getPlayer();
			if (plugin.getAPI().isPlayerWarned(player)) {
				event.setCancelled(true);
				player.sendMessage(plugin.localize.getString(Type.BLOCK_BREAK_MESSAGE));
			}
		}
	}
}
