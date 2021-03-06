package com.xianglian.love.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.orhanobut.hawk.Hawk;
import com.wl.appcore.utils.AppUtils2;
import com.xianglian.love.BaseListFragment;
import com.xianglian.love.MainActivity;
import com.xianglian.love.R;
import com.xianglian.love.config.Config;
import com.wl.appcore.Keys;
import com.xianglian.love.loadmore.CustomLoadMoreView;
import com.xianglian.love.main.home.adapter.HomeAdapter;
import com.wl.appcore.entity.UserEntity;
import com.xianglian.love.net.JsonCallBack;
import com.xianglian.love.user.LoginActivity;
import com.xianglian.love.user.been.ItemInfo;
import com.xianglian.love.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.DateUtils2;


/**
 * Created by wanglong on 17/3/11.
 * 首页
 */

public class BaseHomeFragment extends BaseListFragment implements BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private HomeAdapter mAdapter;

    private String mSex;

    private List<UserEntity> mUserEntities = new ArrayList<>();

    public static BaseHomeFragment newInstance() {
        return new BaseHomeFragment();
    }

    private void setupView(View view) {
        setupRecyclerView(view);
        mAdapter = new HomeAdapter(getContext(), mUserEntities);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onRefresh2(false);
            }
        }, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefresh2(true);
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh2(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSex = Hawk.get(Keys.SEX);
        mSex = Keys.TYPE_FEMALE.equals(mSex) ? Keys.TYPE_MALE : Keys.TYPE_FEMALE;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        setupView(view);
        return view;
    }

    public void onRefresh2(boolean refresh) {
        onRefresh2(refresh, getParams());
    }

    private void onRefresh2(final boolean refresh, Map<String, String> params) {
        String url;
        if (!refresh && TextUtils.isEmpty(mNextUrl)) {
            return;
        }
        if (!refresh && !TextUtils.isEmpty(mNextUrl)) {
            url = mNextUrl;
        } else {
            url = Config.PATH + "users";
        }
        final GetRequest<UserEntity> request = OkGo.get(url);
        request.headers("Authorization", AppUtils.getToken(getContext()));
        if (params != null && params.size() > 0) {
            request.params(params);
        }
        request.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST);
        request.execute(new JsonCallBack<UserEntity>(UserEntity.class) {
            @Override
            public void onSuccess(Response<UserEntity> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                System.out.print("response&&& " + response);
                if (response != null && response.body() != null) {
                    UserEntity userEntity = response.body();
                    System.out.print("response&&&2 " + userEntity);
                    if (userEntity == null) return;
                    if (userEntity.getCount() <= 0) {
                        mAdapter.setNewData(null);
                        mAdapter.setEmptyView(emptyView);
                        return;
                    }
                    mNextUrl = userEntity.getNext();
                    System.out.print("response&&&3 " + mNextUrl);
                    List<UserEntity> userEntities = userEntity.getResults();
                    System.out.print("response&&&4 " + userEntities);
                    dealItemData(userEntities, refresh);
                    if (TextUtils.isEmpty(mNextUrl)) {
                        mAdapter.loadMoreEnd(refresh);
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
            }

            @Override
            public void onCacheSuccess(Response<UserEntity> response) {
                super.onCacheSuccess(response);
                if (response != null && response.body() != null && refresh) {
                    List<UserEntity> userEntities = response.body().getResults();
                    dealItemData(userEntities, true);
                }
            }

            @Override
            public void onError(Response<UserEntity> response) {
                super.onError(response);
                mSwipeRefreshLayout.setRefreshing(false);
                if (refresh) {
                    mAdapter.setEmptyView(errorView);
                } else {
                    toast("请求失败");
                }
            }
        });
    }

    private List<UserEntity> getUserEntities(List<UserEntity> entities) {
        List<UserEntity> adapterList = mAdapter.getData();

        List<UserEntity> entityList = new ArrayList<>();
        List<UserEntity> topList = new ArrayList<>();
        List<UserEntity> sortList = new ArrayList<>();
        List<UserEntity> normalList = new ArrayList<>();
        for (UserEntity entity : entities) {
            if (entity == null
                    /*|| TextUtils.isEmpty(entity.getPic1())*/
                    || "1".equals(entity.getBlack_user())) {
                continue;
            }
            if (entity.getIs_top() == 1) {
                topList.add(entity);
            } else if (entity.getPosition() <= 0) {
                normalList.add(entity);
            } else {
                sortList.add(entity);
            }
        }
        adapterList.addAll(0, topList);
        adapterList.addAll(normalList);
        if (!sortList.isEmpty()) {
            for (UserEntity userEntity : sortList) {
                if (userEntity == null) continue;
                adapterList.add(userEntity.getPosition(), userEntity);
            }
        }
        mAdapter.notifyDataSetChanged();
        return entityList;
    }

    private void dealItemData(List<UserEntity> userEntities, boolean refresh) {
        if (userEntities == null || userEntities.size() == 0) return;
        if (refresh) mUserEntities.clear();
        for (UserEntity entity : userEntities) {
            entity.setViewType(UserEntity.TYPE_NORMAL);
        }
        getUserEntities(userEntities);
//        if (refresh) {
//            mAdapter.setNewData(userEntities);
//        } else {
//            mAdapter.addData(userEntities);
//        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!AppUtils.isLogin(getContext())) {
            startActivity(LoginActivity.getIntent(getContext()));
            return;
        } else if (!TextUtils.isEmpty(AppUtils2.isCompleteData())) {
//            startActivity(MainActivity.getIntent(getContext(), MainActivity.TAB_MY));
            if (getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showToast(AppUtils2.isCompleteData());
//                ((MainActivity) getActivity()).dealJumpTab(MainActivity.TAB_MY);
            }
            return;
        }
        UserEntity info = mAdapter.getItem(position);
        if (info != null) {
            Intent intent = PersonDetailActivity.getIntent(getContext(), info.getId());
            if (getContext() != null)
                getContext().startActivity(intent);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != MainActivity.REQUEST_CODE_SEARCH || data == null) {
            return;
        }
        int searchType = data.getIntExtra(Config.EXTRA_SEARCH_TYPE, 0);
        if (searchType == 1) {
            Map<String, String> map = getParams();
            if (map == null) return;
            if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(true);
            onRefresh2(true, getParams());
        }
    }

    private Map<String, String> getParams() {
        List<ItemInfo> itemInfoList = Hawk.get(Keys.SEARCH_INFO_LIST);
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(mSex)) {
            map.put("gender", mSex);
        }
        if (itemInfoList != null && !itemInfoList.isEmpty()) {
            for (ItemInfo item : itemInfoList) {
                if (item == null) continue;
                if (AppUtils.stringToInt(item.getMin_age()) >= 0) {
                    map.put("max_age", DateUtils2.getBirthday(item.getMin_age()) + "");
                }
                if (AppUtils.stringToInt(item.getMax_age()) > 0) {
                    map.put("min_age", DateUtils2.getBirthday(item.getMax_age()) + "");
                }

                if (AppUtils.stringToInt(item.getMin_height()) >= 0) {
                    map.put("min_height", item.getMin_height());
                }
                if (AppUtils.stringToInt(item.getMax_height()) > 0) {
                    map.put("max_height", item.getMax_height());
                }

                if (item.getMin_education() >= 0) {
                    map.put("min_education", String.valueOf(item.getMin_education()));
                }

                if (item.getMax_education() > 0) {
                    map.put("max_education", String.valueOf(item.getMax_education()));
                }

                if (!TextUtils.isEmpty(item.getWork_area_code()) && item.getWork_area_code().length() > 4) {
                    map.put("work_area_code", item.getWork_area_code().substring(0, 4));
                }

                if (!TextUtils.isEmpty(item.getBorn_area_code()) && item.getBorn_area_code().length() > 4) {
                    map.put("born_area_code", item.getBorn_area_code().substring(0, 4));
                }
            }
            return map;
        }
        return map;
    }
}
