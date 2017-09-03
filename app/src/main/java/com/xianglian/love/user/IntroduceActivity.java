package com.xianglian.love.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;
import com.xianglian.love.R;
import com.xianglian.love.config.Config;
import com.xianglian.love.utils.AppUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 个人介绍/情感经历
 */
public class IntroduceActivity extends BaseUserInfoActivity {

    private EditText mIntroduceView;

    //个人介绍
    public static final int INTRODUCE = 0;

    //情感经历
    public static final int EXPERIENCE = 1;

    //择偶要求
    public static final int CHOOSE_FRIEND_STANDARD = 2;

    private int mType;

    public static Intent getIntent(Context context, int type) {
        Intent intent = new Intent(context, IntroduceActivity.class);
        intent.putExtra(Config.INTRODUCE_KEY, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        mIntroduceView = (EditText) findViewById(R.id.et_introduce);
        mType = getIntent().getIntExtra(Config.INTRODUCE_KEY, INTRODUCE);
        String content = null;
        if (INTRODUCE == mType) {
            setupTitle(getString(R.string.my_introduce), getString(R.string.save));
            content = AppUtils.getIntroduce(this);
        } else if (EXPERIENCE == mType) {
            setupTitle(getString(R.string.experience_love), getString(R.string.save));
            content = AppUtils.getExperience(this);
        } else if (CHOOSE_FRIEND_STANDARD == mType) {
            setupTitle(getString(R.string.condition_friend), getString(R.string.save));
            content = AppUtils.getChooseMarry(this);
        }
        if (!TextUtils.isEmpty(content)) {
            mIntroduceView.setText(content);
        }

    }

    @Override
    public void rightClick() {
        String text = mIntroduceView.getText().toString();
        if (TextUtils.isEmpty(text)) {
            showToast(R.string.introduce_null);
        } else if (text.trim().length() < 10) {
            showToast(R.string.introduce_little);
        } else {
            updateContent(text);
        }
    }

    private void updateContent(final String content) {
        if (TextUtils.isEmpty(content)) return;
        doRequest(content);
    }

    private void doRequest(final String input) {
        dialogShow();
        final String url = Config.PATH + "user/set/basic-info";
        Map<String, String> params = new HashMap<>();
        params.put("uid", getUserId(this));
        switch (mType) {
            case INTRODUCE:
                params.put("person_intro", input);
                break;
            case EXPERIENCE:
                params.put("relationship_desc", input);
                break;
            case CHOOSE_FRIEND_STANDARD:
                params.put("mate_preference", input);
                break;
        }
        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(url).addHeads(getHeader()).addParams(params).build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        dialogDisMiss();
                        toast(getString(R.string.request_fail));
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        dialogDisMiss();
                        String result = info.getRetDetail();
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                String msg = obj.getString("msg");
                                int code = obj.getInt("code");
                                if (!TextUtils.isEmpty(msg)) {
                                    showToast(msg);
                                }
                                if (code == 0) {
                                    updateUserInfo(input);
                                    Intent intent = new Intent();
                                    intent.putExtra(Config.INTRODUCE_RESULT_KEY, input);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void updateUserInfo(String input) {
        switch (mType) {
            case INTRODUCE:
                AppUtils.parseData(this, "person_intro", input);
                break;
            case EXPERIENCE:
                AppUtils.parseData(this, "relationship_desc", input);
                break;
            case CHOOSE_FRIEND_STANDARD:
                AppUtils.parseData(this, "mate_preference", input);
                break;
        }
    }
}
