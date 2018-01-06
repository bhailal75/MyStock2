package com.stock.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.stock.retrofit.model.MessageResponse;
import com.stock.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sparken09 on 27/10/17.
 */

public class APICallback implements Callback {
    private int reqCode;
    private OnApiResponseListner onApiResponseListner;
    private Context context;

    public APICallback(Context context, int reqCode, OnApiResponseListner onApiResponseListner) {
        this.context = context;
        this.reqCode = reqCode;
        this.onApiResponseListner = onApiResponseListner;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.code() == APICall.Token_RES_CODE) {

        } else if (response.errorBody() != null) {
            try {
                if (onApiResponseListner != null) {
                    MessageResponse messageResponse = new Gson().fromJson(response.errorBody().string(), MessageResponse.class);
                    onApiResponseListner.onResponseError(messageResponse, reqCode);
                    Utility.log(messageResponse.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onApiResponseListner.onResponseError("", reqCode);
            }

        } else {
            if (onApiResponseListner != null)
                onApiResponseListner.onResponseComplete(response.body(), reqCode);
        }

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        try {
            if (onApiResponseListner != null) {
                if (t != null && t.getMessage().toLowerCase().contains("failed to connect"))
                    onApiResponseListner.onResponseError("Connection Problem", reqCode);
                else if (t != null)
                    onApiResponseListner.onResponseError(t.getMessage(), reqCode);
                else
                    onApiResponseListner.onResponseError("Something wrong", reqCode);
            }
        } catch (Exception ae) {
            ae.printStackTrace();
            onApiResponseListner.onResponseError("", reqCode);
        }
    }
}
