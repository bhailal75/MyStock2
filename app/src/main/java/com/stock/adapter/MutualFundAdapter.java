package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.RowMutualfundListBinding;
import com.stock.retrofit.model.MutualFundData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparken09 on 25/10/17.
 */

public class MutualFundAdapter extends RecyclerView.Adapter<MutualFundAdapter.ViewHolder>{
    private Context context;
    private List<MutualFundData> mutualfundArrayList;
    private RowMutualfundListBinding mBinding;
    private MutualFundsClickListner mutualFundsClickListner;

    public MutualFundAdapter(Context context, List<MutualFundData> mutualfundArrayList ,MutualFundsClickListner mutualFundsClickListner) {
        this.context = context;
        this.mutualfundArrayList = mutualfundArrayList;
        this.mutualFundsClickListner = mutualFundsClickListner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_mutualfund_list,parent,false);
        return new ViewHolder(mBinding,parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.rowMutualfundCard.setTag(position);
        holder.mBinding.rowMutualfundName.setText(mutualfundArrayList.get(position).getFundName());
        holder.mBinding.rowMutualfundPrice.setText(mutualfundArrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mutualfundArrayList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowMutualfundListBinding mBinding;
        public ViewHolder(RowMutualfundListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowMutualfundCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mutualFundsClickListner != null){
                int pos = (int) view.getTag();
                mutualFundsClickListner.onMutualFundsClick(pos);

            }
        }
    }
    public interface MutualFundsClickListner{
        void onMutualFundsClick(int pos);
    }
}