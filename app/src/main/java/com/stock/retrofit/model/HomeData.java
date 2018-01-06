package com.stock.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stock.retrofit.WebAPI;

public class HomeData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("script_name")
@Expose
private String scriptName;
@SerializedName("script_code")
@Expose
private String scriptCode;
@SerializedName("entry")
@Expose
private String entry;
@SerializedName("stop_loss")
@Expose
private String stopLoss;
@SerializedName("targets")
@Expose
private String targets;
@SerializedName("target_date")
@Expose
private String targetDate;
@SerializedName("ipo_name")
@Expose
private String ipoName;
@SerializedName("price")
@Expose
private String price;
@SerializedName("rating")
@Expose
private String rating;
@SerializedName("subscription")
@Expose
private String subscription;
@SerializedName("lot_size")
@Expose
private String lotSize;
@SerializedName("issue_size")
@Expose
private String issueSize;
@SerializedName("face_value")
@Expose
private String faceValue;
    @SerializedName("allotment_date")
    @Expose
    private String allotmentDate;
@SerializedName("ipo_date")
@Expose
private String ipoDate;
    @SerializedName("listing_date")
    @Expose
    private String listingDate;

    public String getAllotmentDate() {
        return allotmentDate;
    }

    public void setAllotmentDate(String allotmentDate) {
        this.allotmentDate = allotmentDate;
    }

    public String getListingDate() {
        return listingDate;
    }

    public void setListingDate(String listingDate) {
        this.listingDate = listingDate;
    }

    @SerializedName("fund_name")
@Expose
private String fundName;
@SerializedName("week_high")
@Expose
private String weekHigh;
@SerializedName("week_low")
@Expose
private String weekLow;
@SerializedName("one_month")
@Expose
private String oneMonth;
@SerializedName("three_month")
@Expose
private String threeMonth;
@SerializedName("six_month")
@Expose
private String sixMonth;
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
@SerializedName("title")
@Expose
private String title;
@SerializedName("description")
@Expose
private String description;
@SerializedName("image")
@Expose
private String image;
@SerializedName("sme_ipo_name")
@Expose
private String smeIpoName;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getScriptName() {
return scriptName;
}

public void setScriptName(String scriptName) {
this.scriptName = scriptName;
}

public String getScriptCode() {
return scriptCode;
}

public void setScriptCode(String scriptCode) {
this.scriptCode = scriptCode;
}

public String getEntry() {
return entry;
}

public void setEntry(String entry) {
this.entry = entry;
}

public String getStopLoss() {
return stopLoss;
}

public void setStopLoss(String stopLoss) {
this.stopLoss = stopLoss;
}

public String getTargets() {
return targets;
}

public void setTargets(String targets) {
this.targets = targets;
}

public String getTargetDate() {
return targetDate;
}

public void setTargetDate(String targetDate) {
this.targetDate = targetDate;
}

public String getIpoName() {
return ipoName;
}

public void setIpoName(String ipoName) {
this.ipoName = ipoName;
}

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public String getRating() {
return rating;
}

public void setRating(String rating) {
this.rating = rating;
}

public String getSubscription() {
return subscription;
}

public void setSubscription(String subscription) {
this.subscription = subscription;
}

public String getLotSize() {
return lotSize;
}

public void setLotSize(String lotSize) {
this.lotSize = lotSize;
}

public String getIssueSize() {
return issueSize;
}

public void setIssueSize(String issueSize) {
this.issueSize = issueSize;
}

public String getFaceValue() {
return faceValue;
}

public void setFaceValue(String faceValue) {
this.faceValue = faceValue;
}

public String getIpoDate() {
return ipoDate;
}

public void setIpoDate(String ipoDate) {
this.ipoDate = ipoDate;
}

public String getFundName() {
return fundName;
}

public void setFundName(String fundName) {
this.fundName = fundName;
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

public String getOneMonth() {
return oneMonth;
}

public void setOneMonth(String oneMonth) {
this.oneMonth = oneMonth;
}

public String getThreeMonth() {
return threeMonth;
}

public void setThreeMonth(String threeMonth) {
this.threeMonth = threeMonth;
}

public String getSixMonth() {
return sixMonth;
}

public void setSixMonth(String sixMonth) {
this.sixMonth = sixMonth;
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

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getImage() {
return WebAPI.NEWS_IMAGE+image;
}

public void setImage(String image) {
this.image = image;
}

public String getSmeIpoName() {
return smeIpoName;
}

public void setSmeIpoName(String smeIpoName) {
this.smeIpoName = smeIpoName;
}

}