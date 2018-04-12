package com.trc.floatheart;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Random;


/**
 * Created by 差不多的TRC on 2018/4/11.
 * <p>
 * 如果你项目用到这个View或者喜欢这个简单的逻辑实现飘心效果 麻烦给我start 算是鼓励我一下>_<
 * <p>
 * 实现逻辑：
 * step1: 用贝塞尔曲线好画出个二阶曲线，贝塞尔曲线很简单的，二阶曲线为例：
 * 个人理解，就是一根线 有两个点画在线的两侧，两个点好比两个人，用力拉这根线，
 * 把这个跟线拉两道弯，变成了贝塞尔曲线，几阶曲线就几个点。当然了，具体实现也很简单，
 * 还是二阶曲线为例：首先画笔moveTo某个点上 然后cubicTo 把两个点坐标和重点坐标写进去
 * ，就完成你的贝塞尔曲线路径了；
 * <p>
 * step2:曲线画完了，我在想怎么让一个图片在我规定的路径上运动，我如果知道这个Path的每个
 * 点的坐标就好了，然后我就可以把图片每隔一段时间更新下坐标位置，这样不就形成动画了吗，
 * 这时想到了PathMeasure，Android SDK提供了一个非常有用的API来帮助开发者实现这样一个
 * Path路径点的坐标追踪，这个类就是PathMeasure，它可以认为是一个Path的坐标计算器。
 * <p>
 * step3:哈哈，很开心，路径的坐标已经拿到手了，接下来就是每隔一段时更新坐标就好，第一反应想用Handler做个
 * 计时器，然后每隔几毫秒更新坐标，仔细思考下，太麻烦，而且不可控因素太多，然后想到了ValueAnimator
 * 这个不就是为我而生吗，哈哈，于是开始干活，在onAnimationUpdate 方法里更新坐标。嗯，效果出来了
 * 好不错，{自己认为还不错，哈哈} 。
 * <p>
 * step4:优化动画效果，首先想到透明度，这个简单,用AnimatorSet 来个组合（透明度和坐标更新同时） so easy。
 * <p>
 * step5:继续优化动画，这个路径也太单一了吧，全是一个路径上，看起来有点傻不拉几的，所以把路径那两个控制点的和终点x
 * y坐标在一定范围内随机
 * <p>
 * step6 努力，终有一天你得到你想要的生活!!!（>_<）
 */

public class FloatHeartView extends RelativeLayout {
    private Context context;
    private RelativeLayout FRl;
    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;
    private float firstControlX = 0;
    private float firstControlY = 0;
    private float secondControlX = 0;
    private float secondControlY = 0;
    private float animHeight = 500;
    private float animWidth = 200;
    private PathMeasure mPathMeasure;
    private Path path;
    private PackageManager mpm;
    private float[] mCurrentPosition = new float[2];
    private ImageView iv;
    private int tagIconWidth;
    private int tagIconHeight;
    private Drawable tagIconSrc;
    private int floatIconWidth;
    private int floatIconHeight;
    private Drawable floatIconSrc;
    private int animMaxWidth;
    private int animMinWidth;
    private int animMaxHeight;
    private int animMinHeight;
    private int animDuration;

    public FloatHeartView(Context context) {
        super(context);
        init(context);
    }

