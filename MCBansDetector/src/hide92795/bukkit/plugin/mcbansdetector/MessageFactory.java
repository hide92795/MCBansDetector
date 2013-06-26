package hide92795.bukkit.plugin.mcbansdetector;

import hide92795.bukkit.plugin.mcbansdetector.data.MCBansData;
import ipx.IP;
import org.bukkit.ChatColor;

public class MessageFactory {
	public MCBansDetector plugin;

	public MessageFactory(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	protected String createMCBansMessageForConsoleLogger(String playerName, MCBansData data) {
		StringBuilder sb = new StringBuilder();
		sb.append("Player \"");
		sb.append(playerName);
		sb.append("\"'s result: ");
		sb.append("TotalBan:");
		sb.append(data.getTotalBan());
		sb.append(" Reputation:");
		sb.append(data.getReputation());
		return sb.toString();
	}

	protected String createMCBansMessageForOriginalLogger(String playerName, MCBansData data) {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		sb.append(" ");
		sb.append(playerName);
		sb.append(" | ");
		sb.append("TotalBan:");
		sb.append(data.getTotalBan());
		sb.append(" Reputation:");
		sb.append(data.getReputation());
		sb.append("\n");
		return sb.toString();
	}

	protected String createMCBansMessageForModerator(String playerName, MCBansData data) {
		StringBuilder sb = new StringBuilder();
		sb.append(plugin.localize.getString(Type.LOGIN_BANED_PLAYER));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), playerName));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.BAN_COUNT), data.getTotalBan()));
		sb.append("\n");

		sb.append(ChatColor.RED);
		sb.append("http://mcbans.com/player/" + playerName);
		return sb.toString();
	}

	protected String createMCBansErrorMessageForConsoleLogger(String playerName) {
		StringBuilder sb = new StringBuilder();
		sb.append("An Error has occurred getting data from MCBans.");
		sb.append(" Player:");
		sb.append(playerName);
		return sb.toString();
	}

	protected String createMCBansErrorMessageForOriginalLogger(String playerName) {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		sb.append(" ");
		sb.append(playerName);
		sb.append(" | ");
		sb.append("TotalBan:UNKNOWN Reputation:UNKNOWN");
		sb.append("\n");
		return sb.toString();
	}

	protected String createMCBansErrorMessageForModerator(String playerName) {
		StringBuilder sb = new StringBuilder();
		sb.append(plugin.localize.getString(Type.CANT_GET_BANDATA));
		sb.append("\n");

		sb.append(plugin.localize.getString(Type.PLEASE_CHECK_MANUALLY));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), playerName));
		sb.append("\n");

		sb.append(ChatColor.RED);
		sb.append("http://mcbans.com/player/" + playerName);
		return sb.toString();
	}

	protected String createCountryMessageForConsoleLogger(String playerName, String countryDesc) {
		StringBuilder sb = new StringBuilder();
		sb.append("Player \"");
		sb.append(playerName);
		sb.append("\" connect from ");
		sb.append(countryDesc);
		return sb.toString();
	}

	protected String createWarnCountryMessageForOriginalLogger(String playerName, String countryDesc) {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		sb.append(" ");
		sb.append(playerName);
		sb.append(" : ");
		sb.append("Connect from ");
		sb.append(countryDesc);
		sb.append("\n");
		return sb.toString();
	}

	protected String createWarnCountryMessageForModerator(String playerName, String countryDesc, IP ip) {
		StringBuilder sb = new StringBuilder();
		sb.append(plugin.localize.getString(Type.CONNTCT_FROM_WARN_COUNTRY));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), playerName));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.IP_ADDRESS), ip.toString()));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.CONNECT_FROM), countryDesc));
		return sb.toString();
	}
}
