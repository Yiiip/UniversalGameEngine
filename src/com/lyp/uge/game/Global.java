package com.lyp.uge.game;

import java.util.HashMap;
import java.util.Map;

public final class Global {

	private Global() {}
	
	public static boolean debug = true;
	public static boolean mdoe_polygon_view = true;
	public static boolean mode_culling_back = true;
	
	public static GlobalUser User = new GlobalUser();
	
	public static boolean loading(String filepath) {
		return false;
	}
	
	private static class GlobalUser {
		
		private static Map<String, Object> userGlobalData = new HashMap<>();
		
		private GlobalUser() {
		}
		
		public static void add(String key, Object value) {
			userGlobalData.put(key, value);
		}
		
		public static void get(String key) {
			userGlobalData.get(key);
		}
		
		public static void remove(String key) {
			if (userGlobalData.containsKey(key)) {
				userGlobalData.remove(key);
			}
		}
	}
}
