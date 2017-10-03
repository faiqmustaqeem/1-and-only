package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 4/26/2017.
 */

public class SetCategoriesStatusDataModel {
    String name;
    String categoryName;
    String city;
    String lastUpdateTime;
    String ID;
    String details;
    String contact1;
    String contact2;
    String email;
    String imageURL;
    String latitude;
    String longitude;
    String quantity;
    String status;

    public SetCategoriesStatusDataModel(String categoryName, String lastUpdateTime, String ID, String quantity, String status) {
        this.categoryName=categoryName;
        this.lastUpdateTime=lastUpdateTime;
        this.ID=ID;
        this.quantity=quantity;
        this.status=status;
    }

    public String getCategoryName() {
        return categoryName;
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

    public String getDetails() {
        return details;
    }
    public String getContact1() {
        return contact1;
    }
    public String getContact2() {
        return contact2;
    }
    public String getEmail() {
        return email;
    }
    public String getImageURL() {
        return imageURL;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getQuantity() {
        return quantity;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }
}
