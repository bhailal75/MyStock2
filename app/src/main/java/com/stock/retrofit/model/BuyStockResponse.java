package com.stock.retrofit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyStockResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private List<BuyStockData> data = null;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<BuyStockData> getData() {
return data;
}

public void setData(List<BuyStockData> data) {
this.data = data;
}

}