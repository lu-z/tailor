package android.blaze.com.tailor.content;

import android.graphics.drawable.Drawable;

/**
 * Created by jonwu on 10/4/14.
 */
public class Items {
    String title;
    Drawable drawable;
    ExpandedItem expandedItem;

    public Items(){}

    public Items(String title, Drawable drawable, ExpandedItem expandedItem){
        this.title = title;
        this.drawable = drawable;
        this.expandedItem = expandedItem;
    }
    public ExpandedItem getExpandedItem() {
        return expandedItem;
    }

    public void setExpandedItem(ExpandedItem expandedItem) {
        this.expandedItem = expandedItem;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
