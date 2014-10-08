package android.blaze.com.tailor.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class User {
	HashMap<String, Integer> preferences;
	

	public User(HashMap<String,Integer> preferences) {
		this.preferences = preferences;
	}
	
	// construct a new User object using dictionary from server.
	public User(JSONObject obj) throws JSONException {
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			String nextKey = (String) iter.next();
			preferences.put(nextKey, obj.getInt(nextKey));
		}
	}
	
	public HashMap<String, Integer> getPreferences() {
		return preferences;
	}
	
	
}
