package android.blaze.com.tailor.content;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Created by jonwu on 10/4/14.
 */
public class DropDownAnim extends Animation {
    private final int targetHeight;
    private final View view;
    private final boolean down;
    private final int start;


    public DropDownAnim(View view, int targetHeight, boolean down, int start) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.down = down;
        this.start = start;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (down) {
            newHeight = (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = (int) (targetHeight * (1 - interpolatedTime));
        }

        if(newHeight == 1000 && down) {

            view.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }else if (newHeight < start){
            view.getLayoutParams().height = start;
        } else {
            view.getLayoutParams().height = newHeight;
        }
        view.requestLayout();
    }


    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
