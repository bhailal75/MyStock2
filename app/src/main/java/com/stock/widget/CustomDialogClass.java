package com.stock.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.stock.R;
import com.stock.interfaces.SelectPhoto;



@SuppressLint("ValidFragment")
public class CustomDialogClass extends DialogFragment implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public LinearLayout gallary, camera;
    private SelectPhoto selectPhoto;


    public CustomDialogClass() {
    }

    public CustomDialogClass(SelectPhoto selectPhoto) {
        this.selectPhoto = selectPhoto;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gallary = (LinearLayout) view.findViewById(R.id.gallary);
        camera = (LinearLayout) view.findViewById(R.id.camera);
        gallary.setOnClickListener(this);
        camera.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallary:
                selectPhoto.getGallary();
                dismiss();
                break;
            case R.id.camera:
                selectPhoto.getCamera();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}





