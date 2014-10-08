package android.blaze.com.tailor.content;

import android.app.Activity;
import android.blaze.com.tailor.R;
import android.blaze.com.tailor.http.HttpRequestController;
import android.blaze.com.tailor.model.Item;
import android.blaze.com.tailor.model.PostObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SelectionActivity extends Activity {

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    ImageView ivItem;
    TextView tvItemName;
    View vHeader;
    LinearLayout lvFooter;
    ImageView ivLike;
    ImageView ivDislike;

    ArrayList<View> overlays = new ArrayList<View>();
    ArrayList<View> choices = new ArrayList<View>();

    // animation
    Animation fadeIn;
    Animation fadeOut;

    final Handler handler = new Handler();

    // Timers
    Timer choicesTimer = null;


    ArrayList<Item> items = new ArrayList<Item>();
    int currentItem = 0;
    MenuListAdapter adapter;
    boolean hideOverlay = false;
    boolean isFooterCollapsed = true;
//    String endpoint = "http://192.168.1.101:5000/";
//    String endpoint = "http://192.168.1.110:5000/";
      String endpoint = "http://lit-tundra-8985.herokuapp.com/";

    private static final String DEFAULT_SEARCH_ITEM = "dresses";
    private static final int NUM_LOCAL_ITEMS = 10;
    HttpRequestController http;
    RelativeLayout testLayout;
    Boolean isHeaderCollapsed = true;
    ImageView homebar;
    ImageView backbar;
    MenuListAdapter menuListAdapter;
    ArrayList<Items> itemsModel;
    private ExpandableListView expandableListView;
    ExpandedItem expandedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        ivItem = (ImageView) findViewById(R.id.iv_item);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        ivDislike = (ImageView) findViewById(R.id.iv_dislike);
        tvItemName = (TextView) findViewById(R.id.tv_item_name);
        lvFooter = (LinearLayout) findViewById(R.id.lv_footer);
        vHeader = (View) findViewById(R.id.homebar);

        overlays.add(lvFooter);
        overlays.add(vHeader);

        choices.add(ivLike);
        choices.add(ivDislike);

        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        ivItem.setOnTouchListener(gestureListener);

        http = new HttpRequestController();
        // fetch batch of items from server
        new FetchItemsFromServer(ivItem).execute(DEFAULT_SEARCH_ITEM);

        // jon stuff

        testLayout = (RelativeLayout) findViewById(R.id.testLayout);
        homebar = (ImageView) findViewById(R.id.homebar);
        backbar = (ImageView) findViewById(R.id.backbar);

        itemsModel = new ArrayList<Items>();
        expandedItem = new ExpandedItem();

        expandedItem.setLayout(R.layout.favorite_item);
        itemsModel.add(new Items("Top Brands", getResources().getDrawable(
                R.drawable.favorites), expandedItem));

        expandedItem = new ExpandedItem();
        expandedItem.setLayout(R.layout.style_item);
        itemsModel.add(new Items("Styles", getResources().getDrawable(
                R.drawable.settings), expandedItem));

        expandedItem = new ExpandedItem();
        expandedItem.setLayout(R.layout.budgeting_item);
        itemsModel.add(new Items("Budgeting", getResources().getDrawable(
                R.drawable.budgeting), expandedItem));


        menuListAdapter = new MenuListAdapter(this, itemsModel);
        expandableListView = (ExpandableListView) findViewById(R.id.elvMenu);
        expandableListView.setAdapter(menuListAdapter);
        GetStats getStats = new GetStats();
        getStats.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyGestureDetector extends SimpleOnGestureListener {

        // variables for thresholds
        private double cumulativeDist = 0;
        private boolean allowIncrement = true;
        // swipe right moves screen left = YES. Swipe left moves screen right =
        // NO.
        private static final int SWIPE_RIGHT_THRESHOLD = 400;
        private static final int SWIPE_LEFT_THRESHOLD = -400;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (allowIncrement)
                cumulativeDist += distanceX;
            // start showing the dislike
            if (cumulativeDist > 0) {
                double val = (cumulativeDist / SWIPE_RIGHT_THRESHOLD);
                ((View) ivDislike).setAlpha((float) val);
                if (cumulativeDist >= SWIPE_RIGHT_THRESHOLD) {
                    ((View) ivDislike).setAlpha(1);
                    doDislike();
                    cumulativeDist = 0;
                    allowIncrement = false;
                }
            } else if (cumulativeDist < 0) {
                // start showing the like
                double val = cumulativeDist / SWIPE_LEFT_THRESHOLD;
                ((View) ivLike).setAlpha((float) val);
                if (cumulativeDist <= SWIPE_LEFT_THRESHOLD) {
                    ((View) ivLike).setAlpha(1);
                    doLike();
                    cumulativeDist = 0;
                    allowIncrement = false;
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAndHideOverlays(overlays);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            showAndHideOverlays(overlays);
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            showAndHideOverlays(overlays);
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            cumulativeDist = 0;
            allowIncrement = true;
            return true;
        }

        private void doLike() {
            PostObject postObj;
            try {
                if (currentItem < items.size()) {
                    postObj = new PostObject(items.get(currentItem)
                            .toJSONObject(), 1);
                    // show next photo and send post request to server with item
                    // and weight
                    new PostItemToServer(ivItem).execute(postObj);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void doDislike() {
            PostObject postObj;
            try {
                if (currentItem < items.size()) {
                    postObj = new PostObject(items.get(currentItem)
                            .toJSONObject(), -1);
                    // show next photo and send post request to server with item
                    // and weight
                    new PostItemToServer(ivItem).execute(postObj);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                return true;
            case (MotionEvent.ACTION_MOVE):
                return true;
            case (MotionEvent.ACTION_UP):
                hideChoices(choices);
                return true;
            case (MotionEvent.ACTION_CANCEL):
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void hideChoices(ArrayList<View> views) {
        // hide the choices
        if (choicesTimer != null) {
            choicesTimer.cancel();
            choicesTimer.purge();
        }
        choicesTimer = new Timer();
        choicesTimer.schedule(new ChoicesTask(views), 500);
    }

    private void showAndHideOverlays(ArrayList<View> views) {
        if (!hideOverlay) {
            for (View v : views) {
                Animation fadeIn = new AlphaAnimation(v.getAlpha(), 1);
                fadeIn.setInterpolator(new AccelerateInterpolator());
                fadeIn.setDuration(500);
                fadeIn.setAnimationListener(new FadeAnimationListener(v, true));
                v.startAnimation(fadeIn);
            }
            hideOverlay = true;
        } else {
            for (View v : views) {
                Animation fadeOut = new AlphaAnimation(v.getAlpha(), 0);
                fadeOut.setInterpolator(new DecelerateInterpolator());
                fadeOut.setDuration(500);
                fadeOut.setAnimationListener(new FadeAnimationListener(v, false));
                v.startAnimation(fadeOut);
            }
            if (!isHeaderCollapsed) {
                collapseHeader();
            }
            if (!isFooterCollapsed) {
                collapseFooter();
            }
            hideOverlay = false;
        }
    }

    private class ChoicesTask extends TimerTask {
        ArrayList<View> views;

        public ChoicesTask(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    for (View v : views) {
                        Animation fadeOut = new AlphaAnimation(v.getAlpha(), 0);
                        fadeOut.setInterpolator(new DecelerateInterpolator());
                        fadeOut.setDuration(500);
                        fadeOut.setAnimationListener(new FadeAnimationListener(
                                v, false));
                        v.startAnimation(fadeOut);
                    }
                }
            });
        }
    }

    private class FadeAnimationListener implements AnimationListener {
        View v;
        boolean fadeIn;

        public FadeAnimationListener(View v, boolean fadeIn) {
            this.v = v;
            this.fadeIn = fadeIn;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (fadeIn) {
                v.setAlpha(1);
            } else {
                v.setAlpha(0);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }

    private class PostItemToServer extends AsyncTask<PostObject, Void, String> {

        ImageView v;
        String postURL = endpoint + "update";

        public PostItemToServer(ImageView v) {
            this.v = v;
        }

        @Override
        protected String doInBackground(PostObject... params) {
            // create a map of the values in postObject
            PostObject obj = params[0];
            HashMap<String, JSONObject> post = new HashMap<String, JSONObject>();
            post.put("item", obj.getObj());
            JSONObject weight = new JSONObject();
            try {
                weight.put("weight", obj.getWeight());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            post.put("weight", weight);
            http.postJSONObject(postURL, post);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // move to the next image or fetch new images if the array is empty.
            currentItem++;
            if (currentItem < NUM_LOCAL_ITEMS && currentItem < items.size()) {
                new DownloadImageTask(v).execute(items.get(currentItem)
                        .getImageUrl());
                tvItemName.setText(items.get(currentItem).getName());
            } else {
                items.clear();
                new FetchItemsFromServer(ivItem).execute(DEFAULT_SEARCH_ITEM);
            }
            GetStats getStats = new GetStats();
            getStats.execute();
        }

    }

    private class FetchItemsFromServer extends AsyncTask<String, Void, String> {

        ImageView v;
        // TODO: Need to change during event!
        String fetchBase = endpoint;

        public FetchItemsFromServer(ImageView v) {
            this.v = v;
        }

        @Override
        protected String doInBackground(String... itemTypes) {
            // TODO Auto-generated method stub
            String endpoint = itemTypes[0];
            String fetchURL = fetchBase + endpoint;
            try {
                JSONArray arr = new JSONArray(http.get(fetchURL));
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    String name = obj.getString("name");
                    String brand = obj.getString("brand");
                    String price = obj.getString("price");
                    String imageUrl = obj.getString("img");
                    String desc = obj.getString("description");
                    // colors
                    JSONArray jsonColors = (JSONArray) obj.get("colors");
                    String[] colors = new String[jsonColors.length()];
                    for (int j = 0; j < jsonColors.length(); j++) {
                        colors[j] = (String) jsonColors.get(j);
                    }
                    // types
                    JSONArray jsonTypes = (JSONArray) obj.get("types");
                    String[] types = new String[jsonTypes.length()];
                    for (int j = 0; j < jsonTypes.length(); j++) {
                        types[j] = (String) jsonTypes.get(j);
                    }
                    // categories
                    JSONArray jsonCats = (JSONArray) obj.get("categories");
                    String[] cats = new String[jsonCats.length()];
                    for (int j = 0; j < jsonCats.length(); j++) {
                        cats[j] = (String) jsonCats.get(j);
                    }
                    // attributes
                    JSONArray jsonAttrs = (JSONArray) obj.get("attributes");
                    String[] attrs = new String[jsonAttrs.length()];
                    for (int j = 0; j < jsonAttrs.length(); j++) {
                        attrs[j] = (String) jsonAttrs.get(j);
                    }

                    Item item = new Item(name, brand, price, colors, types,
                            cats, attrs, imageUrl, desc);
                    items.add(item);
                }
            } catch (JSONException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            currentItem = 0;
            if (currentItem < items.size()) {
                new DownloadImageTask(v).execute(items.get(currentItem)
                        .getImageUrl());
                tvItemName.setText(items.get(currentItem).getName());
            }

        }
    }

    private class GetStats extends AsyncTask<String, Void, String> {
        public GetStats() {

        }

        @Override
        protected String doInBackground(String... itemTypes) {

            // TODO Auto-generated method stub
            try {
                String url = endpoint+"stats";
                http = new HttpRequestController();
                String response = http.get(url);
                System.out.println(response);
                return response;
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!= null){
                processAfterGetStats(result);
            }

        }
    }
    public void processAfterGetStats(String result){
        try {

            JSONObject jObject = new JSONObject(result);
            JSONArray jArrayBrands = jObject.getJSONArray("top_brands");
            ArrayList<String> logos = new ArrayList<String>();
            ArrayList<String> styles = new ArrayList<String>();
            for (int i = 0; i < jArrayBrands.length() ; i++) {
                logos.add(jArrayBrands.getString(i));
            }
            JSONArray jArrayStyles = jObject.getJSONArray("top_styles");
            for (int i = 0; i < jArrayStyles.length() ; i++) {
                styles.add(jArrayStyles.getString(i));
            }
            Integer meanPrice = jObject.getInt("mean_price");
            ExpandedItem expandedItem;
            for (int i = 0; i < menuListAdapter.getItemsModel().size(); i++) {
                expandedItem = (ExpandedItem) menuListAdapter.getItemsModel().get(i).getExpandedItem();
                expandedItem.setLogos(logos);
                expandedItem.setStyles(styles);

                expandedItem.setMeanPrice(meanPrice);
            }
            menuListAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Write to list view - Projects Activity");
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void expandButton(View view) {
        expandHeader();
        if (!isFooterCollapsed)
            collapseFooter();
    }

    public void collapseButton(View view) {
        collapseHeader();
    }

    public void footerExpand(View view) {
        if (isFooterCollapsed) {
            expandFooter();
            if (!isHeaderCollapsed)
                collapseHeader();
        } else {
            collapseFooter();
        }
    }

    public void expandFooter() {
        DropDownAnim dropDownAnim = new DropDownAnim(lvFooter, 900, true, 230);
        dropDownAnim.setDuration((int) (1000 / lvFooter.getContext()
                .getResources().getDisplayMetrics().density));
        lvFooter.startAnimation(dropDownAnim);
        isFooterCollapsed = false;
    }

    public void collapseFooter() {
        DropDownAnim dropDownAnim = new DropDownAnim(lvFooter, 900, false, 230);
        dropDownAnim.setDuration((int) (1000 / lvFooter.getContext()
                .getResources().getDisplayMetrics().density));
        lvFooter.startAnimation(dropDownAnim);
        isFooterCollapsed = true;
    }

    public void expandHeader() {
        DropDownAnim dropDownAnim = new DropDownAnim(testLayout, 1000, true, 0);
        dropDownAnim.setDuration((int) (1000 / testLayout.getContext()
                .getResources().getDisplayMetrics().density));
        testLayout.startAnimation(dropDownAnim);
        homebar.setVisibility(View.GONE);
        backbar.setVisibility(View.VISIBLE);
        isHeaderCollapsed = false;
    }

    public void collapseHeader() {
        DropDownAnim dropDownAnim = new DropDownAnim(testLayout, 1000, false, 0);
        dropDownAnim.setDuration((int) (1000 / testLayout.getContext()
                .getResources().getDisplayMetrics().density));
        testLayout.startAnimation(dropDownAnim);
        backbar.setVisibility(View.GONE);
        homebar.setVisibility(View.VISIBLE);
        isHeaderCollapsed = true;
    }
}
