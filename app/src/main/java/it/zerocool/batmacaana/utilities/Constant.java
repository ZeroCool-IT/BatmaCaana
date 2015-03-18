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
public class Constant {


    //USER ID
    public static final int USER_ID = 594;
    public static final int NULL_UID = 593;

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
    public static final int TYPE_CITY = 5;
    public static final int TYPE_EVENT = 6;
    public static final int TYPE_NEWS = 7;
    public static final int TYPE_ROUTE = 8;


    //Navigation Drawer Fragments
    public static final int ABOUT = 0;
    public static final int TOSEE = 1;
    public static final int EVENT = 2;
    public static final int EAT = 3;
    public static final int SLEEP = 4;
    public static final int NEWS = 5;
    public static final int SERVICES = 6;
    public static final int ROUTES = 7;
    public static final int SUBHEADER = 8;
    public static final int CITY = 9;
    public static final int FAVORITE = 10;
    public static final int SETTINGS = 11;
    public static final int CREDITS = 12;
    public static final int OFFLINE = 14;
    public static final int UPDATE = 15;

    //Place
    public static final int PLACE = 8;

    //Requests URI
    public static final String URI_TOSEE = "http://www.exploracity.it/app/json/vedere.php?user=";
    public static final String URI_EAT = "http://www.exploracity.it/app/json/mangiare.php?user=";
    public static final String URI_SLEEP = "http://www.exploracity.it/app/json/dormire.php?user=";
    public static final String URI_SERVICES = "http://www.exploracity.it/app/json/servizi.php?user=";
    public static final String URI_NEARBY = "http://www.exploracity.it/app/json/vicini.php?user=";
    public static final String URI_NEWS = "http://www.exploracity.it/app/json/news.php?user=";
    public static final String URI_EVENT = "http://www.exploracity.it/app/json/eventi.php?user=";
    public static final String URI_IMAGE_BIG = "http://www.exploracity.it/app/images/big/";
    public static final String URI_IMAGE_MEDIUM = "http://www.exploracity.it/app/images/big/";
    public static final String URI_SEARCH1 = "http://www.exploracity.it/app/json/cerca.php?user=";
    public static final String URI_SEARCH2 = "&stringa=";
    public static final String OBJECT_SEARCH1 = "http://www.exploracity.it/app/json/cercadettaglio.php?user=";
    public static final String OBJECT_SEARCH2 = "&id=";
    public static final String OBJECT_SEARCH3 = "&type=";
    public static final String SHARE_URI = "http://exploracity.it/item/";
    public static final String URI_ROUTES = "http://www.exploracity.it/app/json/percorsi.php?user=";
    public static final String URI_KML = "http://www.exploracity.it/app/kml/";
    public static final String URI_CITY = "http://www.exploracity.it/app/json/comune.php?user=";


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
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String FLAG_FROM_NOTIFICATION = "notification";
    public static final String ID_ARG = "id";
    public static final String MESSAGE_ARG = "message";
    public static final String LANDSCAPE_ORIENTATION = "orientation";
    public static final String FROM_GALLERY = "from_gallery";

    //Shared Preferences
    public static final String PREF_FILE_NAME = "preference";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String KEY_USER_DEFAULT_START_VIEW = "user_default_view";
    public static final String KEY_NEWS_NOTIFICATION_NUMBER = "news_notification_number";
    public static final String KEY_EVENT_NOTIFICATION_NUMBER = "event_notification_number";
    public static final String NOTIFICATION_PREFS = "notification";
    public static final String EARTH_NOT_NEEDED = "earth";
    public static final String SPLASH = "splash";


    //Location Constraints
    public static final int LOCATION_UPDATE_TIME = 30000;
    public static final int LOCATION_MIN_DISTANCE_UPDATE = 50;

    //Gallery image

    /*public static final Integer[] GALLERY_IMAGE = {R.drawable.im_gallery_01, R.drawable.im_gallery_02,
            R.drawable.im_gallery_03, R.drawable.im_gallery_04, R.drawable.im_gallery_05, R.drawable.im_gallery_06,
            R.drawable.im_gallery_07, R.drawable.im_gallery_08, R.drawable.im_gallery_09, R.drawable.im_gallery_10};*/

    //Others
    public static final int VIEW_STATE_SELECTED = 100;
    public static final int NAV_DRAWER_SUBHEADER = 8;
    public static final long M_BACK_PRESS_THRESHOLD = 3500;

    /**
     * Private constructor
     */
    private Constant() {
    }

}
