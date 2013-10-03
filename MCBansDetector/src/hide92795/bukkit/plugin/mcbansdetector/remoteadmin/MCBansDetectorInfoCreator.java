package hide92795.bukkit.plugin.mcbansdetector.remoteadmin;

import hide92795.bukkit.plugin.mcbansdetector.MCBansDetector;
import hide92795.bukkit.plugin.remoteadmin.api.AdditionalInfo;
import hide92795.bukkit.plugin.remoteadmin.api.AdditionalInfoCreator;

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
