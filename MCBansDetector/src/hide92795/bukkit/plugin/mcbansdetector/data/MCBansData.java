package hide92795.bukkit.plugin.mcbansdetector.data;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;
import org.bukkit.ChatColor;

public class MCBansData extends Data {
	private final int totalBan;
	private final int reputation;

	public MCBansData(String date, String name, int totalBan, int reputation) {
		super(date, name);
		this.totalBan = totalBan;
		this.reputation = reputation;
	}

	@Override
	public StringBuilder getDescription(MCBansDetector plugin) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(plugin.localize.getString(Type.LAST_CONNECTION), getDate()));
		sb.append("\n");
		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), getPlayerName()));
		sb.append("\n");
		sb.append(String.format(plugin.localize.getString(Type.BAN_COUNT), getTotalBan()));
		sb.append("\n");
		sb.append(ChatColor.RED);
		sb.append("http://mcbans.com/player/" + getPlayerName());
		sb.append("\n");
		return sb;
	}

	public int getTotalBan() {
		return totalBan;
	}

	public int getReputation() {
		return reputation;
	}

	@Override
	public String getType() {
		return "BAN    ";
	}
}
