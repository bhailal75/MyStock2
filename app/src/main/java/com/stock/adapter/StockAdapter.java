package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.RowStockListBinding;
import com.stock.retrofit.model.BuyStockData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparken09 on 25/10/17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private Context context;
    private List<BuyStockData> stockArrayList;
    private RowStockListBinding mBinding;
    private StockClickListner stockClickListner;

    public StockAdapter(Context context, List<BuyStockData> stockArrayList, StockClickListner stockClickListner) {
        this.context = context;
        this.stockArrayList = stockArrayList;
        this.stockClickListner = stockClickListner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_stock_list,parent,false);
        return new ViewHolder(mBinding,parent);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.rowStockCard.setTag(position);
        holder.mBinding.rowStockName.setText(stockArrayList.get(position).getScriptName());
        holder.mBinding.rowStockTarget.setText(stockArrayList.get(position).getTargets());
        holder.mBinding.rowStockDate.setText(stockArrayList.get(position).getTargetDate());
    }

    @Override
    public int getItemCount() {
        return stockArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowStockListBinding mBinding;
        public ViewHolder(RowStockListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowStockCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (stockClickListner != null){
                int pos = (int) view.getTag();
                stockClickListner.onStockClick(pos);
            }
        }
    }
    public interface StockClickListner{
        void onStockClick(int pos);
    }
}
