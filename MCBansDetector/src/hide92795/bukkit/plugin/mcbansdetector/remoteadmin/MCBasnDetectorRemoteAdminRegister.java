package hide92795.bukkit.plugin.mcbansdetector.remoteadmin;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.remoteadmin.RemoteAdmin;

public class MCBasnDetectorRemoteAdminRegister {
	public static void register(MCBansDetector plugin) {
		((RemoteAdmin) plugin.getServer().getPluginManager().getPlugin("RemoteAdmin")).getAPI().registerCreator(new MCBansDetectorInfoCreator(plugin));
	}
}
