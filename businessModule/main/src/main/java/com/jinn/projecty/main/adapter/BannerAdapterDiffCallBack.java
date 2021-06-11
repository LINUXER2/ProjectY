package com.jinn.projecty.main.adapter;

import com.jinn.projecty.main.bean.RecommandDataBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by jinnlee on 2021/6/9.
 */

class BannerAdapterDiffCallBack extends DiffUtil.ItemCallback<RecommandDataBean.IssueListBean.ItemListBean> {

    @Override
    public boolean areItemsTheSame(@NonNull RecommandDataBean.IssueListBean.ItemListBean oldItem, @NonNull RecommandDataBean.IssueListBean.ItemListBean newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull RecommandDataBean.IssueListBean.ItemListBean oldItem, @NonNull RecommandDataBean.IssueListBean.ItemListBean newItem) {
        return oldItem.getId()==newItem.getId();
    }
}
