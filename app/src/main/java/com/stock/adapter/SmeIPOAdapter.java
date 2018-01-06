package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.RowIpoListBinding;
import com.stock.fragments.SmeIPOFragment;
import com.stock.retrofit.model.IPODataEntityManager;
import com.stock.retrofit.model.SmeIPOData;
import com.stock.retrofit.model.SmeIPODataEntityManager;

import java.util.List;



public class SmeIPOAdapter extends RecyclerView.Adapter<SmeIPOAdapter.ViewHolder> {
    private Context context;
    private RowIpoListBinding mBinding;
    private SmeIPOClickListner smeIPOClickListner;
    private List<SmeIPOData> ipokArrayList, notifyOnArray;
    private SmeIPODataEntityManager ipoDataEntityManager;

    public SmeIPOAdapter(Context context, List<SmeIPOData> ipokArrayList, SmeIPOClickListner smeIPOClickListner) {
        this.context = context;
        this.smeIPOClickListner = smeIPOClickListner;
        this.ipokArrayList = ipokArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_ipo_list, parent, false);
        return new SmeIPOAdapter.ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ipoDataEntityManager = new SmeIPODataEntityManager();
        notifyOnArray =  ipoDataEntityManager.select().id().equalsTo(ipokArrayList.get(position).getId()).asList();
        if (notifyOnArray != null && notifyOnArray.size() > 0)
            holder. mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarm);

        holder.mBinding.rowIpoName.setText(ipokArrayList.get(position).getSmeIpoName());
        holder.mBinding.rowIpoPrice.setText(ipokArrayList.get(position).getPrice());
        holder.mBinding.rowIpoDate.setText(ipokArrayList.get(position).getIpoDate());

        holder.mBinding.rowIpoCard.setTag(position);
        holder.mBinding.rowIpoAlarm.setTag(position);
        holder.mBinding.rowIpoShare.setTag(position);
    }

    @Override
    public int getItemCount() {
        return ipokArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowIpoListBinding mBinding;

        public ViewHolder(RowIpoListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowIpoCard.setOnClickListener(this);
            this.mBinding.rowIpoAlarm.setOnClickListener(this);
            this.mBinding.rowIpoShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == mBinding.rowIpoShare) {
                if (smeIPOClickListner != null) {
                    int pos = (int) view.getTag();
                    smeIPOClickListner.onSmeIPOShare(pos);
                }
            } else if (view == mBinding.rowIpoAlarm) {
                if (smeIPOClickListner != null) {

                    int pos = (int) view.getTag();
                    if (ipokArrayList.get(pos).isFlag() == false) {
                        mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarm);
                        ipokArrayList.get(pos).setFlag(true);
                    }else{
                        mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarmoff);
                        ipokArrayList.get(pos).setFlag(false);
                    }
                    smeIPOClickListner.onSmeIPOAlarm(pos);
                }
            } else {
                if (smeIPOClickListner != null) {
                    int pos = (int) view.getTag();
                    smeIPOClickListner.onSmeIPOClick(pos);
                }
            }
        }
    }

    public interface SmeIPOClickListner {
        void onSmeIPOClick(int pos);
        void onSmeIPOShare(int pos);
        void onSmeIPOAlarm(int pos);
    }
}
