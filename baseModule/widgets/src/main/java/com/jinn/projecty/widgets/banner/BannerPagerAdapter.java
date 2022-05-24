package com.jinn.projecty.widgets.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.jinn.projecty.widgets.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BannerPagerAdapter extends RecyclerView.Adapter {
    private IBannerClickListener bannerClickListener;
    private List<String>mLists = new ArrayList<>();
    private Context mContext;

    public BannerPagerAdapter(Context context){
        mContext = context;
    }

    public void initData(List<String>list){
        mLists.clear();
        mLists.addAll(list);
    }

    public void setBannerClickListener(IBannerClickListener bannerClickListener) {
        this.bannerClickListener = bannerClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_pager_item,null,false);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        String url = mLists.get(position);
        if(holder instanceof CommonViewHolder){
            Glide.with(mContext).load(url).into(((CommonViewHolder) holder).imgView);
            if (bannerClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerClickListener.OnBannerClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
          return mLists.size();
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;
        public CommonViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.banner_image_view_item);
        }
    }


}
