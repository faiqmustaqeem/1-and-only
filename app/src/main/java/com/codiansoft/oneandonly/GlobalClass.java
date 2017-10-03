package com.codiansoft.oneandonly;

import java.util.ArrayList;

/**
 * Created by salal-khan on 4/20/2017.
 */

public class GlobalClass {
    public static final String EDIT_MY_AD = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/edit_ad";
    public static final String REPORT_AD_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/spamReport";
    public static String APPLICATION_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/api";
    public static String REGISTER_URL = APPLICATION_URL + "/user/register";
    public static String LOGIN_URL = APPLICATION_URL + "/user/login";
    public static String CATEGORIES_URL = APPLICATION_URL + "/user/categories";
    public static String CATEGORIES_STATUS_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/category_switch";
    public static String UPLOAD_AD_URL = APPLICATION_URL + "/user/addPost";
    public static String FETCH_ALL_ADDS_URL = APPLICATION_URL + "/user/fetchAds";
    public static String UPLOAD_AD_IMAGE_URL = APPLICATION_URL + "/user/base64";
    public static String FETCH_PROFILE_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/fetchProfile";
    public static String UPDATE_PROFILE_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/updateProfile";
    public static String FETCH_COUNTRIES_URL = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/fetchCountries";
    public static String FETCH_STATES_URL = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/fetchStates";
    public static String FETCH_CITIES_URL = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/fetchCities";
    public static String FETCH_MY_ADS_URL = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/fetchUserAds";
    public static String MY_AD_STATUS_CHANGE_URL = "http://codiansoft.com/salal/OneAndOnly/index.php/Api/change_status";
    public static String AD_ACTIVE_DAYS_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/adInactivateTime";
    public static String REMOVE_AD_FOR_USER_URL = "http://codiansoft.com:80/salal/OneAndOnly/index.php/Api/adRemove";

    public static String selectedCategory;
    public static String selectedSubCategory;
    public static String selectedSubCategoryID;
    public static String selectedCategoryID;

    public static boolean alreadyLoggedIn;

    public static boolean userNotRegisteredYet;

    //for selected category in level 1
    public static String selectedLevel1CategoryName;
    public static String selectedLevelCategoryDescription;
    public static String selectedLevelCategoryUpdateTime;
    public static String selectedLevelCategoryID;
    public static String selectedLevelCategoryQuantity;

    //for selected category in level 2
    public static String selectedLevel2Country;
    public static String selectedLevel2CountryID;
    public static String selectedLevel2UpdateTime;
    public static String selectedLevel2City;
    public static String selectedLevel2CityID;
    public static String selectedLevel2State;
    public static String selectedLevel2StateID;
    public static String selectedLevel2Quantity;

    //for selected category in level 3
    public static String selectedLevel3ItemName;
    public static String selectedLevel3ItemCost;
    public static String selectedLevel3ItemID;
    public static String selectedLevel3ItemDescription;
    public static String selectedLevel3ItemImageURL;
    public static String selectedLevel3ContactNumber1;
    public static String selectedLevel3ContactNumber2;
    public static String selectedLevel3ItemEmail;
    public static String selectedLevel3Latitude;
    public static String selectedLevel3Longitude;

    //for selected property from properties list
    public static String selectedPropertyName;
    public static String selectedPropertyCategory;
    public static String selectedPropertyCity;
    public static String selectedPropertyUpdateTime;
    public static String selectedPropertyID;
    public static String selectedPropertyDetails;
    public static String selectedPropertyContact1;
    public static String selectedPropertyContact2;
    public static String selectedPropertyEmail;
    public static String selectedPropertyImageURL;
    public static String selectedPropertyLatitude;
    public static String selectedPropertyLongitude;

    public static String uploadingAdID;
    public static String selectedAddPostType;
    public static String selectedAddPostCountry = "";
    public static String selectedAddPostCountryID = "";
    public static String selectedAddPostState = "";
    public static String selectedAddPostStateID = "";
    public static String selectedAddPostCity = "";
    public static String selectedAddPostCityID = "";
    public static StringBuilder selectedAddPostCityIDs = new StringBuilder("");
    public static StringBuilder selectedAddPostCityNames = new StringBuilder("");
    public static StringBuilder selectedAddPostStateIDs = new StringBuilder("");
    public static StringBuilder selectedAddPostStateNames = new StringBuilder("");
    public static boolean isSelectAllStates = false;
    public static boolean postingAd;
    public static ArrayList<String> selectedAdImages;
    public static String selectedAdPic;

    public static String myAdEditID;
    public static String myAdEditTitle;
    public static String myAdEditDescription;
    public static String myAdEditCurrencyCode;
    public static String myAdEditPrice;

    public static String selectedSubCatDes1Title = "D1";
    public static String selectedSubCatDes2Title = "D2";
    public static String selectedSubCatDes3Title = "D3";
    public static String selectedSubCatDes4Title = "D4";
    public static String userCountryID = "";
    ;
}