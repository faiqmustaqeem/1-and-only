package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 5/24/2017.
 */

public class CitiesDataModel {

    String stateID;
    String city;
    String lastUpdateTime;
    String ID;
    String quantity;
    boolean onOrOff;

    public CitiesDataModel(String stateID, String city, String lastUpdateTime, String ID, String quantity) {
        this.stateID = stateID;
        this.city = city;
        this.city = city;
        this.lastUpdateTime = lastUpdateTime;
        this.ID = ID;

        this.quantity = quantity;
    }

    public String getState() {
        return stateID;
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
    public void setOnOff(boolean onOrOff) {
        this.onOrOff = onOrOff;
    }

    public boolean getOnOff() {
        return onOrOff;
    }
}