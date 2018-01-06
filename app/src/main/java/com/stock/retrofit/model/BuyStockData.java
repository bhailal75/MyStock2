package com.stock.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyStockData {

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

}