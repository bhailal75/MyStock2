package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.container.BaseContainer;
import com.stock.databinding.RowHomeListBinding;
import com.stock.fragments.BuyStockDetailsFragment;
import com.stock.retrofit.model.HomeData;
import com.stock.retrofit.model.HomeDatum;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

/**
 * Created by sparken09 on 25/10/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private Context context;
    private List<HomeDatum> homeArrayList;
    private RowHomeListBinding mBinding;
    private HomeDetailListAdapter adapter;
    private HomeDetailListAdapter.HomeClickListner homeClickListner;

    public HomeListAdapter(Context context,List<HomeDatum> homeArrayList,HomeDetailListAdapter.HomeClickListner homeClickListner) {
        this.context = context;
        this.homeArrayList = homeArrayList;
        this.homeClickListner = homeClickListner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_list,parent,false);
        return new ViewHolder(mBinding,parent);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        homeDataList.addAll(homeArrayList.get(position).getData());
        mBinding.rowHomeSongTv.setText(homeArrayList.get(position).getTitle());
        holder.bindData(position);

    }

    @Override
    public int getItemCount() {
        return homeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private RowHomeListBinding mBinding;
        public ViewHolder(RowHomeListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bindData(int position) {
            mBinding.rowHomeSongRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapter=new HomeDetailListAdapter(context, homeArrayList.get(position).getData(),homeArrayList.get(position).getType(),homeClickListner,position);
            mBinding.rowHomeSongRv.setAdapter(adapter);
        }


    }


}
