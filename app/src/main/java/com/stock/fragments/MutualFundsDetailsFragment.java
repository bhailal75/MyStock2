package com.stock.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.databinding.FragmentMutualfundsDetailsBinding;
import com.stock.databinding.FragmentNewsDetailsBinding;
import com.stock.interfaces.OnSetActinBarTitle;
import com.stock.retrofit.model.HomeData;
import com.stock.retrofit.model.MutualFundData;

import butterknife.ButterKnife;



public class MutualFundsDetailsFragment extends Fragment {
    private FragmentMutualfundsDetailsBinding mBinding;
    private MutualFundData mutualFundData;
    private HomeData homeData;
    private OnSetActinBarTitle onSetActinBarTitle;

    public MutualFundsDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public MutualFundsDetailsFragment(MutualFundData mutualFundData) {
        this.mutualFundData = mutualFundData;
    }

    @SuppressLint("ValidFragment")
    public MutualFundsDetailsFragment(HomeData homeData) {
        this.homeData = homeData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mutualfunds_details, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        onSetActinBarTitle = (OnSetActinBarTitle) getActivity();
        onSetActinBarTitle.onSetActionBarTitle("Mutual Fund");
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (homeData == null) {
            mBinding.frgMutualfundName.setText(mutualFundData.getFundName());
            mBinding.frgMutualfundPrice.setText(mutualFundData.getPrice());
            mBinding.frgMutualfundWeekHigh.setText(mutualFundData.getWeekHigh());
            mBinding.frgMutualfundWeekLow.setText(mutualFundData.getWeekLow());



            if (mutualFundData.getOneYear() != null)
                mBinding.frgMutualfundOneYear.setText(mutualFundData.getOneYear()+" %");
            else
                mBinding.frgMutualfundOneYear.setText("-");

            if (mutualFundData.getTwoYear() != null)
                mBinding.frgMutualfundTwoYear.setText(mutualFundData.getTwoYear()+" %");
            else
                mBinding.frgMutualfundTwoYear.setText("-");

            if (mutualFundData.getThreeYear() != null)
                mBinding.frgMutualfundThreeYear.setText(mutualFundData.getThreeYear()+" %");
            else
                mBinding.frgMutualfundThreeYear.setText("-");

            if (mutualFundData.getFiveYear() != null)
                mBinding.frgMutualfundFiveYear.setText(mutualFundData.getFiveYear()+" %");
            else
                mBinding.frgMutualfundFiveYear.setText("-");
        } else {
            mBinding.frgMutualfundName.setText(homeData.getFundName());
            mBinding.frgMutualfundPrice.setText(homeData.getPrice());
            mBinding.frgMutualfundWeekHigh.setText(homeData.getWeekHigh());
            mBinding.frgMutualfundWeekLow.setText(homeData.getWeekLow());


            if (homeData.getOneYear() != null)
                mBinding.frgMutualfundOneYear.setText(homeData.getOneYear()+" %");
            else
                mBinding.frgMutualfundOneYear.setText("-");

            if (homeData.getTwoYear() != null)
                mBinding.frgMutualfundTwoYear.setText(homeData.getTwoYear()+" %");
            else
                mBinding.frgMutualfundTwoYear.setText("-");

            if (homeData.getThreeYear() != null)
                mBinding.frgMutualfundThreeYear.setText(homeData.getThreeYear()+" %");
            else
                mBinding.frgMutualfundThreeYear.setText("-");

            if (homeData.getFiveYear() != null)
                mBinding.frgMutualfundFiveYear.setText(homeData.getFiveYear()+" %");
            else
                mBinding.frgMutualfundFiveYear.setText("-");
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
