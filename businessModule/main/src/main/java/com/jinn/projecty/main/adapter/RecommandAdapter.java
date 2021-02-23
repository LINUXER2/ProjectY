package com.jinn.projecty.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinn.projecty.main.R;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.main.constant.Constant;
import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jinnlee on 2021/2/23.
 */
public class RecommandAdapter extends RecyclerView.Adapter {
    private final String TAG ="RecommandAdapter";
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_BANNER =1;
    private RecommandDataBean mData;
    private Context mContext;
    public RecommandAdapter(Context context){
         mContext = context;
    }

    public void initData(RecommandDataBean data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void updateData(RecommandDataBean data){

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           if(holder instanceof ViewHolderVideo){
               String newsTitle = mData.getIssueList().get(0).getItemList().get(position).getData().getTitle();
               String newsImageUrl = mData.getIssueList().get(0).getItemList().get(position).getData().getCover().getFeed();
               LogUtils.d(TAG,"onBindViewHolder:video image:"+newsImageUrl);
               ((ViewHolderVideo) holder).mTitle.setText(newsTitle);
               Glide.with(mContext).load(newsImageUrl).into(((ViewHolderVideo) holder).mImage);
           }else if(holder instanceof ViewHolderBanner){
               String newsTitle = mData.getIssueList().get(0).getItemList().get(position).getData().getTitle();
               String newsImageUrl = mData.getIssueList().get(0).getItemList().get(position).getData().getImage();
               LogUtils.d(TAG,"onBindViewHolder:banner image:"+newsImageUrl);
               ((ViewHolderBanner) holder).mTitle.setText(newsTitle);
               Glide.with(mContext).load(newsImageUrl).into(((ViewHolderBanner) holder).mImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        String type = mData.getIssueList().get(0).getItemList().get(position).getType();
        if (TextUtils.equals(type, Constant.newsTypeBanner)) {
            return TYPE_BANNER;
        } else if (TextUtils.equals(type, Constant.newsTypeVideo)) {
            return TYPE_VIDEO;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (mData==null|| !mData.isDataValid()) {
            return 0;
        }
        LogUtils.d(TAG,"getItemCount:"+mData.getIssueList().get(0).getItemList().size());
        return mData.getIssueList().get(0).getItemList().size();
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
}
