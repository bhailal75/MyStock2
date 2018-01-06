package com.stock.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.AlarmReceiver;
import com.stock.MainActivity;
import com.stock.NotificationScheduler;
import com.stock.R;
import com.stock.StockApp;
import com.stock.adapter.IPOAdapter;
import com.stock.baseclass.BaseFragment;
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
import com.stock.utility.MyPref;
import com.stock.utility.Utility;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;



public class IPOFragment extends BaseFragment implements IPOAdapter.IPOClickListner, OnApiResponseListner, SwipeRefreshLayout.OnRefreshListener {
    private FragmentIpoBinding mBinding;
    private List<IPOData> ipokArrayList;
    private LinearLayoutManager linearLayoutManager;
    private IPOAdapter ipoAdapter;
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
            callIpo = ((StockApp) getActivity().getApplication()).getApiTask().getIpo(new APICallback(getActivity(),
                    APICall.IPO_CODE, this));
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
        ipoAdapter = new IPOAdapter(getActivity(), ipokArrayList, this);
        mBinding.frgIpoRv.setAdapter(ipoAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callIpo != null && !callIpo.isCanceled())
            callIpo.cancel();
    }

    @Override
    public void onIPOClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new IPODetailsFragment(ipokArrayList.get(pos)));
    }

    @Override
    public void onIPOShare(int pos) {
        String share_temp = " IPO " +
                "\nName: " + ipokArrayList.get(pos).getIpoName() +
                "\nPrice: " + ipokArrayList.get(pos).getPrice() +
                "\nRating: " + ipokArrayList.get(pos).getRating() +
                "\nSubscriptions: " + ipokArrayList.get(pos).getSubscription() +
                "\nLot Size: " + ipokArrayList.get(pos).getLotSize() +
                "\nIssue Size: " + ipokArrayList.get(pos).getIssueSize() +
                "\nFace Value: " + ipokArrayList.get(pos).getFaceValue() +
                "\nDate: " + ipokArrayList.get(pos).getIpoDate();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, share_temp + " share via " + "https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);


        ///

    }


    @Override
    public void onIPOAlarm(int pos) {
        IPODataEntityManager ipoDataEntityManager = new IPODataEntityManager();

        List<IPOData> ipoDatas = ipoDataEntityManager.select().id().equalsTo(ipokArrayList.get(pos).getId()).asList();
        Utility.log("ipo Data : " + ipoDatas);

        if (ipoDatas.size() == 0) {
            IPOData ipoData = new IPOData();
            ipoData.setId(ipokArrayList.get(pos).getId());
            ipoData.setIpoName(ipokArrayList.get(pos).getIpoName());
            ipoData.setPrice(ipokArrayList.get(pos).getPrice());
            ipoData.setRating(ipokArrayList.get(pos).getRating());
            ipoData.setSubscription(ipokArrayList.get(pos).getSubscription());
            ipoData.setLotSize(ipokArrayList.get(pos).getLotSize());
            ipoData.setIssueSize(ipokArrayList.get(pos).getIssueSize());
            ipoData.setFaceValue(ipokArrayList.get(pos).getFaceValue());
            ipoData.setIpoDate(ipokArrayList.get(pos).getIpoDate());
            ipoDataEntityManager.add(ipoData);
        } else {
            if (ipoDatas != null && ipoDatas.size() > 0)
                ipoDataEntityManager.delete(ipoDatas);
        }

    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frgIpoPv.setVisibility(View.GONE);
        mBinding.frgIpoRv.setVisibility(View.VISIBLE);
        mBinding.swipeContainer.setRefreshing(false);
        if (requestCode == APICall.IPO_CODE) {
            if (clsGson instanceof IPOResponse) {
                IPOResponse ipoResponse = (IPOResponse) clsGson;
                ipokArrayList.clear();
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                ipokArrayList.addAll(ipoResponse.getData());

                IPODataEntityManager ipoDataEntityManager = new IPODataEntityManager();
                for (int i = 0; i < ipokArrayList.size(); i++) {
                   List<IPOData> ipoDatas = ipoDataEntityManager.select().id().equalsTo(ipokArrayList.get(i).getId()).asList();
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
    public void onRefresh() {
        if (Utility.haveNetworkConnection(getActivity())) {
            callIpo = ((StockApp) getActivity().getApplication()).getApiTask().getIpo(new APICallback(getActivity(),
                    APICall.IPO_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}
