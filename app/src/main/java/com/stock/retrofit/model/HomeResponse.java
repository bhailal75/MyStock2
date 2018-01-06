package com.stock.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<HomeDatum> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<HomeDatum> getData() {
return data;
}

public void setData(List<HomeDatum> data) {
this.data = data;
}

}