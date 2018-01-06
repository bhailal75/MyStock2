package com.stock.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutualFundData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("fund_name")
@Expose
private String fundName;
@SerializedName("price")
@Expose
private String price;
@SerializedName("week_high")
@Expose
private String weekHigh;
@SerializedName("week_low")
@Expose
private String weekLow;
@SerializedName("one_year")
@Expose
private String oneYear;
@SerializedName("two_year")
@Expose
private String twoYear;
@SerializedName("three_year")
@Expose
private String threeYear;
@SerializedName("five_year")
@Expose
private String fiveYear;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getFundName() {
return fundName;
}

public void setFundName(String fundName) {
this.fundName = fundName;
}

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public String getWeekHigh() {
return weekHigh;
}

public void setWeekHigh(String weekHigh) {
this.weekHigh = weekHigh;
}

public String getWeekLow() {
return weekLow;
}

public void setWeekLow(String weekLow) {
this.weekLow = weekLow;
}

public String getOneYear() {
return oneYear;
}

public void setOneYear(String oneYear) {
this.oneYear = oneYear;
}

public String getTwoYear() {
return twoYear;
}

public void setTwoYear(String twoYear) {
this.twoYear = twoYear;
}

public String getThreeYear() {
return threeYear;
}

public void setThreeYear(String threeYear) {
this.threeYear = threeYear;
}

public String getFiveYear() {
return fiveYear;
}

public void setFiveYear(String fiveYear) {
this.fiveYear = fiveYear;
}

}