    public FloatHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatHeartViewAttr);
        /**tag图片宽高 src*/
        tagIconWidth = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_tagIconWidth,
                DisplayUtil.dip2px(context, 20));
        tagIconHeight = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_tagIconHeight,
                DisplayUtil.dip2px(context, 20));
        tagIconSrc = mTypedArray.getDrawable(R.styleable.FloatHeartViewAttr_tagIconSrc);
        /**飘图片宽高 src*/
        floatIconWidth = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_floatIconWidth,
                DisplayUtil.dip2px(context, 20));
        floatIconHeight = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_floatIconHeight,
                DisplayUtil.dip2px(context, 20));
        floatIconSrc = mTypedArray.getDrawable(R.styleable.FloatHeartViewAttr_floatIconSrc);
        /**动画宽度 范围*/
        animMinWidth = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_animMaxWidth,
                DisplayUtil.dip2px(context, -60));
        /***/
        animMaxWidth = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_animMinWidth,
                DisplayUtil.dip2px(context, 120));
        /** 动画高度范围*/
        animMaxHeight = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_animMaxHeight,
                DisplayUtil.dip2px(context, 333));
        /***/
        animMinHeight = (int) mTypedArray.getDimension(R.styleable.FloatHeartViewAttr_animMinHeight,
                DisplayUtil.dip2px(context, 233));
        /**动画时长*/
        animDuration = mTypedArray.getInteger(R.styleable.FloatHeartViewAttr_animDuration, 1500);

        //获取资源后要及时回收
        mTypedArray.recycle();
        init(context);
    }

    public FloatHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.float_heart_view_customtitle, this, true);
        iv = findViewById(R.id.iv);
        iv.setImageDrawable(tagIconSrc);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        params.width = DisplayUtil.dip2px(context, tagIconWidth);
        params.height = DisplayUtil.dip2px(context, tagIconHeight);
        iv.setLayoutParams(params);
        FRl = findViewById(R.id.rl);
    }

    public void addFloatHeart(ImageView startLocationIv, int redId) {
        addFHeart(startLocationIv, redId2Drawable(redId));
    }

    public void addFloatHeart(int redId) {
        addFHeart(this.iv, redId2Drawable(redId));
    }

    public void addFloatHeart() {
        addFHeart(this.iv, floatIconSrc);
    }


    private void addFHeart(ImageView startLocationIv, Drawable floatDrawable) {
        int min = animMinWidth;
        int max = animMaxWidth;
        Random random = new Random();

        animWidth = random.nextInt(max) % (max - min + 1) + min;
        int min1 = animMinHeight;
        int max1 = animMaxHeight;
        Random random1 = new Random();
        animHeight = random1.nextInt(max1) % (max1 - min1 + 1) + min1;


        final ImageView _floatIv = new ImageView(context);
        _floatIv.setImageDrawable(floatDrawable);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(floatIconWidth, floatIconHeight);
        params.width = DisplayUtil.dip2px(context, floatIconWidth);
        params.height = DisplayUtil.dip2px(context, floatIconHeight);
        FRl.addView(_floatIv, params);

        int[] parentLocation = new int[2];
        FRl.getLocationInWindow(parentLocation);

        //得到起始图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        startLocationIv.getLocationInWindow(startLoc);

//        三、正式开始计算动画开始/结束的坐标
        //起始点：图片起始点-父布局起始点+该图片的一半
        startX = startLoc[0] - parentLocation[0];
        startY = startLoc[1] - parentLocation[1];

        endX = startX;
        endY = startY - animHeight;
        firstControlX = startX - animWidth;
        firstControlY = startY - animHeight * 3 / 8;
        secondControlX = startX + animWidth;
        secondControlY = startY - animHeight * 5 / 8;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, endX, endY);


        final PathMeasure mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）

        AnimatorSet bouncer = new AnimatorSet();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        ObjectAnimator anim = ObjectAnimator.ofFloat(_floatIv, "alpha", 1f, 0.1f);
        // 匀速线性插值器
        bouncer.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                Log.d("FloatHeartView", "value:" + value);
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的图片（动画图片）的坐标设置为该中间点的坐标
                _floatIv.setTranslationX(mCurrentPosition[0]);
                _floatIv.setTranslationY(mCurrentPosition[1]);
            }
        });

        bouncer.play(valueAnimator).with(anim);
        bouncer.setDuration(animDuration);
        bouncer.start();

        bouncer.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
//                // 把移动的图片imageview从父布局里移除
                FRl.removeView(_floatIv);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public Drawable redId2Drawable(int redId) {
        Resources resources = context.getResources();
        Drawable drawable = resources.getDrawable(redId);
        return drawable;
    }


    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childView
            if (i == 0 || i == 1) {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 2 || i == 3) {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 0 || i == 2) {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

            if (i == 1 || i == 3) {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    // abstract method in viewgroup
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;

            cl = cParams.leftMargin;
            ct = cParams.topMargin;

            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }

    }

}
