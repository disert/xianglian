package com.xianglian.love.library.photo.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.xianglian.love.library.photo.MediaChoseActivity;
import com.xianglian.love.library.ucrop.UCrop;

/**
 *
 */
public class PickConfig {

    public static int DEFAULT_SPAN_COUNT = 3;
    public static int DEFAULT_PICK_SIZE = 1;

    public static int MODE_SINGLE_PICK = 1;
    public static int MODE_MULTIP_PICK = 2;


    public final static int PICK_REQUEST_CODE = 10607;


    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_SPAN_COUNT = "extra_span_count";
    public final static String EXTRA_PICK_MODE = "extra_pick_mode";
    public final static String EXTRA_MAX_SIZE = "extra_max_size";
    public final static String EXTRA_UCROP_OPTIONS = "extra_ucrop_options";
    public final static String EXTRA_IS_NEED_CAMERA = "extra_isneed_camera";
    public final static String EXTRA_IS_NEED_CROP = "extra_isneed_crop";
    public final static String EXTRA_IS_SQUARE_CROP = "extra_issquare_crop";
    public final static String EXTRA_ACTION_BAR_COLOR = "extra_actionbar_color";
    public final static String EXTRA_STATUS_BAR_COLOR = "extra_status_bar_color";


    private int spanCount;
    private int pickMode;
    private int maxPickSize;
    private boolean isNeedCrop;
    private boolean isNeedCamera;
    private int actionBarColor;
    private int statusBarColor;
    private UCrop.Options options;
    private boolean isSqureCrop;

    private PickConfig(Activity context, PickConfig.Builder builder) {
        this.spanCount = builder.spanCount;
        this.pickMode = builder.pickMode;
        this.maxPickSize = builder.maxPickSize;
        this.isNeedCrop = builder.is_need_crop;
        this.isNeedCamera = builder.is_need_camera;
        this.statusBarColor = builder.statusBarColor;
        this.actionBarColor = builder.actionBarColor;
        this.options=builder.options;
        this.isSqureCrop=builder.isSquareCrop;


        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_STATUS_BAR_COLOR, this.statusBarColor);
        bundle.putInt(EXTRA_ACTION_BAR_COLOR, this.actionBarColor);
        bundle.putInt(EXTRA_SPAN_COUNT, this.spanCount);
        bundle.putParcelable(EXTRA_UCROP_OPTIONS,this.options);
        bundle.putInt(EXTRA_PICK_MODE, this.pickMode);
        bundle.putInt(EXTRA_MAX_SIZE, this.maxPickSize);
        bundle.putBoolean(EXTRA_IS_NEED_CAMERA, this.isNeedCamera);
        bundle.putBoolean(EXTRA_IS_NEED_CROP, this.isNeedCrop);
        bundle.putBoolean(EXTRA_IS_SQUARE_CROP,this.isSqureCrop);

        if (this.pickMode == MODE_MULTIP_PICK) {
            this.isNeedCrop = false;
        } else {
            this.maxPickSize = 1;
        }
        startPick(context, bundle);
    }

    private void startPick(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE, bundle);
        intent.setClass(context, MediaChoseActivity.class);
        context.startActivityForResult(intent, PICK_REQUEST_CODE);
    }


    public static class Builder {

        private Activity context;
        private int spanCount = DEFAULT_SPAN_COUNT;
        private int pickMode = MODE_SINGLE_PICK;
        private int maxPickSize = DEFAULT_PICK_SIZE;
        private boolean is_need_crop = false;
        private boolean is_need_camera = true;
        private boolean isSquareCrop =false;
        private int actionBarColor = Color.parseColor("#03A9F4");
        private int statusBarColor = Color.parseColor("#0288D1");

        private UCrop.Options options=null;





        public Builder(Activity context) {
            if (context == null) {
                throw new IllegalArgumentException("A non-null Context must be provided");
            }
            this.context = context;
        }

        public PickConfig.Builder spanCount(int spanCount) {
            this.spanCount = spanCount;
            if (this.spanCount == 0) {
                this.spanCount = DEFAULT_SPAN_COUNT;
            }
            return this;
        }


        /**
         * this methord must be last call
         *
         * @param pickMode
         * @return
         */
        public PickConfig.Builder pickMode(int pickMode) {
            this.pickMode = pickMode;
            return this;
        }

        public PickConfig.Builder maxPickSize(int maxPickSize) {
            this.maxPickSize = maxPickSize;
            if (this.maxPickSize == 0) {
                this.maxPickSize = DEFAULT_PICK_SIZE;
            }
            return this;
        }

        public PickConfig.Builder statusBarColor(int statusBarcolor) {
            this.statusBarColor = statusBarcolor;
            return this;
        }

        public PickConfig.Builder setUropOptions(UCrop.Options options) {
            this.options = options;
            return this;
        }


        public PickConfig.Builder actionBarColor(int actionBarColor) {
            this.actionBarColor = actionBarColor;
            return this;
        }

        public PickConfig.Builder isNeedCrop(boolean is_need_crop) {
            this.is_need_crop = is_need_crop;
            return this;
        }

        public PickConfig.Builder isSquareCrop(boolean isSquareCrop) {
            this.isSquareCrop = isSquareCrop;
            return this;
        }

        public PickConfig.Builder isNeedCamera(boolean is_need_camera) {
            this.is_need_camera = is_need_camera;
            return this;
        }


        public PickConfig build() {
            return new PickConfig(context, this);
        }
    }




}