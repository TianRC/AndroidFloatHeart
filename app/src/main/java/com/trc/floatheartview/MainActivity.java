package com.trc.floatheartview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    RelativeLayout rl;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = findViewById(R.id.rl);
        iv = findViewById(R.id.iv);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart(iv);
            }
        });
    }

    private void addCart(ImageView iv) {
        int min = -150;
        int max = 400;
        Random random = new Random();
        animWidth = random.nextInt(max) % (max - min + 1) + min;
        int min1 = 699;
        int max1 = 999;
        Random random1 = new Random();
        animHeight = random1.nextInt(max1) % (max1 - min1 + 1) + min1;


        Log.d("SplashActivity", "animWidth:" + animWidth);

        final ImageView goods = new ImageView(MainActivity.this);
        goods.setImageDrawable(iv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        rl.addView(goods, params);

        int[] parentLocation = new int[2];
        rl.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);
        Log.d("SplashActivity", "startLoc:" + startLoc[0]);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        iv.getLocationOnScreen(endLoc);
        Log.d("SplashActivity", "endLoc:" + endLoc[0]);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
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
        ObjectAnimator anim = ObjectAnimator.ofFloat(goods, "alpha", 1f, 0.1f);
//        valueAnimator.setDuration(2500);
        // 匀速线性插值器
        bouncer.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
//        valueAnimator.start();


        bouncer.play(valueAnimator).with(anim);
        bouncer.setDuration(1500);
        bouncer.start();
//      六、动画结束后的处理
        bouncer.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
//                i++;
//                count.setText(String.valueOf(i));
//                // 把移动的图片imageview从父布局里移除
                rl.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
