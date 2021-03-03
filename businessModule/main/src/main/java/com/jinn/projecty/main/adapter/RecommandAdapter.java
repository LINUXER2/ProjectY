package com.jinn.projecty.main.adapter;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jinnlee on 2021/2/23.
 */
public class RecommandAdapter extends RecyclerView.Adapter {
    private final String TAG ="RecommandAdapter";
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_BANNER =1;
    public static final int TYPE_OTHERS =2;
    private ArrayList<RecommandDataBean.IssueListBean.ItemListBean> mLists = new ArrayList<>();
    private Context mContext;
    public RecommandAdapter(Context context){
         mContext = context;
    }

    public void initData(RecommandDataBean data) {
        mLists.clear();
        ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list = data.getIssueList().get(0).getItemList();
        list.removeIf(obj-> !TextUtils.equals(obj.getType(),Constant.newsTypeVideo)&&!TextUtils.equals(obj.getType(),Constant.newsTypeBanner));
        mLists.addAll(list);
        LogUtils.d(TAG,"initData:size"+mLists.size());
        notifyDataSetChanged();
    }

    public void updateData(RecommandDataBean data){
        int pos = mLists.size();
        ArrayList<RecommandDataBean.IssueListBean.ItemListBean> list = data.getIssueList().get(0).getItemList();
        list.removeIf(obj-> !TextUtils.equals(obj.getType(),Constant.newsTypeVideo)&&!TextUtils.equals(obj.getType(),Constant.newsTypeBanner));
        mLists.addAll(list);
        notifyItemRangeChanged(pos,mLists.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(TAG,"onCreateViewHolder:viewType:"+viewType);
        switch (viewType){
            case TYPE_BANNER:
                ViewHolderBanner viewHolderBanner;
                View viewBanner = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_banner,parent,false);
                viewHolderBanner = new ViewHolderBanner(viewBanner);
                return viewHolderBanner;
            case TYPE_VIDEO:
                View viewVideo = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_video,parent,false);
                ViewHolderVideo viewHolderVideo;
                viewHolderVideo = new ViewHolderVideo(viewVideo);
                return viewHolderVideo;
            default:
                 View viewDefault = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_video,parent,false);
                 ViewHolderOther viewHolderDefault;
                 viewHolderDefault = new ViewHolderOther(viewDefault);
                 return viewHolderDefault;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           if(holder instanceof ViewHolderVideo){
               String newsTitle = mLists.get(position).getData().getTitle();
               String newsImageUrl = mLists.get(position).getData().getCover().getFeed();
               LogUtils.d(TAG,"onBindViewHolder:video image:"+newsImageUrl);
               ((ViewHolderVideo) holder).mTitle.setText(newsTitle);
               Glide.with(mContext).load(newsImageUrl).transition(new DrawableTransitionOptions().crossFade()).into(((ViewHolderVideo) holder).mImage);
           }else if(holder instanceof ViewHolderBanner){
               String newsTitle =mLists.get(position).getData().getTitle();
               String newsImageUrl = mLists.get(position).getData().getImage();
               LogUtils.d(TAG,"onBindViewHolder:banner image:"+newsImageUrl);
               ((ViewHolderBanner) holder).mTitle.setText(newsTitle);
               Glide.with(mContext).load(newsImageUrl).transition(new DrawableTransitionOptions().crossFade()).into(((ViewHolderBanner) holder).mImage);
            }else if(holder instanceof ViewHolderOther){

           }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        String type = mLists.get(position).getType();
        LogUtils.d(TAG,"getItemViewType:"+type);
        if (TextUtils.equals(type, Constant.newsTypeBanner)) {
            return TYPE_BANNER;
        } else if (TextUtils.equals(type, Constant.newsTypeVideo)) {
            return TYPE_VIDEO;
        } else {
            return TYPE_OTHERS;
        }
    }

    @Override
    public int getItemCount() {
        LogUtils.d(TAG,"getItemCount:"+mLists.size());
        return mLists.size();
    }


    public static class ViewHolderBanner extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImage;

        public ViewHolderBanner(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mImage=itemView.findViewById(R.id.news_image);
        }
    }


    public static class ViewHolderVideo extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImage;

        public ViewHolderVideo(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mImage=itemView.findViewById(R.id.news_image);
        }
    }

    public static class ViewHolderOther extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mImage;

        public ViewHolderOther(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mImage=itemView.findViewById(R.id.news_image);
        }
    }
}
