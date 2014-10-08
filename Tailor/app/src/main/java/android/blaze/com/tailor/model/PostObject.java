package android.blaze.com.tailor.model;

import org.json.JSONObject;

public class PostObject {


	JSONObject obj;
	int weight;
	
	public PostObject(JSONObject obj, int weight) {
		this.obj = obj;
		this.weight = weight;
	}

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
