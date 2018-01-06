package com.stock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.stock.baseclass.BaseAppCompactActivity;
import com.stock.databinding.ActivityLoginBinding;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.LoginResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.MyPref;
import com.stock.utility.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class LoginActivity extends BaseAppCompactActivity implements OnApiResponseListner,View.OnTouchListener {

    ActivityLoginBinding mBinding;
    private Intent intent;
    private Call<?> callLogin;
    private String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ButterKnife.bind(this);

        if (getMyPref().getData(MyPref.Keys.ISLOGIN, false)) {
            Utility.navigationIntent(this, MainActivity.class);
            finish();
        }

        device_token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callLogin != null && !callLogin.isCanceled())
            callLogin.cancel();
    }


    @OnClick(R.id.act_login_forgot_password)
    public void forgetPassword() {
        intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.act_login_create_new)
    public void signUp() {
        intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.act_login_signin)
    public void signIn(){
//        if (isValid())
        if (isValid()) {
            if (Utility.haveNetworkConnection(this)) {
                showDialog();
                callLogin = ((StockApp) getApplication()).getApiTask().login(
                        mBinding.actLoginUsername.getText().toString().trim(),
                        mBinding.actLoginPassword.getText().toString().trim(),
                        device_token,
                        new APICallback(this, APICall.LOGIN_CODE, this));
            } else {
                Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            }
        }
    }

    private boolean isValid() {
        if (mBinding.actLoginUsername.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(),"Email is Required");

            return false;
        }
        if (!Utility.isValidEmaillId(mBinding.actLoginUsername.getText().toString().trim())) {
            Utility.showRedSnackBar(mBinding.getRoot(),"Valid Email is Required");
            return false;
        }
        if (mBinding.actLoginPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(),"Password is Required");
            return false;
        }
        return true;
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.LOGIN_CODE) {
            dismissDialog();
            if (clsGson instanceof LoginResponse) {
                LoginResponse loginResponse = (LoginResponse) clsGson;
                getMyPref().setData(MyPref.Keys.TOKEN, loginResponse.getToken());
                getMyPref().setData(MyPref.Keys.ISLOGIN, true);
                ((StockApp) getApplication()).refreshToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(LoginActivity.this);

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(LoginActivity.this);
        return false;
    }
}
