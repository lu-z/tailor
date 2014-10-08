package android.blaze.com.tailor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Item {

	String name;
	String brand;
	String price;
	String imageUrl;
	String desc;

	String[] colors;
	String[] types;
	String[] categories;
	String[] attributes;

	public Item(String name, String brand, String price, String[] colors, String[] types,
			String[] categories, String[] attributes, String imageUrl, String desc) {
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.colors = colors;
		this.types = types;
		this.categories = categories;
		this.attributes = attributes;
		this.imageUrl = imageUrl;
		this.desc = desc;
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("brand", brand);
		obj.put("price", price);
		obj.put("img", imageUrl);
		obj.put("description", desc);
		obj.put("colors", new JSONArray(Arrays.asList(colors)));
		obj.put("types", new JSONArray(Arrays.asList(types)));
		obj.put("categories", new JSONArray(Arrays.asList(categories)));
		obj.put("attributes", new JSONArray(Arrays.asList(attributes)));
		return obj;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String[] getColors() {
		return colors;
	}

	public void setColors(String[] colors) {
		this.colors = colors;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String[] getAttributes() {
		return attributes;
	}

	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
