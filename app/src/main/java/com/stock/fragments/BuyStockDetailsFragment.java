package com.stock.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.MainActivity;
import com.stock.R;
import com.stock.databinding.FragmentBuyStockDetailsBinding;
import com.stock.databinding.FragmentBuyStokBinding;
import com.stock.databinding.FragmentNewsDetailsBinding;
import com.stock.interfaces.OnSetActinBarTitle;
import com.stock.retrofit.model.BuyStockData;
import com.stock.retrofit.model.HomeData;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by sparken09 on 26/10/17.
 */

public class BuyStockDetailsFragment extends Fragment {
    private FragmentBuyStockDetailsBinding mBinding;
    private BuyStockData buyStockData;
    private HomeData homeData;
    private OnSetActinBarTitle onSetActinBarTitle;


    public BuyStockDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public BuyStockDetailsFragment(BuyStockData buyStockData) {
        this.buyStockData = buyStockData;
    }

    @SuppressLint("ValidFragment")
    public BuyStockDetailsFragment(HomeData homeData) {
        this.homeData = homeData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_stock_details, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        onSetActinBarTitle = (OnSetActinBarTitle) getActivity();
        onSetActinBarTitle.onSetActionBarTitle("Stock");
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (homeData == null) {
            mBinding.frgStockName.setText(buyStockData.getScriptName());
            mBinding.frgStockCode.setText(buyStockData.getScriptCode());
            mBinding.frgStockEntry.setText(buyStockData.getEntry());
            mBinding.frgStockLoss.setText(buyStockData.getStopLoss());
            mBinding.frgStockTarget.setText(buyStockData.getTargets());
            mBinding.frgStockDate.setText(buyStockData.getTargetDate());
        } else {
            mBinding.frgStockName.setText(homeData.getScriptName());
            mBinding.frgStockCode.setText(homeData.getScriptCode());
            mBinding.frgStockEntry.setText(homeData.getEntry());
            mBinding.frgStockLoss.setText(homeData.getStopLoss());
            mBinding.frgStockTarget.setText(homeData.getTargets());
            mBinding.frgStockDate.setText(homeData.getTargetDate());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeData != null){
            onSetActinBarTitle.onSetActionBarTitle("My Money Place");
        }
    }
}
