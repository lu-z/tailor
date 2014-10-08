package android.blaze.com.tailor.content;

import java.util.ArrayList;

/**
 * Created by jonwu on 10/5/14.
 */
public class ExpandedItem {
    ArrayList<String> logos;
    ArrayList<String> styles;
    Integer meanPrice;
    int layout;

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public ExpandedItem(){}
    public ExpandedItem(ArrayList<String> logos, ArrayList<String> styles, Integer meanPrice){
        this.logos = logos;
        this.styles = styles;
        this.meanPrice = meanPrice;
    }
    public ArrayList<String> getLogos() {
        return logos;
    }

    public void setLogos(ArrayList<String> logos) {
        this.logos = logos;
    }

    public ArrayList<String> getStyles() {
        return styles;
    }

    public void setStyles(ArrayList<String> styles) {
        this.styles = styles;
    }

    public Integer getMeanPrice() {
        return meanPrice;
    }

    public void setMeanPrice(Integer meanPrice) {
        this.meanPrice = meanPrice;
    }
}
