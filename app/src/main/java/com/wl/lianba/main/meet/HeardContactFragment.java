
package com.wl.lianba.main.meet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.wl.lianba.BaseFragment;
import com.wl.lianba.R;
import com.wl.lianba.main.home.been.PersonInfo;
import com.wl.lianba.main.meet.adapter.TalkMeetAdapter;
import com.wl.lianba.main.meet.model.MeetInfo;
import com.wl.lianba.utils.CommonLinearLayoutManager;
import com.wl.lianba.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我发出的、TA发出的
 */
public class HeardContactFragment extends BaseFragment implements OnClickListener{
    private CommonLinearLayoutManager mLayoutManager;

    private MyRecyclerView mRecyclerView;

    private List<PersonInfo> mEntities;

    private TalkMeetAdapter mAdapter;

    private MeetInfo mEntity;


    private OnClickListener mItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEntities == null) return;
            int position = mRecyclerView.getChildAdapterPosition(v);
            mEntity = mAdapter.getItem(position);

        }
    };

    public static HeardContactFragment newInstance() {
        HeardContactFragment fragment = new HeardContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_meet_fragment, null);
        mEntities = new ArrayList<>();
        mAdapter = new TalkMeetAdapter(getActivity(), mItemClickListener);
        this.setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        mRecyclerView = (MyRecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new CommonLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        addData();
    }

    private void addData() {
        addDataByType(MeetInfo.ViewType.COMMON_INFO);

    }

    private void addDataByType(int type) {
        switch (type) {
            case MeetInfo.ViewType.COMMON_INFO: {
//                mAdapter.getInfo().addAll(getData());
                MeetInfo info = new MeetInfo();
                info.setViewType(type);
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                mAdapter.getInfo().add(info);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEntities != null) {
            mEntities.clear();
        }
    }

    @Override
    public void onClick(View v) {
    }

}
