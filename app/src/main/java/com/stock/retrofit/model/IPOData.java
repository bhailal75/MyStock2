package com.stock.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fr.xebia.android.freezer.annotations.Model;

@Model
public class IPOData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("ipo_name")
    @Expose
    public String ipoName;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("subscription")
    @Expose
    public String subscription;
    @SerializedName("lot_size")
    @Expose
    public String lotSize;
    @SerializedName("issue_size")
    @Expose
    public String issueSize;
    @SerializedName("face_value")
    @Expose
    public String faceValue;
    @SerializedName("ipo_date")
    @Expose
    public String ipoDate;
    @SerializedName("allotment_date")
    @Expose
    public String allotmentDate;
    @SerializedName("listing_date")
    @Expose
    public String listingDate;

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

    public boolean flag;

    public Integer getId() {
        return id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setId(Integer id) {
        this.id = id;
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

}