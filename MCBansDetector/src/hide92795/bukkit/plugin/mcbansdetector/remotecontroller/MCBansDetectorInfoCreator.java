package hide92795.bukkit.plugin.mcbansdetector.remotecontroller;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.remotecontroller.api.AdditionalInfo;
import hide92795.bukkit.plugin.remotecontroller.api.AdditionalInfoCreator;

public class MCBansDetectorInfoCreator implements AdditionalInfoCreator {
	private MCBansDetector plugin;

	public MCBansDetectorInfoCreator(MCBansDetector plugin) {
		this.plugin = plugin;
	}

	@Override
	public AdditionalInfo createAdditionalInfo() {
		return new AdditionalInfo("MCBansDetector", plugin.getLocalizedMessageForRemoteAdminAdditionalInfo());
	}
}
