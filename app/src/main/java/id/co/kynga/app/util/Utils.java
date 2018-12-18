package id.co.kynga.app.util;

import id.co.kynga.app.KyngaApp;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Utils {
	public static String getMACAddress(String interfaceName) {
		try {
			final List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null) return "";
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) KyngaApp.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static boolean wifiEnabled() {
		WifiManager wifi = (WifiManager) KyngaApp.context.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()) {
			return true;
		}
		return false;
	}
}