package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 4/27/2017.
 */

public class Level3ListItemDataModel {
    String name;
    String description;
    String cost;
    String ID;
    String quantity;
    String itemImageURL;
    String contact1;
    String contact2;
    String email;
    String latitude;
    String longitude;

    public Level3ListItemDataModel(String name, String description, String cost, String ID,
                                   String quantity,String itemImageURL,String latitude,
                                   String longitude,String contact1,String contact2,String email ) {
        this.name=name;
        this.description=description;
        this.cost=cost;
        this.ID=ID;
        this.quantity=quantity;
        this.itemImageURL=itemImageURL;
        this.contact1=contact1;
        this.contact2=contact2;
        this.email=email;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }

    public String getID() {
        return ID;
    }

    public String getItemImageURL() {
        return itemImageURL;
    }
    public String getContact1() {
        return contact1;
    }
    public String getContact2() {
        return contact2;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getEmail() {
        return email;
    }
}
