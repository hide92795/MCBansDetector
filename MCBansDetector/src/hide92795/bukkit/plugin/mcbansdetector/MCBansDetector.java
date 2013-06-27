package hide92795.bukkit.plugin.mcbansdetector;

import hide92795.bukkit.plugin.corelib.Localize;
import hide92795.bukkit.plugin.corelib.Usage;
import hide92795.bukkit.plugin.mcbansdetector.data.Data;
import hide92795.bukkit.plugin.mcbansdetector.data.MCBansData;
import hide92795.bukkit.plugin.mcbansdetector.data.MCBansErrorData;
import hide92795.bukkit.plugin.mcbansdetector.data.WarnCountryData;
import hide92795.bukkit.plugin.mcbansdetector.listener.PlayerBlockBreakListener;
import hide92795.bukkit.plugin.mcbansdetector.listener.PlayerLoginListener;
import ipx.IP;
import ipx.IPMap;
import ipx.IPRange;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCBansDetector extends JavaPlugin {
	protected Logger logger;
	private PrintWriter printwriter;
	protected IPMap ipmap;
	private List<String> countryList;
	private HashMap<Integer, Data> warnData;
	protected HashSet<String> warnNames;
	private MCBansDetectorAPI api;
	private MessageFactory messageFactory;
	public boolean enableProtect;
	private Usage usage;
	public Localize localize;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		logger = getLogger();
		api = new MCBansDetectorAPI(this);
		messageFactory = new MessageFactory(this);
		localize = new Localize(this);
		warnData = new HashMap<>();
		warnNames = new HashSet<>();
		try {
			reload();
		} catch (Exception e1) {
			logger.severe("Error has occured on loading config.");
		}

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new PlayerLoginListener(this), this);
		pm.registerEvents(new PlayerBlockBreakListener(this), this);


		try {
			// Log
			File dir = new File("MCBansDetector_log");
			if (!dir.exists()) {
				dir.mkdir();
			}

			StringBuilder sb = new StringBuilder();
			sb.append("MCBansDetector");
			sb.append("_");
			sb.append(Utils.getCurrentTime("yyyy-MM-dd HH_mm_ss"));
			sb.append(".txt");

			logger.info("Log file is " + sb.toString());

			File file = new File(dir, sb.toString());
			printwriter = new PrintWriter(file, "UTF-8");
		} catch (Exception e) {
			logger.severe("Error on creating log file.");
			e.printStackTrace();
		}

		try {
			// IPMapping
			ipmap = new IPMap(getResource("ip_country_mapping.csv"));
		} catch (Exception e) {
			logger.severe("Error on loading ipmap from file.");
			e.printStackTrace();
		}
		logger.info("MCBansDetector enabled!");
	}

	private void createUsage() {
		usage = new Usage(this);
		usage.addCommand("/mcbansdetector-list [" + localize.getString(Type.PAGE) + "]",
				localize.getString(Type.SHOW_LIST));
		usage.addCommand("/mcbansdetector-show <ID>", localize.getString(Type.SHOW_DETAIL));
		usage.addCommand("/mcbansdetector-protect", localize.getString(Type.TOGGLE_PROTECT));
		usage.addCommand("/mcbansdetector-reload", localize.getString(Type.RELOAD_SETTING));
	}

	@Override
	public void onDisable() {
		printwriter.close();
		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "mcbansdetector":
			sender.sendMessage(usage.toString());
			break;
		case "mcbansdetector-list":
			if (args.length == 0) {
				sendListPage(sender, 1);
			} else if (args.length == 1) {
				String page_s = args[0];
				int page = 0;
				try {
					page = Integer.parseInt(page_s);
				} catch (Exception e) {
					sender.sendMessage(usage.toString());
					return true;
				}
				sendListPage(sender, page);
			}
			break;
		case "mcbansdetector-show":
			if (args.length == 1) {
				int id;
				try {
					// ID
					id = Integer.parseInt(args[0]);
				} catch (Exception e) {
					sender.sendMessage(usage.toString());
					return true;
				}
				Data data = warnData.get(id);
				if (data != null) {
					sendData(sender, data);
				} else {
					sender.sendMessage(localize.getString(Type.NOT_FOUND_ID));
				}
			} else {
				sender.sendMessage(usage.toString());
				return true;
			}
			break;
		case "mcbansdetector-protect":
			if (enableProtect) {
				enableProtect = false;
				sender.sendMessage(localize.getString(Type.DISABLE_PROTECT));
			} else {
				enableProtect = true;
				sender.sendMessage(localize.getString(Type.ENABLE_PROTECT));
			}
			break;
		case "mcbansdetector-reload":
			try {
				reload();
				sender.sendMessage(localize.getString(Type.RELOADED_SETTING));
				logger.info("Reloaded successfully.");
			} catch (Exception e) {
				sender.sendMessage(localize.getString(Type.ERROR_RELOAD_SETTING));
			}

			break;
		default:
			break;
		}
		return true;
	}

	private void reload() throws Exception {
		reloadConfig();
		try {
			localize.reload(getConfig().getString("Language"));
		} catch (Exception e1) {
			logger.severe("Can't load language file.");
			try {
				localize.reload("jp");
				logger.severe("Loaded default language file.");
			} catch (Exception e) {
				throw e;
			}
		}
		enableProtect = getConfig().getBoolean("ProtectModeOnLaunch");
		countryList = getConfig().getStringList("Country");
		if (countryList == null) {
			countryList = new ArrayList<>();
			getConfig().set("Country", countryList);
		}

		StringBuilder sb1 = new StringBuilder();
		sb1.append("Warning countries : ");
		for (Object object : countryList) {
			sb1.append(object.toString());
			sb1.append(", ");
		}
		sb1.delete(sb1.length() - 2, sb1.length() - 1);
		logger.info(sb1.toString());
		createUsage();
	}

	private void sendData(CommandSender sender, Data data) {
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.YELLOW);
		sb.append("=========================");
		sb.append("\n");
		sb.append(data.getDescription(this));
		sb.append(ChatColor.YELLOW);
		sb.append("=========================");
		sender.sendMessage(sb.toString());
	}

	private void sendListPage(CommandSender sender, int page) {
		if (page > 0) {
			int allPage = (warnData.size() / 10) + 1;
			if (allPage < page) {
				sender.sendMessage(localize.getString(Type.NOT_FOUND_PAGE));
				return;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.YELLOW);
			sb.append("===========");
			sb.append(ChatColor.AQUA);
			sb.append(page);
			sb.append("/");
			sb.append(allPage);
			sb.append(ChatColor.YELLOW);
			sb.append("===========");
			sb.append("\n");

			sb.append("ID  Type     Name");
			sb.append("\n");
			for (int i = 0; i < 10; i++) {
				int id = (page - 1) * 10 + i + 1;
				Data data = warnData.get(id);
				if (data == null) {
					continue;
				}
				String name = data.getPlayerName();

				if (name == null) {
					break;
				}

				sb.append(ChatColor.RED);
				sb.append(id);
				if (id < 10) {
					sb.append(" ");
				}
				sb.append("  ");
				sb.append(data.getType());
				sb.append("  ");
				sb.append(name);
				sb.append("\n");
			}
			sb.append(ChatColor.YELLOW);
			sb.append("=========================");

			if (page != allPage) {
				sb.append("\n");
				sb.append(localize.getString(Type.GET_NEXT_PAGE));
				sb.append(ChatColor.GOLD);
				sb.append("/mcbansdetector-list ");
				sb.append(page + 1);
			}
			sender.sendMessage(sb.toString());
		} else {
			sender.sendMessage(usage.toString());
		}
	}


	public void checkMCBans(String playerName) {
		logger.info("Starting to check \"" + playerName + "\" from MCBans");
		getServer().getScheduler().runTask(this, new CheckMCBansThread(this, playerName));
	}

	public synchronized void resultMCBans(String playerName, MCBansData data) {
		logger.info(messageFactory.createMCBansMessageForConsoleLogger(playerName, data));
		if (data.getTotalBan() != 0) {
			warnData.put(warnData.size() + 1, data);
			warnNames.add(playerName);
			printwriter.write(messageFactory.createMCBansMessageForOriginalLogger(playerName, data));
			sendMessageToOperator(messageFactory.createMCBansMessageForModerator(playerName, data));
		}
	}


	public synchronized void errorOnMCBans(String playerName) {
		logger.info(messageFactory.createMCBansErrorMessageForConsoleLogger(playerName));
		warnData.put(warnData.size() + 1, new MCBansErrorData(Utils.getCurrentTime("yyyy/MM/dd HH:mm:ss"), playerName));
		warnNames.add(playerName);
		printwriter.write(messageFactory.createMCBansErrorMessageForOriginalLogger(playerName));
		sendMessageToOperator(messageFactory.createMCBansErrorMessageForModerator(playerName));
	}


	public void checkCountry(Player player) {
		String playerName = player.getName();
		IP ip = api.getIP(player);
		IPRange range = api.getIPRange(player);
		if (range != null) {
			String countryDesc = range.getId() + " (" + range.getCountryName() + ")";
			logger.info(messageFactory.createCountryMessageForConsoleLogger(playerName, countryDesc));

			if (countryList.contains(range.getId())) {
				// Warn
				WarnCountryData data = new WarnCountryData(Utils.getCurrentTime("yyyy/MM/dd HH:mm:ss"), playerName,
						countryDesc, ip.toString());
				warnData.put(warnData.size() + 1, data);
				warnNames.add(playerName);

				printwriter.write(messageFactory.createWarnCountryMessageForOriginalLogger(playerName, countryDesc));
				sendMessageToOperator(messageFactory.createWarnCountryMessageForModerator(playerName, countryDesc, ip));
			}
		}
	}


	private void sendMessageToOperator(String msg) {
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if (player.hasPermission("mcbansdetector.notice") || player.isOp()) {
				player.sendMessage(msg);
			}
		}
	}

	public void checkCurrentWarnPlayerForModerator(Player player) {
		if (player.hasPermission("mcbansdetector.notice") || player.isOp()) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.RED);
			sb.append("[MCBansDetector]");
			if (warnData.size() == 0) {
				sb.append(localize.getString(Type.NO_WARN_PLAYER_LOGIN));
			} else {
				StringBuilder currentLogin = getCurrentLoginPlayersName();

				sb.append(String.format(localize.getString(Type.WARN_PLAYER_LOGIN), warnData.size()));
				sb.append("\n");
				sb.append(String.format(localize.getString(Type.PLEASE_CHECK_DETAIL), "/mcbansdetector-list"));
				if (currentLogin.length() != 0) {
					sb.append("\n");
					sb.append(localize.getString(Type.CURRENT_LOGIN_WARN_PLAYER));
					sb.append("\n");
					sb.append(currentLogin);
				}

			}
			player.sendMessage(sb.toString());
		}
	}

	private StringBuilder getCurrentLoginPlayersName() {
		StringBuilder sb = new StringBuilder();
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			String name = player.getName();
			if (warnNames.contains(name)) {
				sb.append(", ");
				sb.append(ChatColor.YELLOW);
				sb.append(name);
				sb.append(ChatColor.RED);
			}
		}
		if (sb.length() != 0) {
			sb.delete(0, 2);
		}
		return sb;
	}

	public MCBansDetectorAPI getAPI() {
		return api;
	}

}
