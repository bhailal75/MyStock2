package com.stock.baseclass;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.stock.R;
import com.stock.utility.MyPref;



abstract public class BaseFragment extends Fragment {
    BaseAppCompactActivity activity;
    private Dialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseAppCompactActivity) getActivity();
    }
    protected void log(String msg) {
        activity.log(msg);
    }

    protected void toast(String msg) {
        activity.toast(msg);
    }



    protected MyPref getMypref() {
        return activity.getMyPref();
    }

    protected void showDialog() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.setContentView(R.layout.dialog_loader);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

}
