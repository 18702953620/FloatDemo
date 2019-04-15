package com.ch.floatview;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.ch.floatview.listener.FloatLifecycle;
import com.ch.floatview.listener.LifecycleListener;
import com.ch.floatview.result.ResultHelper;

/**
 * 作者： ch
 * 时间： 2019/4/12 0012-下午 3:52
 * 描述： 悬浮控件
 * 来源：
 */

public class FloatView {
    private WindowManager windowManager;
    private Builder builder;
    private WindowManager.LayoutParams layoutParams;
    private static FloatView INSTANCE;
    private FloatLifecycle floatLifecycle;
    private Context context;


    public static FloatView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FloatView();
        }
        return INSTANCE;
    }


    private void init(Builder builder) {
        this.builder = builder;
        this.context = builder.context.getApplicationContext();

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        layoutParams = new WindowManager.LayoutParams();

        layoutParams.format = PixelFormat.RGBA_8888;

        if (builder.width == 0) {
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.width = builder.width;
        }
        if (builder.height == 0) {
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.height = builder.height;
        }

        if (builder.gravity == -1) {
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        } else {
            layoutParams.gravity = builder.gravity;
        }

        layoutParams.y = builder.y;
        layoutParams.x = builder.x;

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        if (floatLifecycle == null) {
            floatLifecycle = new FloatLifecycle(context, builder.activities, new LifecycleListener() {
                @Override
                public void onShow() {
                    show();
                }

                @Override
                public void onHide() {
                    dismiss();
                }

                @Override
                public void onBackToDesktop() {
                    dismiss();
                }
            });

        }


    }

    private void addView() {
        if (builder.view.isAttachedToWindow()) {
            //如果已经添加过
            return;
        }

        windowManager.addView(builder.view, layoutParams);
    }

    public void show() {
        if (windowManager == null || builder == null || layoutParams == null) {
            return;
        }
        context.getApplicationContext().registerReceiver(floatLifecycle, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        //6.0之上 要判断权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                addView();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + builder.context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (builder.context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) builder.context;

                    ResultHelper.init(activity).startActivityForResult(intent, new ResultHelper.CallBack() {
                        @Override
                        public void onActivityResult(int resultCode, Intent data) {
                            addView();
                        }
                    });
                }
            }
        } else {
            addView();
        }
    }


    public void show(Builder builder) {
        if (builder == null) {
            return;
        }

        init(builder);
        show();
    }


    public void dismiss() {
        if (windowManager == null || builder == null || builder.view == null) {
            return;
        }
        //如果已经被移除
        if (!builder.view.isAttachedToWindow()) {
            return;
        }
        //移除广播
        context.getApplicationContext().unregisterReceiver(floatLifecycle);

        windowManager.removeView(builder.view);
    }


    public static class Builder {
        Context context;
        View view;
        int width;
        int height;
        int x;
        int y;
        int gravity = -1;
        Class[] activities;

        public Builder filter(Class... activities) {
            this.activities = activities;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder view(View view) {
            this.view = view;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder build() {
            if (context == null) {
                throw new NullPointerException("content is null");
            }
            return this;
        }


    }
}
