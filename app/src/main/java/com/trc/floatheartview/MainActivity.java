package com.trc.floatheartview;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.trc.floatheart.FloatHeartView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView, imageView1, imageView2;
    private FloatHeartView floatHeartView, floatHeartView1, floatHeartView2;
    private int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatHeartView = this.findViewById(R.id.fhv);
        imageView = findViewById(R.id.tagIv);
        floatHeartView1 = this.findViewById(R.id.fhv1);
        imageView1 = findViewById(R.id.tagIv1);
        floatHeartView2 = this.findViewById(R.id.fhv2);
        imageView2 = findViewById(R.id.tagIv2);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatHeartView1.addFloatHeart(imageView1, R.mipmap.heart4);
            }
        });
        autoAdd();
    }

    private void autoAdd() {
        final int WHAT = 102;
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT:
                        floatHeartView.addFloatHeart(imageView, R.mipmap.heart3);
                        break;
                    case 103:
                        floatHeartView2.addFloatHeart(imageView2, getResId());
                        break;
                }
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = WHAT;
                message.obj = System.currentTimeMillis();
                handler.sendMessage(message);
            }
        };
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 103;
                message.obj = System.currentTimeMillis();
                handler.sendMessage(message);
            }
        };

        Timer timer = new Timer();
        // 参数：
        // 1000，延时1秒后执行。
        // 2000，每隔2秒执行1次task。
        timer.schedule(task, 1000, 200);
        new Timer().schedule(task1, 1000, 100);
    }

    /***
     * 随机获取图片
     * @return
     */
    public int getResId() {
        int min = 1;
        int max = 6;
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        switch (num) {
            case 1:
                return R.mipmap.heart1;
            case 2:
                return R.mipmap.heart2;
            case 3:
                return R.mipmap.heart3;
            case 4:
                return R.mipmap.heart4;
            case 5:
                return R.mipmap.heart5;
            case 6:
                return R.mipmap.heart6;
        }
        return R.mipmap.heart1;
    }
}
