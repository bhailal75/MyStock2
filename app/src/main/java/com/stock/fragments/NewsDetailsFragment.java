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
import com.stock.databinding.FragmentNewsDetailsBinding;
import com.stock.interfaces.OnSetActinBarTitle;
import com.stock.retrofit.model.HomeData;
import com.stock.retrofit.model.NewsData;
import com.stock.utility.Utility;

import butterknife.ButterKnife;



public class NewsDetailsFragment extends Fragment {
    private FragmentNewsDetailsBinding mBinding;
    private NewsData newsData;
    private HomeData homeData;
    private OnSetActinBarTitle onSetActinBarTitle;

    public NewsDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public NewsDetailsFragment(NewsData newsData) {
        this.newsData = newsData;
    }

    @SuppressLint("ValidFragment")
    public NewsDetailsFragment(HomeData homeData) {
        this.homeData = homeData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_details, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        onSetActinBarTitle = (OnSetActinBarTitle) getActivity();
        onSetActinBarTitle.onSetActionBarTitle("News");
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (homeData == null) {
            mBinding.frgNewsTitle.setText(newsData.getTitle());
            mBinding.frgNewsContent.setText(newsData.getDescription());
            Utility.loadImage(newsData.getImage(), mBinding.frgNewsImage, R.drawable.placeholder);
        } else {
            mBinding.frgNewsTitle.setText(homeData.getTitle());
            mBinding.frgNewsContent.setText(homeData.getDescription());
            Utility.loadImage(homeData.getImage(), mBinding.frgNewsImage, R.drawable.placeholder);
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
