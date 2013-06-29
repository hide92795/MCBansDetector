package hide92795.bukkit.plugin.mcbansdetector;

import hide92795.bukkit.plugin.corelib.Localizable;

public enum Type implements Localizable {
	BLOCK_BREAK_MESSAGE("BlockBreakMessage"), PAGE("Page"), SHOW_LIST("ShowList"), SHOW_DETAIL("ShowDetail"), TOGGLE_PROTECT("ToggleProtect"), RELOAD_SETTING(
			"ReloadSetting"), NOT_FOUND_ID("NotFoundID"), NOT_FOUND_PAGE("NotFoundPage"), ENABLE_PROTECT("EnableProtect"), DISABLE_PROTECT(
			"DisableProtect"), RELOADED_SETTING("ReloadedSetting"), ERROR_RELOAD_SETTING("ErrorReloadSetting"), GET_NEXT_PAGE("GetNextPage"), NO_WARN_PLAYER_LOGIN(
			"NoWarnPlayerLogin"), WARN_PLAYER_LOGIN("WarnPlayerLogin"), PLEASE_CHECK_DETAIL("PleaseCheckDetail"), CURRENT_LOGIN_WARN_PLAYER(
			"CurrentLoginWarnPlayer"), LOGIN_BANED_PLAYER("LoginBanedPlayer"), PLAYERNAME("PlayerName"), BAN_COUNT("BanCount"), CANT_GET_BANDATA(
			"CantGetBanData"), PLEASE_CHECK_MANUALLY("PleaseCheckManually"), CONNTCT_FROM_WARN_COUNTRY("ConntctFromWarnCountry"), IP_ADDRESS(
			"IPAddress"), CONNECT_FROM("ConnectFrom"), LAST_CONNECTION("LastConnection"), WARN_PLAYER("WarnPlayer"), SIDEBAR_PLAYER_NAME(
			"SideBarPlayerName");
	private final String type;

	private Type(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type;
	}

}
