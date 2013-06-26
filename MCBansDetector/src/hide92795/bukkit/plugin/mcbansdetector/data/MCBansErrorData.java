package hide92795.bukkit.plugin.mcbansdetector.data;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;
import org.bukkit.ChatColor;

public class MCBansErrorData extends Data {

	public MCBansErrorData(String date, String name) {
		super(date, name);
	}

	@Override
	public StringBuilder getDescription(MCBansDetector plugin) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format(plugin.localize.getString(Type.LAST_CONNECTION), getDate()));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), getPlayerName()));
		sb.append("\n");

		sb.append(ChatColor.RED);
		sb.append("http://mcbans.com/player/" + getPlayerName());
		sb.append("\n");
		return sb;
	}

	@Override
	public String getType() {
		return "B_ERROR";
	}
}
