package com.stock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.stock.baseclass.BaseAppCompactActivity;
import com.stock.databinding.ActivityForgotPasswordBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.Utility;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ForgotPasswordActivity extends BaseAppCompactActivity implements OnApiResponseListner,View.OnTouchListener {

    ActivityForgotPasswordBinding mBinding;
    private Call<?> callForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.act_forgotpassword_back)
    public void backPress() {
        onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callForget != null && !callForget.isCanceled())
            callForget.cancel();
    }


    @OnClick(R.id.act_forgot_recover)
    public void recoverPassword() {
        Utility.hideKeyboared(this);
        if (isValid())
            if (Utility.haveNetworkConnection(this)) {
                showDialog();
                callForget = ((StockApp) getApplication()).getApiTask().forgetPassword(
                        mBinding.actForgotEmail.getText().toString().trim(),
                        new APICallback(this, APICall.FORGET_PASSWORD, this));
            } else {
                Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            }
    }

    @OnClick(R.id.act_forgot_create_password)
    public void createPasswordClick(){
        Utility.hideKeyboared(this);
        if (isMoreValid()) {
            if (Utility.haveNetworkConnection(this)) {
                showDialog();
                callForget = ((StockApp) getApplication()).getApiTask().createPassword(mBinding.actForgotRecoverCode.getText().toString(),
                        mBinding.actForgotEnterNewpassword.getText().toString().trim(),
                        new APICallback(this, APICall.VERIFY_CODE, this));
            } else {
                Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            }
        }
    }

    private boolean isValid() {
        if (mBinding.actForgotEmail.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.email_required));
            return false;
        }

        if (!isValidEmaillId(mBinding.actForgotEmail.getText().toString().trim())) {
            Utility.showRedSnackBar(mBinding.getRoot(),"Valid Email is Required");
            return false;
        }
        return true;
    }
    private boolean isMoreValid() {
        if (mBinding.actForgotRecoverCode.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Code is Required");
            return false;
        }

        if (mBinding.actForgotEnterNewpassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Password is Required");
            return false;
        }

        return true;
    }

    public static boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        dismissDialog();
        if (requestCode == APICall.FORGET_PASSWORD) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = new MessageResponse();
                Utility.showRedSnackBar(mBinding.getRoot(),messageResponse.getMessage());
                mBinding.actCreateNewLl.setVisibility(View.VISIBLE);
                mBinding.actRecoverLl.setVisibility(View.GONE);
            }
        }
        if (requestCode == APICall.VERIFY_CODE) {
            dismissDialog();
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = new MessageResponse();
                Utility.showRedSnackBar(mBinding.getRoot(),messageResponse.getMessage());
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            backPress();
        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(ForgotPasswordActivity.this);
        return false;
    }
}
