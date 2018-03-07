package com.codiansoft.oneandonly.model;

/**
 * Created by Codiansoft on 3/7/2018.
 */

public class CountryModel {
    private String name;
    private String id;
    private boolean isAllSelected;
    private boolean isFilterSelected;

    public CountryModel(String name,String id)
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

    public boolean isAllSelected() {
        return isAllSelected;
    }

    public void setAllSelected(boolean allSelected) {
        isAllSelected = allSelected;
    }

    public boolean isFilterSelected() {
        return isFilterSelected;
    }

    public void setFilterSelected(boolean filterSelected) {
        isFilterSelected = filterSelected;
    }
}
