package com.stock.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.stock.MainActivity;
import com.stock.R;
import com.stock.StockApp;
import com.stock.adapter.HomeDetailListAdapter;
import com.stock.adapter.HomeListAdapter;
import com.stock.baseclass.BaseFragment;
import com.stock.container.BaseContainer;
import com.stock.container.BuyStockContainer;
import com.stock.container.HomeContainer;
import com.stock.databinding.FragmentBuyStokBinding;
import com.stock.databinding.FragmentHomeBinding;
import com.stock.interfaces.OnSetActinBarTitle;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.HomeDatum;
import com.stock.retrofit.model.HomeResponse;
import com.stock.retrofit.model.IPOResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by sparken09 on 24/10/17.
 */

public class HomeFragment extends BaseFragment implements OnApiResponseListner, HomeDetailListAdapter.HomeClickListner, SwipeRefreshLayout.OnRefreshListener {

    FragmentHomeBinding mBinding;
    private List<HomeDatum> homeArrayList;
    private LinearLayoutManager linearLayoutManager;
    private HomeListAdapter homeAdapter;
    private Call<?> callLogout;
    private OnSetActinBarTitle onSetActinBarTitle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        onSetActinBarTitle = (OnSetActinBarTitle) getActivity();
        onSetActinBarTitle.onSetActionBarTitle("My Money Place");




        if (Utility.haveNetworkConnection(getActivity())) {
            showDialog();
            callLogout = ((StockApp) getActivity().getApplication()).getApiTask().home(new APICallback(getActivity(), APICall.HOME, this));
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
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Money Place");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callLogout != null && !callLogout.isCanceled())
            callLogout.cancel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frgHomeRv.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeListAdapter(getActivity(), homeArrayList, this);
        mBinding.frgHomeRv.setAdapter(homeAdapter);
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        dismissDialog();
        if (requestCode == APICall.HOME) {
            if (clsGson instanceof HomeResponse) {
                HomeResponse homeResponse = (HomeResponse) clsGson;
                mBinding.swipeContainer.setRefreshing(false);
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                homeArrayList.clear();
                homeArrayList.addAll(homeResponse.getData());
                homeAdapter.notifyDataSetChanged();
            }
            if (homeArrayList.size() == 0)
                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        mBinding.frgHomeRv.setVisibility(View.GONE);
        mBinding.swipeContainer.setRefreshing(false);
        mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }

    }

    @Override
    public void onStockClick(int posType, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new BuyStockDetailsFragment(homeArrayList.get(posType).getData().get(pos)));
    }

    @Override
    public void onIPOClick(int posType, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new IPODetailsFragment(homeArrayList.get(posType).getData().get(pos)));
    }

    @Override
    public void onMutualfundClick(int posType, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new MutualFundsDetailsFragment(homeArrayList.get(posType).getData().get(pos)));
    }

    @Override
    public void onNewsClick(int posType, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new NewsDetailsFragment(homeArrayList.get(posType).getData().get(pos)));
    }

    @Override
    public void onSMEIPOClick(int posType, int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new SmeIPODetailsFragment(homeArrayList.get(posType).getData().get(pos)));
    }

    @Override
    public void onRefresh() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callLogout = ((StockApp) getActivity().getApplication()).getApiTask().home(new APICallback(getActivity(), APICall.HOME, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}
