package hide92795.bukkit.plugin.mcbansdetector;

import hide92795.bukkit.plugin.mcbansdetector.data.MCBansData;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class CheckMCBansThread extends Thread {
	private static final String REQUEST_URL = "http://api.mcbans.com/v2/";

	private MCBansDetector plugin;
	private String playerName;
	private String apikey;

	public CheckMCBansThread(MCBansDetector mcBansDetector, String playerName) {
		this.plugin = mcBansDetector;
		this.playerName = playerName;
		this.apikey = plugin.getConfig().getString("APIKey");
	}

	@Override
	public void run() {
		try {
			URL url = new URL(REQUEST_URL + apikey);
			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);

			OutputStream os = uc.getOutputStream();// POST用のOutputStreamを取得

			String postStr = "exec=playerLookup&player=" + playerName;// POSTするデータ
			PrintStream ps = new PrintStream(os);
			ps.print(postStr);// データをPOSTする
			ps.close();

			InputStream is = uc.getInputStream();// POSTした結果を取得
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String s;
			while ((s = reader.readLine()) != null) {
				JSONObject json = new JSONObject(s);
				int totalBan = (Integer) json.get("total");
				int reputation = (Integer) json.get("reputation");
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
				final MCBansData data = new MCBansData(date, playerName, totalBan, reputation);
				plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
					@Override
					public void run() {
						plugin.resultMCBans(playerName, data);
					}
				});
				break;
			}
			reader.close();
		} catch (Exception e) {
			plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.errorOnMCBans(playerName);
				}
			});
		}
	}
}
