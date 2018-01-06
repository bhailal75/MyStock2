package com.stock.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutualFundRespons {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<MutualFundData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<MutualFundData> getData() {
return data;
}

public void setData(List<MutualFundData> data) {
this.data = data;
}

}