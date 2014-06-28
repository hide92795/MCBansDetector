package hide92795.bukkit.plugin.mcbansdetector.sidebar;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;


public class SideBarManager {
	private final MCBansDetector plugin;
	private org.bukkit.scoreboard.Scoreboard scoreboard;
	private Objective objective;

	public SideBarManager(MCBansDetector plugin) {
		this.plugin = plugin;
		scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
	}

	public void update() {
		createSideBarData();
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player player : players) {
			if (player.hasPermission("mcbansdetector.sidebar") || player.isOp()) {
				try {
					player.setScoreboard(scoreboard);
				} catch (Exception e) {
				}
			}
		}
	}

	private void createSideBarData() {
		if (objective != null) {
			objective.unregister();
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		}
		objective = scoreboard.registerNewObjective("mcbansdetector", "dummy");
		objective.setDisplayName(plugin.localize.getString(Type.WARN_PLAYER));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		boolean zero = true;

		Set<String> warnplayers = plugin.warnCounts.keySet();
		for (String name : warnplayers) {
			if (Bukkit.getPlayerExact(name) != null) {
				zero = false;
				if (name.length() >= 13) {
					name = name.substring(0, 10);
					name = name + "...";
				}
				String disp = String.format(plugin.localize.getString(Type.SIDEBAR_PLAYER_NAME), name);
				Score score = objective.getScore(Bukkit.getOfflinePlayer(disp));
				score.setScore(plugin.warnCounts.get(name));
			}
		}
		if (zero) {
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		}
	}
}
