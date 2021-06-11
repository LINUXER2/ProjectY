package com.jinn.projecty.main.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.main.constant.Constant;
import com.jinn.projecty.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jinnlee on 2021/6/9.
 */

public class BannerAdapter extends ListAdapter<RecommandDataBean.IssueListBean.ItemListBean, BannerAdapter.ViewHolderBanner> {
    private final String TAG = "BannerAdapter";
    private Activity mActivity;
    private ArrayList<RecommandDataBean.IssueListBean.ItemListBean>mLists = new ArrayList<>();

    public BannerAdapter(Activity activity){
        super(new BannerAdapterDiffCallBack());
        mActivity = activity;

    }

    public void initData(ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list) {
        mLists.clear();
        mLists.addAll(list);
        LogUtils.d(TAG,"initData:size"+mLists.size());
        submitList(mLists);
    }

    public void updateData(ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list){
        mLists.clear();
        mLists.addAll(list);
        submitList(mLists);
    }


    @Override
    public void submitList(@Nullable List<RecommandDataBean.IssueListBean.ItemListBean> list) {
        super.submitList(list);
    }

    @Override
    protected RecommandDataBean.IssueListBean.ItemListBean getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public ViewHolderBanner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_banner,parent,false);
        return new ViewHolderBanner(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBanner holder, int position) {
        String newsTitle = mLists.get(position).getData().getTitle();
        String newsImageUrl = mLists.get(position).getData().getImage();
        LogUtils.d(TAG,"onBindViewHolder:video image:"+newsImageUrl);
        ((ViewHolderBanner) holder).mTitle.setText(newsTitle);
        ((ViewHolderBanner) holder).mImage.setTransitionName(newsImageUrl);
        Glide.with(mActivity).load(newsImageUrl).transition(new DrawableTransitionOptions().crossFade()).into(((ViewHolderBanner) holder).mImage);
        ((ViewHolderBanner) holder).mImage.setTag(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.main_item_banner;
    }

    public class ViewHolderBanner extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public ImageView mImage;

        public ViewHolderBanner(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mImage=itemView.findViewById(R.id.news_image);
        }
    }
}
