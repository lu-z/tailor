package android.blaze.com.tailor.content;

import android.blaze.com.tailor.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonwu on 10/4/14.
 */
public class MenuListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    public ArrayList<Items> itemsModel;
    private LayoutInflater inflater;

    public MenuListAdapter(Context context, ArrayList<Items> itemsModel) {
        this.context = context;
        this.itemsModel = itemsModel;
        inflater = LayoutInflater.from(context);

    }

    public ArrayList<Items> getItemsModel() {
        return itemsModel;
    }

    public void setItemsModel(ArrayList<Items> itemsModel) {
        this.itemsModel = itemsModel;
    }

    @Override
    public int getGroupCount() {
        return itemsModel.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // Add content to Parent List View
    @Override

    public View getGroupView(int i, boolean b, View rowView, ViewGroup viewGroup) {

        rowView = inflater.inflate(R.layout.parent_item, viewGroup, false);
        TextView mTitle = (TextView) rowView.findViewById(R.id.tvTitle);

        Items item = itemsModel.get(i);
        mTitle.setText(item.getTitle());
        mTitle.setCompoundDrawablesWithIntrinsicBounds(item.getDrawable(),null,null,null);



        return rowView;
    }

    @Override
    public View getChildView(final int i, int i2, boolean b, View view, ViewGroup viewGroup) {

        ExpandedItem expandedItem = (ExpandedItem)itemsModel.get(i).getExpandedItem();
        System.out.println(expandedItem.getLayout());
        view = inflater.inflate(expandedItem.getLayout(), viewGroup, false);
        if(i ==0) {
            TextView tvFirstBrand = (TextView) view.findViewById(R.id.tvFirstBrand);
            TextView tvSecondBrand = (TextView) view.findViewById(R.id.tvSecondBrand);
            TextView tvThirdBrand = (TextView) view.findViewById(R.id.tvThirdBrand);
            ArrayList<TextView> topBrandsTextViews = new ArrayList<TextView>();
            topBrandsTextViews.add(tvFirstBrand);
            topBrandsTextViews.add(tvSecondBrand);
            topBrandsTextViews.add(tvThirdBrand);
            ArrayList<String> topBrands = expandedItem.getLogos();
            for (int j = 0; j < topBrands.size(); j++) {

                topBrandsTextViews.get(j).setText(topBrands.get(j));
            }
        }else if(i == 1){
            TextView tvFirstStyle = (TextView) view.findViewById(R.id.tvFirstStyle);
            TextView tvSecondStyle = (TextView) view.findViewById(R.id.tvSecondStyle);
            TextView tvThirdStyle = (TextView) view.findViewById(R.id.tvThirdStyle);
            ArrayList<TextView> topStylesTextViews = new ArrayList<TextView>();
            topStylesTextViews.add(tvFirstStyle);
            topStylesTextViews.add(tvSecondStyle);
            topStylesTextViews.add(tvThirdStyle);
            ArrayList<String> topStyles = expandedItem.getStyles();
            for (int j = 0; j < topStyles.size(); j++) {
                topStylesTextViews.get(j).setText(topStyles.get(j));
            }
        }else if(i==2){
            TextView tvMeanPrice = (TextView) view.findViewById(R.id.tvMeanPrice);
            Integer price = expandedItem.getMeanPrice();
            tvMeanPrice.setText("$"+price.toString());
        }



        System.out.println(expandedItem.getMeanPrice());

        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

}


