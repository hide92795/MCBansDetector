package hide92795.bukkit.plugin.mcbansdetector.remotecontroller;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.remotecontroller.RemoteController;

public class MCBasnDetectorRemoteControllerRegister {
	public static void register(MCBansDetector plugin) {
		((RemoteController) plugin.getServer().getPluginManager().getPlugin("RemoteController")).getAPI().registerCreator(
				new MCBansDetectorInfoCreator(plugin));
	}
}
