package com.stock.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.StockApp;
import com.stock.adapter.MutualFundAdapter;
import com.stock.container.BaseContainer;
import com.stock.databinding.FragmentMutualfundsBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.IPOResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.MutualFundData;
import com.stock.retrofit.model.MutualFundRespons;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by sparken09 on 24/10/17.
 */

public class MutualFundsFragment extends Fragment implements MutualFundAdapter.MutualFundsClickListner, OnApiResponseListner, SwipeRefreshLayout.OnRefreshListener {

    FragmentMutualfundsBinding mBinding;
    private List<MutualFundData> mutualfundArrayList;
    private LinearLayoutManager linearLayoutManager;
    private MutualFundAdapter mutualFundAdapter;
    private Call<?> callMutualFund;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mutualfunds, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frgMutualfundRv.setVisibility(View.GONE);
            mBinding.frgMutualfundPv.setVisibility(View.VISIBLE);
            callMutualFund = ((StockApp) getActivity().getApplication()).getApiTask().getMutualFund(new APICallback(getActivity(),
                    APICall.MUTUALFUND_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
        }
        mBinding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mBinding.swipeContainer.setOnRefreshListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mutualfundArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frgMutualfundRv.setLayoutManager(linearLayoutManager);
        mutualFundAdapter = new MutualFundAdapter(getActivity(), mutualfundArrayList, this);
        mBinding.frgMutualfundRv.setAdapter(mutualFundAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callMutualFund != null && !callMutualFund.isCanceled())
            callMutualFund.cancel();
    }

    @Override
    public void onMutualFundsClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new MutualFundsDetailsFragment(mutualfundArrayList.get(pos)));

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frgMutualfundPv.setVisibility(View.GONE);
        mBinding.frgMutualfundRv.setVisibility(View.VISIBLE);
        mBinding.swipeContainer.setRefreshing(false);
        if (requestCode == APICall.MUTUALFUND_CODE) {
            if (clsGson instanceof MutualFundRespons) {
                MutualFundRespons mutualFundRespons = (MutualFundRespons) clsGson;
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                mutualfundArrayList.clear();
                mutualfundArrayList.addAll(mutualFundRespons.getData());
                mutualFundAdapter.notifyDataSetChanged();
            }
            if (mutualfundArrayList.size() == 0)
                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frgMutualfundPv.setVisibility(View.GONE);
        mBinding.frgMutualfundRv.setVisibility(View.GONE);
        mBinding.swipeContainer.setRefreshing(false);
        mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callMutualFund = ((StockApp) getActivity().getApplication()).getApiTask().getMutualFund(new APICallback(getActivity(),
                    APICall.MUTUALFUND_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}
