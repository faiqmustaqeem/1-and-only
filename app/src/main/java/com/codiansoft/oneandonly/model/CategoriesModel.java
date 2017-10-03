package com.codiansoft.oneandonly.model;

import java.util.ArrayList;

/**
 * Created by salal-khan on 6/22/2017.
 */

public class CategoriesModel {

    ArrayList<ArrayList<String>> descriptionHeadings;
    String category_Id;
    String name;
    String lastUpdated;
    String status;
    String quantity;
    ArrayList<String> sub_category_IDs;
    ArrayList<String> sub_category_names;
    ArrayList<String> sub_category_last_update;
    ArrayList<String> sub_category_ads_quantity;

    public CategoriesModel(String category_Id, String name, String lastUpdated, ArrayList<String> sub_category_IDs, ArrayList<String> sub_category_names, ArrayList<String> sub_category_last_update, ArrayList<String> sub_category_ads_quantity, String status, String quantity, ArrayList<ArrayList<String>> descriptionHeadings) {
        this.name = name;
        this.category_Id = category_Id;
        this.lastUpdated = lastUpdated;
        this.sub_category_IDs = sub_category_IDs;
        this.sub_category_names = sub_category_names;
        this.status = status;
        this.quantity = quantity;

        this.sub_category_last_update = sub_category_last_update;
        this.sub_category_ads_quantity = sub_category_ads_quantity;
        this.descriptionHeadings = descriptionHeadings;
    }

    public String getCategory_Id() {
        return category_Id;
    }

    public String getName() {
        return name;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getCategorySatus() {
        return status;
    }

    public String getQuantity() {
        return quantity;
    }

    public ArrayList<String> getSubCategoriesIDs() {
        return sub_category_IDs;
    }

    public ArrayList<String> getSubCategoriesNames() {
        return sub_category_names;
    }
    public ArrayList<String> getSubCategoriesLastUpdateTime() {
        return sub_category_last_update;
    }

    public ArrayList<String> getSubCategoriesAdsQuantity() {
        return sub_category_ads_quantity;
    }

    public ArrayList<ArrayList<String>> getSubCatDesTitles() {
        return descriptionHeadings;
    }
}
