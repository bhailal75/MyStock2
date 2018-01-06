package com.stock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.RowHomeDetailsListBinding;
import com.stock.retrofit.model.HomeData;
import com.stock.retrofit.model.HomeDatum;
import com.stock.utility.Utility;

import java.util.List;



public class HomeDetailListAdapter extends RecyclerView.Adapter<HomeDetailListAdapter.ViewHolder> {

    private Context context;
    private List<HomeData> homedataList;
    private RowHomeDetailsListBinding mBinding;
    private HomeDetailListAdapter adapter;
    private String type;
    private HomeClickListner homeClickListner;
    private int posType;

    public HomeDetailListAdapter(Context context, List<HomeData> homeArrayList, String type,HomeClickListner homeClickListner,int position) {
        this.type = type;
        this.homedataList = homeArrayList;
        this.context = context;
        this.homeClickListner = homeClickListner;
        this.posType = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_home_details_list, parent, false);
        return new ViewHolder(mBinding, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            if (type.equals("Stock")) {
                Utility.loadImage(homedataList.get(position).getImage(),mBinding.rowHomeDetailImage,R.drawable.ic_placeholderstock);
                mBinding.rowHomeDetailTitle.setText(homedataList.get(position).getScriptName());
                mBinding.rowHomeDetailDate.setText(homedataList.get(position).getTargetDate());

            }

            if (type.equals("IPO")){
                Utility.loadImage(homedataList.get(position).getImage(),mBinding.rowHomeDetailImage,R.drawable.ic_placeholderipo);
                mBinding.rowHomeDetailTitle.setText(homedataList.get(position).getIpoName());
                mBinding.rowHomeDetailDate.setText(homedataList.get(position).getIpoDate());

            }
            if (type.equals("MutualFund")){
                Utility.loadImage(homedataList.get(position).getImage(),mBinding.rowHomeDetailImage,R.drawable.ic_placeholdermutualfund);
                mBinding.rowHomeDetailTitle.setText(homedataList.get(position).getFundName());
                mBinding.rowHomeDetailDate.setText(homedataList.get(position).getPrice());

            }
            if (type.equals("News")){
                Utility.loadImage(homedataList.get(position).getImage(),mBinding.rowHomeDetailImage,R.drawable.placeholder);
                mBinding.rowHomeDetailTitle.setText(homedataList.get(position).getTitle());
                mBinding.rowHomeDetailDate.setText("");

            }
            if (type.equals("SMEIPO")){
                Utility.loadImage(homedataList.get(position).getImage(),mBinding.rowHomeDetailImage,R.drawable.placeholdersmeipo);
                mBinding.rowHomeDetailTitle.setText(homedataList.get(position).getSmeIpoName());
                mBinding.rowHomeDetailDate.setText(homedataList.get(position).getIpoDate());
            }
        mBinding.rowIpoCard.setTag(position);

    }

    @Override
    public int getItemCount() {
        return homedataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RowHomeDetailsListBinding mBinding;

        public ViewHolder(RowHomeDetailsListBinding mBinding, View itemView) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            this.mBinding.rowIpoCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (homeClickListner != null) {
                if (type.equals("Stock")) {
                    int pos = (int) view.getTag();
                    homeClickListner.onStockClick(posType,pos);
                }
                if (type.equals("IPO")) {
                    int pos = (int) view.getTag();
                    homeClickListner.onIPOClick(posType,pos);
                }
                if (type.equals("MutualFund")) {
                    int pos = (int) view.getTag();
                    homeClickListner.onMutualfundClick(posType,pos);
                }
                if (type.equals("News")) {
                    int pos = (int) view.getTag();
                    homeClickListner.onNewsClick(posType,pos);
                }
                if (type.equals("SMEIPO")) {
                    int pos = (int) view.getTag();
                    homeClickListner.onSMEIPOClick(posType,pos);
                }
            }
        }
    }
    public interface HomeClickListner{
        void onStockClick(int posType,int pos);
        void onIPOClick(int posType,int pos);
        void onMutualfundClick(int posType,int pos);
        void onNewsClick(int posType,int pos);
        void onSMEIPOClick(int posType,int pos);

    }
}
