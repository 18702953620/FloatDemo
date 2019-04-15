package com.ch.floatdemo;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ch.floatview.FloatView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_a)
    Button btnA;
    private static final int RC_STORAGE = 1001;
    @BindView(R.id.btn)
    Button btn;
    private FloatView floatView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        button = new Button(this);
        button.setText("我是悬浮框");
        button.setTextSize(18);
        button.setWidth(DensityUtils.dp2px(this, 100));
        button.setHeight(DensityUtils.dp2px(this, 50));
        button.setGravity(Gravity.CENTER);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatView.dismiss();
            }
        });
    }


    private void addFloat(Button button) {


        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();


        FloatView.Builder builder = new FloatView.Builder()
                .with(this)
                .view(button)
                .width(dm.widthPixels - DensityUtils.dp2px(this, 30))
                .y(DensityUtils.dp2px(this, 15))
                .build();
        FloatView.getInstance().show(builder);

    }


    @OnClick({R.id.btn, R.id.btn_a})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                addFloat(button);
                break;
            case R.id.btn_a:
                startActivity(new Intent(this, AActivity.class));
                break;
        }
    }

    @Override
    public void finish() {

        FloatView floatView = FloatView.getInstance();
        if (floatView != null) {
            floatView.dismiss();
        }
        super.finish();
    }
}
