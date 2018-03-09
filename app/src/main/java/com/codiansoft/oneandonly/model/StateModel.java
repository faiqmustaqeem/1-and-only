package com.codiansoft.oneandonly.model;

/**
 * Created by Codiansoft on 3/8/2018.
 */

public class StateModel {


    private String name;
    private String id;
    private boolean isAllSelected;
    private boolean isFilterSelected;

    public StateModel(String name,String id)
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
