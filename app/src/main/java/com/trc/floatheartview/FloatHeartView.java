package com.trc.floatheartview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by trc on 2018/4/11.
 */

public class FloatHeartView extends RelativeLayout implements onAddFloatHeartListener{
    public FloatHeartView(Context context) {
        super(context);
        init(context);
    }

    public FloatHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FloatHeartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {

    }

    @Override
    public void onAddFloatHeartListener(ImageView iv) {

    }
}
