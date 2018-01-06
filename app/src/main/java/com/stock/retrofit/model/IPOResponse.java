package com.stock.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IPOResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<IPOData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<IPOData> getData() {
return data;
}

public void setData(List<IPOData> data) {
this.data = data;
}

}