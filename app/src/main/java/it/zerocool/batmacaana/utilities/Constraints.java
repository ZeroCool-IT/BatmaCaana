/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File it.zerocool.batmacaana.utility/Contraints.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.utilities;

/**
 * Class containing constraint values
 *
 * @author Marco Battisti
 */
public class Constraints {


    //USER ID
    public static final int USER_ID = 594;

    //Empty value
    public static final String EMPTY_VALUE = "";


    //Error fallback type fragment
    public static final int CONNECTION_ERROR = 0;
    public static final int NO_RESULTS = 1;

    //Object types
    public static final int TYPE_PLACE = 0;
    public static final int TYPE_TOSEE = 1;
    public static final int TYPE_EAT = 2;
    public static final int TYPE_SLEEP = 3;
    public static final int TYPE_SERVICE = 4;
    public static final int TYPE_SHOP = 5;
    public static final int TYPE_EVENT = 6;
    public static final int TYPE_NEWS = 7;


    //Navigation Drawer Fragments
    public static final int TOSEE = 0;
    public static final int EVENT = 1;
    public static final int EAT = 2;
    public static final int SLEEP = 3;
    public static final int NEWS = 4;
    public static final int SHOP = 5;
    public static final int SERVICES = 6;
    public static final int ABOUT = 7;
    public static final int SUBHEADER = 8;
    public static final int FAVORITE = 9;
    public static final int SETTINGS = 10;
    public static final int OFFLINE = 11;
    public static final int UPDATE = 12;

    //Place
    public static final int PLACE = 8;

    //Requests URI
    public static final String URI_TOSEE = "http://www.ilmiositodemo.altervista.org/app/json/vedere.php?user=";
    public static final String URI_EAT = "http://www.ilmiositodemo.altervista.org/app/json/mangiare.php?user=";
    public static final String URI_SLEEP = "http://www.ilmiositodemo.altervista.org/app/json/dormire.php?user=";
    public static final String URI_SERVICES = "http://www.ilmiositodemo.altervista.org/app/json/servizi.php?user=";
    public static final String URI_SHOP = "http://www.ilmiositodemo.altervista.org/app/json/shop.php?user=";
    public static final String URI_NEWS = "http://www.ilmiositodemo.altervista.org/app/json/news.php?user=";
    public static final String URI_EVENT = "http://www.ilmiositodemo.altervista.org/app/json/eventi.php?user=";
    public static final String URI_IMAGE_BIG = "http://www.ilmiositodemo.altervista.org/app/images/big/";
    public static final String URI_IMAGE_MEDIUM = "http://www.ilmiositodemo.altervista.org/app/images/big/";
    public static final String URI_SEARCH1 = "http://www.ilmiositodemo.altervista.org/app/json/cerca.php?user=";
    public static final String URI_SEARCH2 = "&stringa=";
    public static final String OBJECT_SEARCH1 = "http://www.ilmiositodemo.altervista.org/app/json/cercadettaglio.php?user=";
    public static final String OBJECT_SEARCH2 = "&id=";
    public static final String OBJECT_SEARCH3 = "&type=";
    public static final String PLACE_URI = "http://www.batmacaana.com/item/";


    //Week Days
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;


    //Latitude and longitude tags for Shared Preferences
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    //Bundle and args tag
    public static final String JSON_ARG = "json";
    public static final String TYPE_ARG = "type";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String QUERY = "query";
    public static final String FRAG_SECTION_ID = "frag_section_id";
    public static final String FALLBACK_TYPE_ARG = "fallback";
    public static final String FALLBACK_REFRESH_ARG = "refresh";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";

    //Shared Preferences
    public static final String PREF_FILE_NAME = "preference";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String KEY_USER_DEFAULT_START_VIEW = "user_default_view";

    //Location Constraints
    public static final int LOCATION_UPDATE_TIME = 120000;
    public static final int LOCATION_MIN_DISTANCE_UPDATE = 100;

    //Others
    public static final int VIEW_STATE_SELECTED = 1;
    public static final int NAV_DRAWER_SUBHEADER = 8;

    /**
     * Private constructor
     */
    private Constraints() {
    }

}
