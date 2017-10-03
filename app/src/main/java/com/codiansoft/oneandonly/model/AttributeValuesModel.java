package com.codiansoft.oneandonly.model;

/**
 * Created by salal-khan on 6/5/2017.
 */

public class AttributeValuesModel {
    String attributeValue;

    public AttributeValuesModel(String attributeValue) {
        this.attributeValue = attributeValue;

    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeKey(String value) {
        this.attributeValue = value;
    }
}
