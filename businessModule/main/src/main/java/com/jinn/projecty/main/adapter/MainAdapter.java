package com.jinn.projecty.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.utils.LogUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jinnlee on 2021/2/23.
 */
public class MainAdapter extends ListAdapter<RecommandDataBean.IssueListBean.ItemListBean, MainAdapter.ViewHolderVideo> implements View.OnClickListener {
    private final String TAG ="RecommandAdapter";
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_BANNER =1;
    public static final int TYPE_OTHERS =2;
    private ArrayList<RecommandDataBean.IssueListBean.ItemListBean> mLists = new ArrayList<>();
    private Activity mActivity;

    public MainAdapter(Activity context){
        super(new MainAdapterDiffCallBack());
        mActivity = context;
    }

    public void initData(ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list) {
        mLists.clear();
        mLists.addAll(list);
        LogUtils.d(TAG,"initData:size"+mLists.size());
        submitList(list);
    }

    public void updateData(ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list){
        ArrayList<RecommandDataBean.IssueListBean.ItemListBean> newList = new ArrayList<>(mLists);
        newList.addAll(list);
        mLists = newList;
        submitList(newList);
    }

    public void removeData(int pos){
        LogUtils.d(TAG,"removeData:"+pos);
        ArrayList<RecommandDataBean.IssueListBean.ItemListBean> newList = new ArrayList<>(mLists);
        newList.remove(pos);
        mLists = newList;
        submitList(newList);
    }

    @NonNull
    @Override
    public ViewHolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG,"onCreateViewHolder:viewType:"+viewType);
        View viewVideo = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_video,parent,false);
        ViewHolderVideo viewHolderVideo;
        viewHolderVideo = new ViewHolderVideo(viewVideo);
        return viewHolderVideo;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderVideo holder, int position) {
        String newsTitle = mLists.get(position).getData().getTitle();
        String newsImageUrl = mLists.get(position).getData().getCover().getFeed();
        LogUtils.d(TAG,"onBindViewHolder:video image:"+newsImageUrl);
        holder.mTitle.setText(newsTitle);
        holder.mImage.setTransitionName(newsImageUrl);
        Glide.with(mActivity).load(newsImageUrl).transition(new DrawableTransitionOptions().crossFade()).into(((ViewHolderVideo) holder).mImage);
        holder.mImage.setTag(position);
        holder.mMoreImg.setTag(position);
        holder.mImage.setOnClickListener(this);
        holder.mMoreImg.setOnClickListener(this);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return R.layout.news_item_video;
    }

    @Override
    public int getItemCount() {
        LogUtils.d(TAG,"getItemCount:"+mLists.size());
        return mLists.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        if (v.getId() == R.id.more) {
            removeData(pos);
        } else if(v.getId() == R.id.news_image){
            Intent intent = new Intent();
            LogUtils.d(TAG, "onClick,name:" + v.getTransitionName());
            Pair pair = new Pair(v, "IMG_TRANSITION");
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, pair);
            intent.setClassName("com.jinn.projecty", "com.jinn.projecty.main.ui.VideoDetailActivity");
            intent.putExtra("url", v.getTransitionName());
            ActivityCompat.startActivity(mActivity, intent, activityOptions.toBundle());
        }
    }


    public static class ViewHolderVideo extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImage;
        public ImageView mMoreImg;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mImage=itemView.findViewById(R.id.news_image);
            mMoreImg = itemView.findViewById(R.id.more);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolderVideo holder) {
        LogUtils.d(TAG,"onViewRecycled,"+holder.mTitle);
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolderVideo holder) {
        LogUtils.d(TAG,"onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolderVideo holder) {
        LogUtils.d(TAG,"onViewDetachedFromWindow");
        super.onViewDetachedFromWindow(holder);
    }
}
