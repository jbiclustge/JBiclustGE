package jbiclustge.utils.osystem.progcheck;

import java.util.HashMap;

public class JGESetupSettingsContainer {
	
	HashMap<String, Object> mapsettings=new HashMap<>();
	
	
	public JGESetupSettingsContainer addSetting(String key, Object settingobj) {
		mapsettings.put(key, settingobj);
		return this;
	}
	
	
	public Object getSetting(String key) {
		return mapsettings.get(key);
	}
	
	public boolean containsSetting(String key) {
		return mapsettings.containsKey(key);
	}
	
	public void removeSetting(String key) {
		if(containsSetting(key))
			mapsettings.remove(key);
	}
	

}
