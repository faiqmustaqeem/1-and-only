package com.codiansoft.oneandonly.model;

import java.util.ArrayList;

/**
 * Created by Codiansoft on 8/1/2017.
 */

public class MyAdsModel {
    String name;
    String category;
    String subCategory;
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
    String price;
    String currencyCode;
    String countryName;
    String stateName;
    String cityName;
    String des1;
    String des2;
    String des3;
    String des4;
    ArrayList<String> adImages = new ArrayList<String>();

    public MyAdsModel(String name, String category, String subCategory, String city, String lastUpdateTime, String ID, String details, String contact1,
                                     String contact2, String email, String imageURL, String latitude, String longitude, String price, String currencyCode, String countryName, String stateName, String cityName, ArrayList<String> adImages, String des1, String des2, String des3, String des4) {
        this.name=name;
        this.category=category;
        this.subCategory=subCategory;
        this.city=city;
        this.lastUpdateTime=lastUpdateTime;
        this.ID=ID;
        this.details=details;
        this.contact1=contact1;
        this.contact2=contact2;
        this.email=email;
        this.imageURL=imageURL;
        this.latitude=latitude;
        this.longitude=longitude;
        this.price=price;
        this.currencyCode=currencyCode;
        this.adImages=adImages;
        this.countryName=countryName;
        this.stateName=stateName;
        this.cityName=cityName;
        this.des1=des1;
        this.des2=des2;
        this.des3=des3;
        this.des4=des4;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public String getSubCategory() {
        return subCategory;
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
    public String getPrice() {
        return price;
    }
    public ArrayList<String> getAdImages() {
        return adImages;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
    public String getCountryName() {
        return countryName;
    }
    public String getStateName() {
        return stateName;
    }
    public String getCityname() {
        return cityName;
    }

    public String getDes1() {
        return des1;
    }

    public String getDes2() {
        return des2;
    }

    public String getDes3() {
        return des3;
    }

    public String getDes4() {
        return des4;
    }
}
