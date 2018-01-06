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

import com.stock.StockApp;
import com.stock.R;
import com.stock.adapter.StockAdapter;
import com.stock.container.BaseContainer;
import com.stock.databinding.FragmentBuyStokBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.BuyStockData;
import com.stock.retrofit.model.BuyStockResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;



public class BuyStockFragment extends Fragment implements StockAdapter.StockClickListner, OnApiResponseListner, SwipeRefreshLayout.OnRefreshListener {

    FragmentBuyStokBinding mBinding;
    private List<BuyStockData> stockArrayList;
    private LinearLayoutManager linearLayoutManager;
    private StockAdapter stockAdapter;
    private Call<?> callBuyStock;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_stok, container, false);
        ButterKnife.bind(this, mBinding.getRoot());

        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frgBuyStockRv.setVisibility(View.GONE);
            mBinding.frgStockPv.setVisibility(View.VISIBLE);
            callBuyStock = ((StockApp) getActivity().getApplication()).getApiTask().getBuyStock(new APICallback(getActivity(),
                    APICall.BUY_STOCK_CODE, this));
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
    public void onPause() {
        super.onPause();
        if (callBuyStock != null && !callBuyStock.isCanceled())
            callBuyStock.cancel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frgBuyStockRv.setLayoutManager(linearLayoutManager);
        stockAdapter = new StockAdapter(getActivity(), stockArrayList, this);
        mBinding.frgBuyStockRv.setAdapter(stockAdapter);
    }

    @Override
    public void onStockClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new BuyStockDetailsFragment(stockArrayList.get(pos)));
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frgStockPv.setVisibility(View.GONE);
        mBinding.frgBuyStockRv.setVisibility(View.VISIBLE);
        mBinding.swipeContainer.setRefreshing(false);
        if (requestCode == APICall.BUY_STOCK_CODE) {
            if (clsGson instanceof BuyStockResponse) {
                BuyStockResponse buyStockResponse = (BuyStockResponse) clsGson;
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                stockArrayList.clear();
                stockArrayList.addAll(buyStockResponse.getData());
                stockAdapter.notifyDataSetChanged();
            }
            if (stockArrayList.size() == 0)
                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frgStockPv.setVisibility(View.GONE);
        mBinding.frgBuyStockRv.setVisibility(View.GONE);
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
            callBuyStock = ((StockApp) getActivity().getApplication()).getApiTask().getBuyStock(new APICallback(getActivity(),
                    APICall.BUY_STOCK_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}
