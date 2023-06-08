package com.jinn.projecty.main.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinn.projecty.base.BaseFragment;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.adapter.BannerAdapter;
import com.jinn.projecty.main.adapter.MainAdapter;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.main.constant.Constant;
import com.jinn.projecty.main.model.NewsViewModel;
import com.jinn.projecty.main.model.ViewModelFactory;
import com.jinn.projecty.main.ui.widget.SimpleItemDecoration;
import com.jinn.projecty.utils.LogUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFragment extends BaseFragment<NewsViewModel> implements LifecycleOwner {

    private RecyclerView mRecycleView;
    private SmartRefreshLayout mRefreshLayout;
    private final String TAG = "NewsFragment";
    private String mNextPageUrl;
    private String mTabName;
    public static NewsFragment newInstance(Bundle bundle) {
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(getArguments()==null){
            mTabName = "tab_unknow";
        }else{
            mTabName = getArguments().getString("tab_name");
        }
        LogUtils.d(TAG,"onCreate:"+mTabName);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.d(TAG,"onCreateView,"+mTabName);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.d(TAG,"onHiddenChanged,"+mTabName+","+hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtils.d(TAG,"setUserVisibleHint,"+mTabName+",isVisibleToUser:"+isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        LogUtils.d(TAG,"onResume,"+mTabName);
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.d(TAG,"onPause,"+mTabName);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG,"onDestroy,"+mTabName);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    protected void initView(View view) {
        getLifecycle().addObserver(mViewModel);
        mRecycleView = view.findViewById(R.id.main_recycle_view);
        mRefreshLayout = view.findViewById(R.id.main_refresh_layout);
       // new PagerSnapHelper(PagerSnapHelper.GRAVITY_START).attachToRecyclerView(mRecycleView);
        mViewModel.getLiveDataShowLoading().postValue(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //合并adapter
        BannerAdapter adapter1 = new BannerAdapter(getActivity());
        MainAdapter adapter2 = new MainAdapter(getActivity());
        ConcatAdapter concatAdapter = new ConcatAdapter(adapter1,adapter2);
        mRecycleView.setAdapter(concatAdapter);
        mRecycleView.addItemDecoration(new SimpleItemDecoration(getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.getItemAnimator().setRemoveDuration(500);
        mViewModel.getVideoLists().observe(this, new Observer<RecommandDataBean>() {
            @Override
            public void onChanged(RecommandDataBean bean) {
                mNextPageUrl = bean.getNextPageUrl();
                LogUtils.d(TAG,"onChanged,"+bean.getNextPageUrl()+" ,isRecommand:"+bean.isRecommand());
                ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list = bean.getIssueList().get(0).getItemList();
                ArrayList<RecommandDataBean.IssueListBean.ItemListBean> bannerList = new ArrayList<>();
                ArrayList<RecommandDataBean.IssueListBean.ItemListBean> mainList = new ArrayList<>();
                if(!bean.isRecommand()){
                    for (RecommandDataBean.IssueListBean.ItemListBean listBean : list) {
                        if (TextUtils.equals(listBean.getType(), Constant.newsTypeBanner)) {
                            bannerList.add(listBean);
                        } else if (TextUtils.equals(listBean.getType(), Constant.newsTypeVideo)) {
                            mainList.add(listBean);
                        }
                    }
                    adapter1.initData(bannerList);
                    adapter2.initData(mainList);
                    mRefreshLayout.finishRefresh();
                }else{
                    for (RecommandDataBean.IssueListBean.ItemListBean listBean : list) {
                        if (TextUtils.equals(listBean.getType(), Constant.newsTypeBanner)) {
                            bannerList.add(listBean);
                        } else if (TextUtils.equals(listBean.getType(), Constant.newsTypeVideo)) {
                            mainList.add(listBean);
                        }
                    }
                 //   adapter1.updateData(bannerList);
                    adapter2.updateData(mainList);
                    mRefreshLayout.finishLoadMore();
                }

            }
        });
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d(TAG,"load more:");
                mViewModel.requestMoreData(mNextPageUrl);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d(TAG,"onRefresh");
                mViewModel.requestMainData();
            }
        });
        mViewModel.requestMainData();
    }

    @Override
    protected Class onBindViewModel() {
        return NewsViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory onBindViewModelFactory() {
        return (ViewModelProvider.Factory) ViewModelFactory.getInstance(getActivity().getApplication());
    }
}
