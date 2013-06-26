package hide92795.bukkit.plugin.mcbansdetector;

import ipx.IP;
import ipx.IPRange;
import org.bukkit.entity.Player;

public class MCBansDetectorAPI {
	private final MCBansDetector plugin;

	public MCBansDetectorAPI(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	public String getCountryName(Player player) {
		IPRange range = getIPRange(player);

		if (range == null) {
			return "UnKnown";
		} else if (range.getId().equals("LOCAL")) {
			return plugin.getConfig().getString("LocalCountry.Name");
		} else {
			return range.getCountryName();
		}
	}

	public IPRange getIPRange(Player player) {
		return plugin.ipmap.find(getIP(player));
	}

	public IP getIP(Player player) {
		IP ip = null;
		String playerName = player.getDisplayName();
		try {
			ip = new IP(player.getAddress().getAddress().getHostAddress());
		} catch (Exception e) {
			plugin.getLogger().warning("Can't get IP Address from player : " + playerName);
		}
		return ip;
	}

	public boolean isPlayerWarned(Player player) {
		return isPlayerWarned(player.getName());
	}

	public boolean isPlayerWarned(String player) {
		return plugin.warnNames.contains(player);
	}
}
