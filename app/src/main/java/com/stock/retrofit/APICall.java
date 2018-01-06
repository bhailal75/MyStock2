package com.stock.retrofit;

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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sparken09 on 27/10/17.
 */

public interface APICall {
    int Token_RES_CODE = 401;
    int BAD_REQUEST = 400;

    int REGISTER_REQ_CODE = 1;
    int LOGIN_CODE = 2;
    int BUY_STOCK_CODE = 3;
    int LOGOUT_REQ_CODE = 4;
    int IPO_CODE = 5;
    int MUTUALFUND_CODE = 6;
    int NEWS_CODE = 7;
    int PROFILE_CODE = 8;
    int UPDATE_PROFILE_CODE = 9;
    int FORGET_PASSWORD = 10;
    int VERIFY_CODE = 11;
    int HOME = 12;
    int SME_IPO_CODE = 13 ;
    int CHANGE_PASSWORD_CODE = 14;




    @Multipart
    @POST(WebAPI.REGISTER)
    Call<SignUpResponse> signUp(@Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password,
                                @Part("phone") RequestBody phone,
                                @Part MultipartBody.Part image,
                                @Part("device_token") RequestBody device_token);

    @FormUrlEncoded
    @POST(WebAPI.REGISTER)
    Call<SignUpResponse> signUp(@Field("first_name") String first_name,
                                @Field("last_name") String last_name,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("phone") String phone,
                                @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(WebAPI.LOGIN)
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token")String device_token);

    @GET(WebAPI.HOME)
    Call<HomeResponse> home();

    @GET(WebAPI.LOGOUT)
    Call<MessageResponse> logout();

    @GET(WebAPI.BUY_STOCK)
    Call<BuyStockResponse> getBuyStock();

    @GET(WebAPI.IPO)
    Call<IPOResponse> getIpo();

    @GET(WebAPI.SME_IPO)
    Call<SmeIPOResponse> getSmeIpo();

    @GET(WebAPI.MUTUALFUND)
    Call<MutualFundRespons> getMutualFund();

    @GET(WebAPI.NEWS)
    Call<NewsResponse> getNews();

    @GET(WebAPI.PROFILE)
    Call<ProfileResponse> getProfile();

    @Multipart
    @POST(WebAPI.UPDATE_PROFILE)
    Call<LoginResponse> getUpdateProfile(@Part("first_name") RequestBody first_name,
                                         @Part("last_name") RequestBody last_name,
                                         @Part("phone") RequestBody phone,
                                         @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(WebAPI.UPDATE_PROFILE)
    Call<LoginResponse> getUpdateProfile(@Field("first_name") String first_name,
                                         @Field("last_name") String last_name,
                                         @Field("phone") String phone);

    @FormUrlEncoded
    @POST(WebAPI.CHANGE_PASSWORD)
    Call<MessageResponse>changePassword(@Field("old_password") String old_password,
                                        @Field("new_password") String new_password);

    @FormUrlEncoded
    @POST(WebAPI.FORGOT_PASSWORD)
    Call<MessageResponse> forgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST(WebAPI.VERIFY_CODE)
    Call<MessageResponse> verifyCode(@Field("code") String code, @Field("new_password") String new_password);

}
