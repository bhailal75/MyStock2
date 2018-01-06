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
import com.stock.adapter.NewsAdapter;
import com.stock.baseclass.BaseFragment;
import com.stock.container.BaseContainer;
import com.stock.databinding.FragmentNewsBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.MutualFundRespons;
import com.stock.retrofit.model.NewsData;
import com.stock.retrofit.model.NewsResponse;
import com.stock.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by sparken09 on 24/10/17.
 */

public class NewsFragment extends BaseFragment implements NewsAdapter.NewsListClickListner, OnApiResponseListner, SwipeRefreshLayout.OnRefreshListener {

    private FragmentNewsBinding mBinding;
    private List<NewsData> newsArrayList;
    private LinearLayoutManager linearLayoutManager;
    private NewsAdapter newsAdapter;
    private Call<?> callNews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        if (Utility.haveNetworkConnection(getActivity())) {
            mBinding.frgNewsRv.setVisibility(View.GONE);
            mBinding.frgNewsPv.setVisibility(View.VISIBLE);
            callNews = ((StockApp) getActivity().getApplication()).getApiTask().getNews(new APICallback(getActivity(),
                    APICall.NEWS_CODE, this));
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
        if (callNews != null && !callNews.isCanceled())
            callNews.cancel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.frgNewsRv.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity(), newsArrayList, this);
        mBinding.frgNewsRv.setAdapter(newsAdapter);
    }

    @Override
    public void onNewsClick(int pos) {
        ((BaseContainer) getParentFragment()).addFragment(new NewsDetailsFragment(newsArrayList.get(pos)));
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        mBinding.frgNewsPv.setVisibility(View.GONE);
        mBinding.frgNewsRv.setVisibility(View.VISIBLE);
        mBinding.swipeContainer.setRefreshing(false);
        if (requestCode == APICall.NEWS_CODE) {
            if (clsGson instanceof NewsResponse) {
                NewsResponse newsResponse = (NewsResponse) clsGson;
                mBinding.tvNoDataFound.setVisibility(View.GONE);
                newsArrayList.clear();
                newsArrayList.addAll(newsResponse.getData());
                newsAdapter.notifyDataSetChanged();
            }
            if (newsArrayList.size() == 0)
                mBinding.tvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        mBinding.frgNewsPv.setVisibility(View.GONE);
        mBinding.frgNewsRv.setVisibility(View.GONE);
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
            callNews = ((StockApp) getActivity().getApplication()).getApiTask().getNews(new APICallback(getActivity(),
                    APICall.NEWS_CODE, this));
        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            mBinding.swipeContainer.setRefreshing(false);
        }
    }
}
