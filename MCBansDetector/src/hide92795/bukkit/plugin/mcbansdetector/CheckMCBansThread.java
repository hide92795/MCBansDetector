package hide92795.bukkit.plugin.mcbansdetector;

import hide92795.bukkit.plugin.mcbansdetector.data.MCBansData;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class CheckMCBansThread extends Thread {
	private static final String REQUEST_URL = "http://api.mcbans.com/v3/";

	private MCBansDetector plugin;
	private String playerName;
	private String player_uuid;
	private String apikey;

	public CheckMCBansThread(MCBansDetector mcBansDetector, String playerName, String player_uuid) {
		this.plugin = mcBansDetector;
		this.playerName = playerName;
		this.player_uuid = player_uuid;
		this.apikey = plugin.getConfig().getString("APIKey").trim();
	}

	@Override
	public void run() {
		OutputStreamWriter wr = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(REQUEST_URL + apikey);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(5000);
			uc.setReadTimeout(5000);
			uc.setDoOutput(true);

			wr = new OutputStreamWriter(uc.getOutputStream());// POST用のOutputStreamWriterを取得

			String postStr = createPostData();

			wr.write(postStr);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = reader.readLine()) != null) {
				sb.append(s);
			}

			JSONObject json = new JSONObject(sb.toString());
			int totalBan = (Integer) json.get("total");
			double reputation = Double.parseDouble(json.get("reputation").toString());
			String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
			final MCBansData data = new MCBansData(date, playerName, totalBan, reputation);
			plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.resultMCBans(playerName, data);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					plugin.errorOnMCBans(playerName);
				}
			});
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (Exception e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private String createPostData() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(URLEncoder.encode("exec", "UTF-8"));
		sb.append("=");
		sb.append(URLEncoder.encode("playerLookup", "UTF-8"));
		sb.append("&");
		sb.append(URLEncoder.encode("player", "UTF-8"));
		sb.append("=");
		sb.append(URLEncoder.encode(playerName, "UTF-8"));
		sb.append("&");
		sb.append(URLEncoder.encode("player_uuid", "UTF-8"));
		sb.append("=");
		sb.append(URLEncoder.encode(player_uuid, "UTF-8"));
		sb.append("&");
		sb.append(URLEncoder.encode("admin", "UTF-8"));
		sb.append("=");
		sb.append(URLEncoder.encode(plugin.admin.getName(), "UTF-8"));
		return sb.toString();
	}
}
