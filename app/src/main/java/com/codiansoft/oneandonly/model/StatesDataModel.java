package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 7/14/2017.
 */

public class StatesDataModel {
    String country;
    String state;
    String lastUpdateTime;
    String ID;
    String quantity;
    boolean onOrOff;

    public StatesDataModel(String country, String state, String lastUpdateTime, String ID, String quantity) {
        this.country = country;
        this.state = state;
        this.lastUpdateTime = lastUpdateTime;
        this.ID = ID;
        this.quantity = quantity;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getID() {
        return ID;
    }

    public void setOnOff(boolean onOrOff) {
        this.onOrOff = onOrOff;
    }

    public boolean getOnOff() {
        return onOrOff;
    }
    public String getQuantity() {
        return quantity;
    }
}
