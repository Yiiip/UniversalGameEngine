package com.lyp.uge.game;

import java.util.HashMap;
import java.util.Map;

public final class Global {

	private Global() {}
	
	//Debug level
	public static boolean debug_log = true;
	//Logs in console
	public static boolean debug_fps = false;
	public static boolean debug_camera = true;
	public static boolean debug_light = false;
	public static boolean debug_keyboard = false;
	public static boolean debug_mouse = false;
	
	//Render Modes
	public static boolean mdoe_polygon_view = true;
	public static boolean mode_culling_back = true;
	
	//User global variables
	private static GlobalUser user = new GlobalUser();
	
	public static GlobalUser User() {
		return user;
	}

	public static Object User(String key) {
		if (user != null) {
			return GlobalUser.get(key);
		} else {
			return null;
		}
	}
	
	private static class GlobalUser {
		
		private static Map<String, Object> userGlobalData = new HashMap<>();
		
		private GlobalUser() {}
		
		@SuppressWarnings("unused")
		public static GlobalUser add(String key, Object value) {
			if (value instanceof GlobalUser) {
				return null;
			}
			userGlobalData.put(key, value);
			return user;
		}
		
		public static Object get(String key) {
			return userGlobalData.get(key);
		}
		
		@SuppressWarnings("unused")
		public static Object remove(String key) {
			if (userGlobalData.containsKey(key)) {
				return userGlobalData.remove(key);
			} else {
				return null;
			}
		}
	}
	
	public static boolean loadConfig(String configPath) {
		return false;
	}
}
