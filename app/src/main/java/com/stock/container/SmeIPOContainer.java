package com.stock.container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stock.R;
import com.stock.fragments.IPOFragment;
import com.stock.fragments.SmeIPOFragment;


public class SmeIPOContainer extends BaseContainer {
    private SmeIPOFragment ipo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ipo == null)
            ipo = new SmeIPOFragment();
        replaceFragment(ipo);
    }
}














