package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 4/26/2017.
 */

public class CountriesDataModel {
    String country;
    String city;
    String lastUpdateTime;
    String ID;

    String quantity;
    String stateCode;
    boolean onOrOff;

    public CountriesDataModel(String country, String stateCode, String city, String lastUpdateTime, String ID, String quantity ) {
        this.country=country;
        this.city=city;
        this.lastUpdateTime=lastUpdateTime;
        this.ID=ID;
        this.stateCode=stateCode;

        this.quantity=quantity;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getID() {
        return ID;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setOnOff(boolean onOrOff) {
        this.onOrOff=onOrOff;
    }
    public boolean getOnOff() {
        return onOrOff;
    }
}
