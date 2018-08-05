package com.wl.appchat.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.TIMUserProfile;
import com.wl.appchat.MyApplication;
import com.wl.appchat.R;

/**
 * 好友资料
 */
public class FriendProfile implements ProfileSummary {


    private TIMUserProfile profile;
    private boolean isSelected;

    public FriendProfile(TIMUserProfile profile) {
        this.profile = profile;
    }


    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.drawable.head_other;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        if (!profile.getFaceUrl().equals("")) {
            return profile.getFaceUrl();
        }
        return null;
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        System.out.println("------>>> getIdentifier " + profile.getIdentifier() + ", remark = " + profile.getRemark() + ", nickname = " + profile.getNickName());
        if (!profile.getRemark().equals("")) {
            return profile.getRemark();
        } else if (!profile.getNickName().equals("")) {
            return profile.getNickName();
        }
        String text = profile.getIdentifier();
        if (!TextUtils.isEmpty(text)) {
            String[] str = text.split("-");
            if (str.length > 1) {
                return str[1];
            }
        }
        return text;
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        //todo
//        if (FriendshipInfo.getInstance().isFriend(profile.getIdentifier())){
//            ProfileActivity.navToProfile(context, profile.getIdentifier());
//        }else{
//            Intent person = new Intent(context,AddFriendActivity.class);
//            person.putExtra("id",profile.getIdentifier());
//            person.putExtra("name",getName());
//            context.startActivity(person);
//        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 获取用户ID
     */
    @Override
    public String getIdentify() {
        return profile.getIdentifier();
    }


    /**
     * 获取用户备注名
     */
    public String getRemark() {
        return profile.getRemark();
    }


    /**
     * 获取好友分组
     */
    public String getGroupName() {
        if (profile.getFriendGroups().size() == 0) {
            return MyApplication.getContext().getString(R.string.default_group_name);
        } else {
            return profile.getFriendGroups().get(0);
        }
    }

}
