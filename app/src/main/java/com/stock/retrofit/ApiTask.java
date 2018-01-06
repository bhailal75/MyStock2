package com.stock.retrofit;

import com.stock.StockApp;
import com.stock.retrofit.model.BuyStockResponse;
import com.stock.retrofit.model.HomeResponse;
import com.stock.retrofit.model.IPOResponse;
import com.stock.retrofit.model.LoginResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.MutualFundRespons;
import com.stock.retrofit.model.NewsResponse;
import com.stock.retrofit.model.ProfileResponse;
import com.stock.retrofit.model.SignUpResponse;
import com.stock.retrofit.model.SmeIPOResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by sparken09 on 27/10/17.
 */

public class ApiTask {
    private final APICall apiCall;

    public ApiTask() {
        Retrofit retrofit = StockApp.getRetrofitInstance();
        apiCall = retrofit.create(APICall.class);
    }

    public Call<?> signUp(String name, String lname, String email, String password,String phone,String imagepath,String device_token,  APICallback onApiCallback) {
        Call<SignUpResponse> call = apiCall.signUp(toRequestBody(name), toRequestBody(lname), toRequestBody(email),
                toRequestBody(password) , toRequestBody(phone),getImage(imagepath),toRequestBody(device_token));
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> signUp(String name, String lname, String email, String password,String phone,String device_token,  APICallback onApiCallback) {
        Call<SignUpResponse> call = apiCall.signUp(name, lname, email,
                password , phone,device_token);
        call.enqueue(onApiCallback);
        return call;
    }

    public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }



    private MultipartBody.Part getImage(String path) {
        File newFile = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), newFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", newFile.getName(), reqFile);
        return body;
    }


    public Call<?> login(String email, String password, String device_token, APICallback onApiCallback) {
        Call<LoginResponse> call = apiCall.login(email, password,device_token);
        call.enqueue(onApiCallback);
        return call;
    }
    public Call<?> logout(APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.logout();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> home(APICallback onApiCallback) {
        Call<HomeResponse> call = apiCall.home();
        call.enqueue(onApiCallback);
        return call;
    }


    public Call<?> getBuyStock(APICallback onApiCallback) {
        Call<BuyStockResponse> call = apiCall.getBuyStock();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getIpo(APICallback onApiCallback) {
        Call<IPOResponse> call = apiCall.getIpo();
        call.enqueue(onApiCallback);
        return call;
    }
    public Call<?> getSmeIpo(APICallback onApiCallback) {
        Call<SmeIPOResponse> call = apiCall.getSmeIpo();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getMutualFund(APICallback onApiCallback) {
        Call<MutualFundRespons> call = apiCall.getMutualFund();
        call.enqueue(onApiCallback);
        return call;
    }
    public Call<?> getNews(APICallback onApiCallback) {
        Call<NewsResponse> call = apiCall.getNews();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getProfile(APICallback onApiCallback) {
        Call<ProfileResponse> call = apiCall.getProfile();
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getUpdateProfile(String firstname, String lastname, String phone ,String imagepath ,APICallback onApiCallback) {
        Call<LoginResponse> call = apiCall.getUpdateProfile(toRequestBody(firstname),toRequestBody(lastname),
                toRequestBody(phone),getImage(imagepath));
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> getUpdateProfile(String firstname, String lastname, String phone ,APICallback onApiCallback) {
        Call<LoginResponse> call = apiCall.getUpdateProfile(firstname, lastname,phone);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> changePassword(String old_password, String new_password ,APICallback onApiCallback){
        Call<MessageResponse> call = apiCall.changePassword(old_password,new_password);
        call.enqueue(onApiCallback);
        return call;
    }

    public Call<?> forgetPassword(String email, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.forgetPassword(email);
        call.enqueue(onApiCallback);
        return call;
    }
    public Call<?> createPassword(String code,String password, APICallback onApiCallback) {
        Call<MessageResponse> call = apiCall.verifyCode(code,password);
        call.enqueue(onApiCallback);
        return call;
    }
}
