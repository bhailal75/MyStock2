package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.RowNewsListBinding;
import com.stock.retrofit.model.NewsData;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparken09 on 25/10/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private List<NewsData> newskArrayList;
    private RowNewsListBinding mBinding;
    private NewsListClickListner newsListClickListner;

    public NewsAdapter(Context context, List<NewsData> newskArrayList ,NewsListClickListner newsListClickListner) {
        this.context = context;
        this.newskArrayList = newskArrayList;
        this.newsListClickListner = newsListClickListner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_news_list,parent,false);
        return new ViewHolder(mBinding,parent);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.rowNewsCard.setTag(position);
        holder.mBinding.rowNewsTitle.setText(newskArrayList.get(position).getTitle());
        Utility.loadImage(newskArrayList.get(position).getImage(),holder.mBinding.rowNewsImage,R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return newskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowNewsListBinding mBinding;
        public ViewHolder(RowNewsListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowNewsCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (newsListClickListner != null) {
                int pos = (int) view.getTag();
                newsListClickListner.onNewsClick(pos);
            }
        }
    }
    public interface NewsListClickListner{
        void onNewsClick(int pos);
    }
}
