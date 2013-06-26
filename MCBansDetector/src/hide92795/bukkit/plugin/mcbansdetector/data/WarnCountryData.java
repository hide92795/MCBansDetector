package hide92795.bukkit.plugin.mcbansdetector.data;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.mcbansdetector.Type;

public class WarnCountryData extends Data {
	private final String country;
	private final String ip;

	public WarnCountryData(String date, String name, String country, String ip) {
		super(date, name);
		this.country = country;
		this.ip = ip;


	}

	@Override
	public StringBuilder getDescription(MCBansDetector plugin) {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format(plugin.localize.getString(Type.LAST_CONNECTION), getDate()));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.PLAYERNAME), getPlayerName()));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.CONNECT_FROM), getCountry()));
		sb.append("\n");

		sb.append(String.format(plugin.localize.getString(Type.IP_ADDRESS), getIp()));
		sb.append("\n");
		return sb;
	}

	public String getCountry() {
		return country;
	}

	public String getIp() {
		return ip;
	}

	@Override
	public String getType() {
		return "COUNTRY";
	}
}
