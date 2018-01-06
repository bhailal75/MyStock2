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
import com.stock.databinding.RowStockListBinding;
import com.stock.fragments.SmeIPOFragment;
import com.stock.retrofit.model.IPOData;
import com.stock.retrofit.model.IPODataEntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparken09 on 25/10/17.
 */

public class IPOAdapter extends RecyclerView.Adapter<IPOAdapter.ViewHolder> {

    private Context context;
    private List<IPOData> ipokArrayList, notifyOnArray;
    private RowIpoListBinding mBinding;
    private IPOClickListner ipoClickListner;
    private IPODataEntityManager ipoDataEntityManager;


    public IPOAdapter(Context context, List<IPOData> ipokArrayList, IPOClickListner ipoClickListner) {
        this.context = context;
        this.ipokArrayList = ipokArrayList;
        this.ipoClickListner = ipoClickListner;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_ipo_list, parent, false);
        return new ViewHolder(mBinding, parent);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ipoDataEntityManager = new IPODataEntityManager();
        notifyOnArray = ipoDataEntityManager.select().id().equalsTo(ipokArrayList.get(position).getId()).asList();
        if (notifyOnArray != null && notifyOnArray.size() > 0) {
            holder.mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarm);

        }
        holder.mBinding.rowIpoName.setText(ipokArrayList.get(position).getIpoName());
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
                if (ipoClickListner != null) {
                    int pos = (int) view.getTag();
                    ipoClickListner.onIPOShare(pos);

                }
            } else if (view == mBinding.rowIpoAlarm) {
                if (ipoClickListner != null) {

                    int pos = (int) view.getTag();

                    if (ipokArrayList.get(pos).isFlag() == false) {
                        mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarm);
                        ipokArrayList.get(pos).setFlag(true);
                    } else {
                        mBinding.rowIpoAlarm.setImageResource(R.drawable.ic_alarmoff);
                        ipokArrayList.get(pos).setFlag(false);
                    }

                    ipoClickListner.onIPOAlarm(pos);
                }
            } else {
                if (ipoClickListner != null) {
                    int pos = (int) view.getTag();
                    ipoClickListner.onIPOClick(pos);

                }
            }
        }
    }

    public interface IPOClickListner {
        void onIPOClick(int pos);

        void onIPOShare(int pos);

        void onIPOAlarm(int pos);
    }
}
