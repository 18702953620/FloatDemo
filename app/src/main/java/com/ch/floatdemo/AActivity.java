package com.ch.floatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.ch.floatview.FloatView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AActivity extends AppCompatActivity {

    @BindView(R.id.btn_a)
    Button btnA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_a)
    public void onViewClicked() {
        startActivity(new Intent(this, BActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();


        btnA.postDelayed(new Runnable() {
            @Override
            public void run() {
                FloatView.Builder builder = FloatView.getInstance().getBuilder();
                if (builder != null) {
                    FloatView.getInstance().updata(DensityUtils.dp2px(AActivity.this, 160), 500);
                }
            }
        }, 1000);


    }
}
