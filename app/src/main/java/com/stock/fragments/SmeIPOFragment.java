package com.stock.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.StockApp;
import com.stock.adapter.IPOAdapter;
import com.stock.adapter.SmeIPOAdapter;
import com.stock.container.BaseContainer;
import com.stock.databinding.FragmentIpoBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.BuyStockResponse;
import com.stock.retrofit.model.IPOData;
import com.stock.retrofit.model.IPODataEntityManager;
import com.stock.retrofit.model.IPOResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.SmeIPOData;
import com.stock.retrofit.model.SmeIPODataEntityManager;
import com.stock.retrofit.model.SmeIPOResponse;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by sparken09 on 24/10/17.
 */


public class SmeIPOFragment extends Fragment implements SmeIPOAdapter.SmeIPOClickListner, OnApiResponseListner, SwipeRefreshLayout.OnRefreshListener {
    private FragmentIpoBinding mBinding;
    private List<SmeIPOData> ipokArrayList;
    private LinearLayoutManager linearLayoutManager;
    private SmeIPOAdapter ipoAdapter;
    private Call<?> callIpo;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ipo, container, false);
        ButterKnife.bind(this, mBinding.getRoot());

        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frgIpoRv.setVisibility(View.GONE);
            mBinding.frgIpoPv.setVisibility(View.VISIBLE);
            callIpo = ((StockApp) getActivity().getApplication()).getApiTask().getSmeIpo(new APICallback(getActivity(),
                    APICall.SME_IPO_CODE, this));
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

        ipokArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frgIpoRv.setLayoutManager(linearLayoutManager);
        ipoAdapter = new SmeIPOAdapter(getActivity(),ipokArrayList, this);
        mBinding.frgIpoRv.setAdapter(ipoAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callIpo != null && !callIpo.isCanceled())
            callIpo.cancel();
    }


    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frgIpoPv.setVisibility(View.GONE);
        mBinding.frgIpoRv.setVisibility(View.VISIBLE);
        mBinding.swipeContainer.setRefreshing(false);
        if (requestCode == APICall.SME_IPO_CODE) {
            if (clsGson instanceof SmeIPOResponse) {
                SmeIPOResponse smeIPOResponse = (SmeIPOResponse) clsGson;
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                ipokArrayList.clear();
                ipokArrayList.addAll(smeIPOResponse.getData());

                SmeIPODataEntityManager smeIPODataEntityManager = new SmeIPODataEntityManager();
                for (int i = 0; i < ipokArrayList.size(); i++) {
                    List<SmeIPOData> ipoDatas = smeIPODataEntityManager.select().id().equalsTo(ipokArrayList.get(i).getId()).asList();
                    if (ipoDatas != null && ipoDatas.size() > 0) {
                        ipokArrayList.get(i).setFlag(true);
                    }
                }
                ipoAdapter.notifyDataSetChanged();
            }
            if (ipokArrayList.size() == 0)
                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frgIpoPv.setVisibility(View.GONE);
        mBinding.frgIpoRv.setVisibility(View.GONE);
        mBinding.swipeContainer.setRefreshing(false);
        mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    @Override
    public void onSmeIPOClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new SmeIPODetailsFragment(ipokArrayList.get(pos)));
    }

    @Override
    public void onSmeIPOShare(int pos) {
        String share_temp = " IPO " +
                "\nName: " + ipokArrayList.get(pos).getSmeIpoName() +
                "\nPrice: " + ipokArrayList.get(pos).getPrice() +
                "\nRating: " + ipokArrayList.get(pos).getRating() +
                "\nSubscriptions: " + ipokArrayList.get(pos).getSubscription() +
                "\nLot Size: " + ipokArrayList.get(pos).getLotSize() +
                "\nIssue Size: " + ipokArrayList.get(pos).getIssueSize() +
                "\nFace Value: " + ipokArrayList.get(pos).getFaceValue() +
                "\nDate: " + ipokArrayList.get(pos).getIpoDate();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, share_temp);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onSmeIPOAlarm(int pos) {
        SmeIPODataEntityManager smeIPODataEntityManager = new SmeIPODataEntityManager();
        List<SmeIPOData> ipoDatas = smeIPODataEntityManager.select().id().equalsTo(ipokArrayList.get(pos).getId()).asList();
        Utility.log("ipo Data : "+ipoDatas);

        if (ipoDatas.size() == 0) {
            SmeIPOData ipoData = new SmeIPOData();
            ipoData.setId(ipokArrayList.get(pos).getId());
            ipoData.setSmeIpoName(ipokArrayList.get(pos).getSmeIpoName());
            ipoData.setPrice(ipokArrayList.get(pos).getPrice());
            ipoData.setRating(ipokArrayList.get(pos).getRating());
            ipoData.setSubscription(ipokArrayList.get(pos).getSubscription());
            ipoData.setLotSize(ipokArrayList.get(pos).getLotSize());
            ipoData.setIssueSize(ipokArrayList.get(pos).getIssueSize());
            ipoData.setFaceValue(ipokArrayList.get(pos).getFaceValue());
            ipoData.setIpoDate(ipokArrayList.get(pos).getIpoDate());
            smeIPODataEntityManager.add(ipoData);
        }else{
            if (ipoDatas != null && ipoDatas.size() > 0)
                smeIPODataEntityManager.delete(ipoDatas);
        }
    }

    @Override
    public void onRefresh() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callIpo = ((StockApp) getActivity().getApplication()).getApiTask().getSmeIpo(new APICallback(getActivity(),
                    APICall.SME_IPO_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}




