package hide92795.bukkit.plugin.mcbansdetector.data;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;

public abstract class Data {
	private final String date;
	private final String playerName;

	public Data(String date, String playerName) {
		this.date = date;
		this.playerName = playerName;
	}

	public String getDate() {
		return date;
	}

	public String getPlayerName() {
		return playerName;
	}

	public abstract StringBuilder getDescription(MCBansDetector plugin);

	public abstract String getType();
}
