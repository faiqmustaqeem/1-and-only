package com.codiansoft.oneandonly.model;

/**
 * Created by Codiansoft on 3/9/2018.
 */

public class CityModel {
    private String name;
    private String id;
    private boolean isSelected;


    public CityModel(String name,String id)
    {
        this.name=name;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
