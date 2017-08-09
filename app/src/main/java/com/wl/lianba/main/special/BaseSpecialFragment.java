package com.wl.lianba.main.special;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.lianba.BaseFragment;
import com.wl.lianba.R;
import com.wl.lianba.main.special.adapter.SpecialAdapter;
import com.wl.lianba.main.special.model.SpecialInfo;
import com.wl.lianba.utils.CommonLinearLayoutManager;

/**
 * Created by wanglong on 17/3/11.
 * 专刊
 */

public class BaseSpecialFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private CommonLinearLayoutManager mLayoutManager;

    private SpecialAdapter mAdapter;

    private SpecialInfo mEntity;

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = mRecyclerView.getChildAdapterPosition(view);
            mEntity = mAdapter.getItem(position);

        }
    };


    public static BaseSpecialFragment newInstance() {
        BaseSpecialFragment fragment = new BaseSpecialFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_fragment_layout, null);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new CommonLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SpecialAdapter(getContext(), itemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        addData();
    }

    private void addData() {
        addDataByType(SpecialInfo.ViewType.BANNER);
        addDataByType(SpecialInfo.ViewType.ARTICLE);
        mAdapter.notifyDataSetChanged();
    }

    private void addDataByType(int type) {
        switch (type) {
            case SpecialInfo.ViewType.BANNER: {
                SpecialInfo info = new SpecialInfo();
                info.setViewType(type);
                mAdapter.getInfo().add(info);
                break;
            }
            case SpecialInfo.ViewType.ARTICLE: {
                SpecialInfo info = new SpecialInfo();
                info.setViewType(type);
                //测试用
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                break;
            }
        }

    }
}
