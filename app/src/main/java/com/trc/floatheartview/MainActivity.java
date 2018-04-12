package com.trc.floatheartview;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatHeartView floatHeartView = this.findViewById(R.id.fhv);
        final ImageView imageView = findViewById(R.id.tagIv);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatHeartView.addFloatHeart(imageView, R.mipmap.heart3);
            }
        });
    }

}
