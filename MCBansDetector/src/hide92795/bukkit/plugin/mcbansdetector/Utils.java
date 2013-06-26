package hide92795.bukkit.plugin.mcbansdetector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static String getCurrentTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}
}
