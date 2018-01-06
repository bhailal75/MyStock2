package com.stock.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmeIPOResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<SmeIPOData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<SmeIPOData> getData() {
return data;
}

public void setData(List<SmeIPOData> data) {
this.data = data;
}

}