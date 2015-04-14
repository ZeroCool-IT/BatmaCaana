/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File it.zerocool.batmacaana.utility/ParsingUtilities.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.utilities;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.model.Cardable;
import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.model.ContactCard;
import it.zerocool.batmacaana.model.Eat;
import it.zerocool.batmacaana.model.Event;
import it.zerocool.batmacaana.model.News;
import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.model.Route;
import it.zerocool.batmacaana.model.SearchResult;
import it.zerocool.batmacaana.model.Service;
import it.zerocool.batmacaana.model.Shop;
import it.zerocool.batmacaana.model.Sleep;
import it.zerocool.batmacaana.model.TimeCard;
import it.zerocool.batmacaana.model.ToSee;

/**
 * Utility class for data parsing
 *
 * @author Marco Battisti
 */
public class ParsingUtilities {

    /**
     * There isn't a public constructor, it's an utility class!
     */
    private ParsingUtilities() {
        // Private constructor
    }

    /**
     * Build a GregorianCalendar date from a String (YYYY-mm-DD format)
     *
     * @param d is the date to build
     * @return the date in GregorianCalendar format
     */
    @Nullable
    public static GregorianCalendar parseDate(@Nullable String d) {
        if (d != null && !d.equals(Constant.EMPTY_VALUE)) {
            GregorianCalendar result = new GregorianCalendar();
            StringTokenizer tokenizer = new StringTokenizer(d, "-");
            while (tokenizer.hasMoreTokens()) {
                String toSet = tokenizer.nextToken();
                result.set(GregorianCalendar.YEAR, Integer.parseInt(toSet));
                toSet = tokenizer.nextToken();
                result.set(GregorianCalendar.MONTH, Integer.parseInt(toSet) - 1);
                toSet = tokenizer.nextToken();
                result.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(toSet));
            }
            return result;
        }
        return null;
    }

    /**
     * Build a GregorianCalendar hour from a String (HH:mm format)
     *
     * @param h is the hour to build
     * @return the hour in GregorianCalendar format
     */
    @Nullable
    public static GregorianCalendar parseHour(@Nullable String h) {
        if (h != null && !h.equals(Constant.EMPTY_VALUE)) {
            GregorianCalendar result = new GregorianCalendar();
            StringTokenizer tokenizer = new StringTokenizer(h, ":");
            while (tokenizer.hasMoreTokens()) {
                String toSet = tokenizer.nextToken();
                result.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt(toSet));
                toSet = tokenizer.nextToken();
                result.set(GregorianCalendar.MINUTE, Integer.parseInt(toSet));
            }
            return result;
        }
        return null;
    }

    /**
     * Build an ArrayList containing Place objects from a string in JSON format
     *
     * @param json is the JSON string
     * @return the list of Place objects
     */
    @NonNull
    public static ArrayList<Cardable> parsePlaceFromJSON(String json, Location currentLocation) {
        ArrayList<Cardable> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Luoghi");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int type = Integer.parseInt(toBuild.getString("TYPE"));
                    int id = Integer.parseInt((toBuild.getString("LUOGO_ID")));
                    Place p;
                    switch (type) {
                        case Constant.TYPE_TOSEE:
                            p = new ToSee(id);
                            break;
                        case Constant.TYPE_EAT:
                            p = new Eat(id);
                            break;
                        case Constant.TYPE_SLEEP:
                            p = new Sleep(id);
                            break;
                        case Constant.TYPE_SERVICE:
                            p = new Service(id);
                            break;
                        case Constant.TYPE_CITY:
                            p = new Shop(id);
                            break;
                        default:
                            p = null;
                            break;
                    }
                    assert p != null;
                    p.setType(type);
                    p.setJson(toBuild.toString());
                    p.setName(toBuild.getString("NAME"));
                    p.setDescription(toBuild.getString("DESCRIPTION"));
                    p.setImage(toBuild.getString("IMAGE"));
                    p.setTagsFromCSV(toBuild.getString("TAGS"));
                    ContactCard c = new ContactCard();
                    c.setAddress(toBuild.getString("ADDRESS"));
                    c.setEmail(toBuild.getString("EMAIL"));
                    c.setUrl(toBuild.getString("URL"));
                    c.setTelephone(toBuild.getString("TELEPHONENUMBER"));
                    c.setFbLink(toBuild.getString("FACEBOOK"));
                    c.setFsqrLink(toBuild.getString("FOURSQUARE"));
                    c.setTaLink(toBuild.getString("TRIPADVISOR"));
                    c.setGpLink(toBuild.getString("GOOGLEPLUS"));
                    p.setContact(c);
                    Location l = new Location("");
                    String latitude = toBuild.getString("LATITUDE");
                    String longitude = toBuild.getString("LONGITUDE");
                    l.setLatitude(Location.convert(latitude));
                    l.setLongitude(Location.convert(longitude));
                    p.setLocation(l);
                    TimeCard t = new TimeCard();
                    t.setAmOpening(toBuild.getString("AMOPENING"));
                    t.setAmClosing(toBuild.getString("AMCLOSING"));
                    t.setPmOpening(toBuild.getString("PMOPENING"));
                    t.setPmClosing(toBuild.getString("PMCLOSING"));
                    t.setClosingDaysFromCSV(toBuild.getString("CLOSINGDAYS"));
                    t.setNotes(toBuild.getString("NOTES"));
                    p.setTimeCard(t);
                    LocationUtilities.setPlaceDistance(p, currentLocation);
                    p.setAccessible(toBuild.getString("ACCESSIBILITY"));
                    result.add(p);
                }
            }

        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("Coordinate exception", e.getMessage());
        }
        return result;
    }

    /**
     * Build a single place object from json string
     *
     * @param json is the JSON string
     * @return the place object
     */
    @Nullable
    public static Place parseSinglePlace(String json) {
        try {
            JSONObject toBuild = new JSONObject(json);
            int type = Integer.parseInt(toBuild.getString("TYPE"));
            int id = Integer.parseInt((toBuild.getString("LUOGO_ID")));
            Place p;
            switch (type) {
                case Constant.TYPE_TOSEE:
                    p = new ToSee(id);
                    break;
                case Constant.TYPE_EAT:
                    p = new Eat(id);
                    break;
                case Constant.TYPE_SLEEP:
                    p = new Sleep(id);
                    break;
                case Constant.TYPE_SERVICE:
                    p = new Service(id);
                    break;
                case Constant.TYPE_CITY:
                    p = new Shop(id);
                    break;
                default:
                    p = null;
                    break;
            }
            assert p != null;
            p.setType(type);
            p.setJson(toBuild.toString());
            p.setName(toBuild.getString("NAME"));
            p.setDescription(toBuild.getString("DESCRIPTION"));
            p.setImage(toBuild.getString("IMAGE"));
            p.setTagsFromCSV(toBuild.getString("TAGS"));
            ContactCard c = new ContactCard();
            c.setAddress(toBuild.getString("ADDRESS"));
            c.setEmail(toBuild.getString("EMAIL"));
            c.setUrl(toBuild.getString("URL"));
            c.setTelephone(toBuild.getString("TELEPHONENUMBER"));
            c.setFbLink(toBuild.getString("FACEBOOK"));
            c.setFsqrLink(toBuild.getString("FOURSQUARE"));
            c.setTaLink(toBuild.getString("TRIPADVISOR"));
            c.setGpLink(toBuild.getString("GOOGLEPLUS"));
            p.setContact(c);
            Location l = new Location("");
            String latitude = toBuild.getString("LATITUDE");
            String longitude = toBuild.getString("LONGITUDE");
            l.setLatitude(Location.convert(latitude));
            l.setLongitude(Location.convert(longitude));
            p.setLocation(l);
            TimeCard t = new TimeCard();
            t.setAmOpening(toBuild.getString("AMOPENING"));
            t.setAmClosing(toBuild.getString("AMCLOSING"));
            t.setPmOpening(toBuild.getString("PMOPENING"));
            t.setPmClosing(toBuild.getString("PMCLOSING"));
            t.setClosingDaysFromCSV(toBuild.getString("CLOSINGDAYS"));
            t.setNotes(toBuild.getString("NOTES"));
            p.setTimeCard(t);
            p.setAccessible(toBuild.getString("ACCESSIBILITY"));
            return p;
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;
    }

// --Commented out by Inspection START (05/03/2015 16:36):
//    /**
//     * Build an ArrayList containing MainCity objects from a string in JSON format
//     *
//     * @param json is the JSON string
//     * @return the list of MainCity objects
//     */
//    public static ArrayList<MainCity> parseMainCitiesFromJSON(String json) {
//        ArrayList<MainCity> res = new ArrayList<>();
//        try {
//            JSONObject reader = new JSONObject(json);
//            JSONArray data = reader.getJSONArray("Comune");
//            if (data != null) {
//                for (int i = 0; i < data.length(); i++) {
//                    JSONObject toBuild = data.getJSONObject(i);
//                    int userId = Integer.parseInt(toBuild.getString("cf_user_id"));
//                    int id = Integer.parseInt(toBuild.getString("ID"));
//                    MainCity m = new MainCity(id, userId);
//                    m.setName(toBuild.getString("NAME"));
//                    String phone = toBuild.getString("TELEFONO");
//                    String mail = toBuild.getString("EMAIL");
//                    String url = toBuild.getString("URL");
//                    ContactCard c = new ContactCard();
//                    c.setUrl(url);
//                    c.setEmail(mail);
//                    c.setTelephone(phone);
//                    m.setContactCard(c);
//                    m.setHistory(toBuild.getString("STORIA"));
//                    Location l = new Location("");
//                    String latitude = toBuild.getString("LATITUDE");
//                    String longitude = toBuild.getString("LONGITUDE");
//                    if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
//                        l.setLatitude(Location.convert(latitude));
//                        l.setLongitude(Location.convert(longitude));
//                        m.setLocation(l);
//                    }
//                    ArrayList<String> images = new ArrayList<>();
//                    for (int j = 1; j <= 10; j++) {
//                        String im = toBuild.optString("IMAGE" + Integer.valueOf(j).toString());
//                        images.add(im);
//                    }
//                    m.setImages(images);
//                    m.setInfos(toBuild.getString("INFO"));
//                    res.add(m);
//                }
//            }
//        } catch (JSONException e) {
//            Log.e("JSON Exception", e.getMessage());
//        }
//        return res;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:36)

// --Commented out by Inspection START (05/03/2015 16:36):
//    /**
//     * Build a single MainCity object from json string
//     *
//     * @param json is the JSON string
//     * @return the MainCity object
//     */
//    public static MainCity parseSingleMainCity(String json) {
//        try {
//            JSONObject toBuild = new JSONObject(json);
//            int userId = Integer.parseInt(toBuild.getString("cf_user_id"));
//            int id = Integer.parseInt(toBuild.getString("ID"));
//            MainCity m = new MainCity(id, userId);
//            m.setName(toBuild.getString("NAME"));
//            String phone = toBuild.getString("TELEFONO");
//            String mail = toBuild.getString("EMAIL");
//            String url = toBuild.getString("URL");
//            ContactCard c = new ContactCard();
//            c.setUrl(url);
//            c.setEmail(mail);
//            c.setTelephone(phone);
//            m.setContactCard(c);
//            m.setHistory(toBuild.getString("STORIA"));
//            Location l = new Location("");
//            String latitude = toBuild.getString("LATITUDE");
//            String longitude = toBuild.getString("LONGITUDE");
//            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
//                l.setLatitude(Location.convert(latitude));
//                l.setLongitude(Location.convert(longitude));
//                m.setLocation(l);
//            }
//            ArrayList<String> images = new ArrayList<>();
//            for (int j = 1; j <= 10; j++) {
//                String im = toBuild.optString("IMAGE" + Integer.valueOf(j).toString());
//                images.add(im);
//            }
//            m.setImages(images);
//            m.setInfos(toBuild.getString("INFO"));
//            return m;
//        } catch (JSONException e) {
//            Log.e("JSON Exception", e.getMessage());
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:36)

    /**
     * Build an ArrayList containing Event objects from a string in JSON format
     *
     * @param json is the JSON string
     * @return the list of Event objects
     */
    @NonNull
    public static ArrayList<Cardable> parseEventFromJSON(String json) {
        ArrayList<Cardable> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Eventi");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int id = Integer.parseInt(toBuild.getString("EVENTO_ID"));
                    Event e = new Event(id);
                    e.setJson(toBuild.toString());
                    e.setType(toBuild.getInt("TYPE"));
                    e.setName(toBuild.getString("NAME"));
                    e.setDescription(toBuild.getString("DESCRIPTION"));
                    e.setPlace(toBuild.getString("PLACE"));
                    ContactCard c = new ContactCard();
                    c.setAddress(toBuild.getString("ADDRESS"));
                    c.setEmail(toBuild.getString("EMAIL"));
                    c.setTelephone(toBuild.getString("TELEPHONENUMBER"));
                    c.setUrl(toBuild.getString("URL"));
                    e.setContact(c);
                    e.setImage(toBuild.getString("IMAGE"));
                    e.setTagsFromCSV(toBuild.getString("TAGS"));
                    e.setStartDate(toBuild.getString("FROMDATE"));
                    e.setEndDate(toBuild.getString("UNTILDATE"));
                    e.setStartHour(toBuild.getString("FROMHOUR"));
                    e.setEndHour(toBuild.getString("UNTILHOUR"));
                    Location l = new Location("");
                    String latitude = toBuild.getString("LATITUDE");
                    String longitude = toBuild.getString("LONGITUDE");
                    if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
                        l.setLatitude(Location.convert(latitude));
                        l.setLongitude(Location.convert(longitude));
                        e.setLocation(l);
                    }
                    result.add(e);
                }
            }

        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

    /**
     * Build a single Event object from a JSON String
     *
     * @param json is the JSON string
     * @return an Event object
     */
    @Nullable
    public static Event parseSingleEvent(String json) {
        try {
            JSONObject toBuild = new JSONObject(json);
            int id = Integer.parseInt(toBuild.getString("EVENTO_ID"));
            Event e = new Event(id);
            e.setJson(toBuild.toString());
            e.setType(toBuild.getInt("TYPE"));
            e.setName(toBuild.getString("NAME"));
            e.setDescription(toBuild.getString("DESCRIPTION"));
            e.setPlace(toBuild.getString("PLACE"));
            ContactCard c = new ContactCard();
            c.setAddress(toBuild.getString("ADDRESS"));
            c.setEmail(toBuild.getString("EMAIL"));
            c.setTelephone(toBuild.getString("TELEPHONENUMBER"));
            c.setUrl(toBuild.getString("URL"));
            e.setContact(c);
            e.setImage(toBuild.getString("IMAGE"));
            e.setTagsFromCSV(toBuild.getString("TAGS"));
            e.setStartDate(toBuild.getString("FROMDATE"));
            e.setEndDate(toBuild.getString("UNTILDATE"));
            e.setStartHour(toBuild.getString("FROMHOUR"));
            e.setEndHour(toBuild.getString("UNTILHOUR"));
            Location l = new Location("");
            String latitude = toBuild.getString("LATITUDE");
            String longitude = toBuild.getString("LONGITUDE");
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
                l.setLatitude(Location.convert(latitude));
                l.setLongitude(Location.convert(longitude));
                e.setLocation(l);
            }
            return e;
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;
    }

    /**
     * Build an ArrayList containing News objects from a string in JSON format
     *
     * @param json is the JSON string
     * @return the list of News objects
     */
    @NonNull
    public static ArrayList<Cardable> parseNewsFromJSON(String json) {
        ArrayList<Cardable> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("News");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int id = Integer.parseInt(toBuild.getString("NEWS_ID"));
                    News n = new News(id);
                    n.setJson(toBuild.toString());
                    n.setTitle(toBuild.getString("NAME"));
                    n.setType(toBuild.getInt("TYPE"));
                    n.setBody(toBuild.getString("DESCRIPTION"));
                    n.setDate(toBuild.getString("DATE"));
                    n.setImage(toBuild.getString("IMAGE"));
                    n.setUrl(toBuild.getString("URL"));
                    n.setTagsFromCSV(toBuild.getString("TAGS"));
                    result.add(n);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

    /**
     * Build a single News object from a JSON String
     *
     * @param json is the JSON string
     * @return an News object
     */
    @Nullable
    public static News parseSingleNews(String json) {
        try {
            JSONObject toBuild = new JSONObject(json);
            int id = Integer.parseInt(toBuild.getString("NEWS_ID"));
            News n = new News(id);
            n.setTitle(toBuild.getString("NAME"));
            n.setBody(toBuild.getString("DESCRIPTION"));
            n.setType(toBuild.getInt("TYPE"));
            n.setDate(toBuild.getString("DATE"));
            n.setImage(toBuild.getString("IMAGE"));
            n.setUrl(toBuild.getString("URL"));
            n.setTagsFromCSV(toBuild.optString("TAGS", ""));
            return n;
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;
    }

    @NonNull
    public static ArrayList<Cardable> parseCitiesFromJSON(String json) {
        ArrayList<Cardable> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Comune");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int id = Integer.parseInt(toBuild.getString("CITY_ID"));
                    City c = new City(id);
                    c.setUserID(toBuild.getInt("cf_user_id"));
                    c.setName(toBuild.getString("NAME"));
                    c.setDescription(toBuild.getString("DESCRIPTION"));
                    c.setCap(toBuild.getString("CAP"));
                    c.setProvince(toBuild.getString("PROVINCIA"));
                    c.setRegion(toBuild.getString("REGIONE"));
                    c.setInfo(toBuild.getString("INFO"));
                    c.setAvatar(toBuild.getString("LOGO"));
                    for (int j = 1; j <= 10; j++) {
                        String im = toBuild.optString("IMAGE" + Integer.valueOf(j).toString());
                        c.addPictureFromString(im);
                    }
                    ContactCard con = new ContactCard();
                    con.setAddress(toBuild.getString("ADDRESS"));
                    con.setTelephone(toBuild.getString("TELEPHONENUMBER"));
                    con.setEmail(toBuild.getString("EMAIL"));
                    con.setUrl(toBuild.getString("URL"));
                    con.setFbLink(toBuild.getString("FACEBOOK"));
                    con.setYtLink(toBuild.getString("YOUTUBE"));
                    c.setContact(con);
                    Location l = new Location("");
                    String latitude = toBuild.getString("LATITUDE");
                    String longitude = toBuild.getString("LONGITUDE");
                    if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
                        l.setLatitude(Location.convert(latitude));
                        l.setLongitude(Location.convert(longitude));
                        c.setLocation(l);
                    }
                    c.setJson(toBuild.toString());
                    result.add(c);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }


    @Nullable
    public static City parseSingleCity(String json) {
        JSONObject toBuild;
        try {
            toBuild = new JSONObject(json);

            int id = Integer.parseInt(toBuild.getString("CITY_ID"));
            City c = new City(id);
            c.setUserID(toBuild.getInt("cf_user_id"));
            c.setName(toBuild.getString("NAME"));
            c.setDescription(toBuild.getString("DESCRIPTION"));
            c.setCap(toBuild.getString("CAP"));
            c.setProvince(toBuild.getString("PROVINCIA"));
            c.setRegion(toBuild.getString("REGIONE"));
            c.setInfo(toBuild.getString("INFO"));
            c.setAvatar(toBuild.getString("LOGO"));
            for (int j = 1; j <= 10; j++) {
                String im = toBuild.optString("IMAGE" + Integer.valueOf(j).toString());
                c.addPictureFromString(im);
            }
            ContactCard con = new ContactCard();
            con.setAddress(toBuild.getString("ADDRESS"));
            con.setTelephone(toBuild.getString("TELEPHONENUMBER"));
            con.setEmail(toBuild.getString("EMAIL"));
            con.setUrl(toBuild.getString("URL"));
            con.setFbLink(toBuild.getString("FACEBOOK"));
            con.setYtLink(toBuild.getString("YOUTUBE"));
            c.setContact(con);
            Location l = new Location("");
            String latitude = toBuild.getString("LATITUDE");
            String longitude = toBuild.getString("LONGITUDE");
            if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
                l.setLatitude(Location.convert(latitude));
                l.setLongitude(Location.convert(longitude));
                c.setLocation(l);
            }
            c.setJson(toBuild.toString());
            return c;
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;

    }

    @NonNull
    public static ArrayList<Cardable> parseRoutesFromJSON(String json) {
        ArrayList<Cardable> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Percorsi");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int id = Integer.parseInt(toBuild.getString("PERCORSO_ID"));
                    Route r = new Route(id);
                    r.setName(toBuild.getString("NAME"));
                    r.setTagsFromCSV(toBuild.getString("TAGS"));
                    r.setLength(Float.parseFloat(toBuild.getString("DISTANZA")));
                    r.setDuration(toBuild.getString("TEMPO"));
                    r.setLevel(toBuild.getInt("DIFFICOLTA"));
                    r.setImage(toBuild.getString("IMAGE"));
                    r.setKml(toBuild.getString("KML"));
                    r.setDescription(toBuild.getString("DESCRIPTION"));
                    r.setJson(toBuild.toString());
                    result.add(r);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

    @Nullable
    public static Route parseSingleRoute(String json) {
        JSONObject toBuild;
        try {
            toBuild = new JSONObject(json);
            int id = Integer.parseInt(toBuild.getString("PERCORSO_ID"));
            Route r = new Route(id);
            r.setName(toBuild.getString("NAME"));
            r.setTagsFromCSV(toBuild.getString("TAGS"));
            r.setLength(Float.parseFloat(toBuild.getString("DISTANZA")));
            r.setDuration(toBuild.getString("TEMPO"));
            r.setLevel(toBuild.getInt("DIFFICOLTA"));
            r.setImage(toBuild.getString("IMAGE"));
            r.setKml(toBuild.getString("KML"));
            r.setDescription(toBuild.getString("DESCRIPTION"));
            r.setJson(toBuild.toString());
            return r;

        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;
    }

    @NonNull
    public static ArrayList<SearchResult> parseSearchResultsFromJSON(String json) {
        ArrayList<SearchResult> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Trovati");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    SearchResult sr = new SearchResult();
                    sr.setId(toBuild.getInt("LUOGO_ID"));
                    sr.setType(toBuild.getInt("TYPE"));
                    sr.setTagsFromCSV(toBuild.getString("TAGS"));
                    sr.setDescription(toBuild.getString("DESCRIPTION"));
                    sr.setheader(toBuild.getString("NAME"));
                    result.add(sr);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

    @Nullable
    public static String parseSingleResult(String json) {
        String result = null;
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Trovato");
            if (data != null) {
                JSONObject toBuild = data.getJSONObject(0);
                result = toBuild.toString();
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

    @NonNull
    public static ArrayList<City> parseCustomersFromJSON(String json) {
        ArrayList<City> result = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(json);
            JSONArray data = reader.getJSONArray("Utenti");
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    JSONObject toBuild = data.getJSONObject(i);
                    int id = toBuild.getInt("CITY_ID");
                    City c = new City(id);
                    c.setUserID(toBuild.getInt("cf_user_id"));
                    c.setName(toBuild.getString("name"));
                    c.setAvatar(toBuild.getString("LOGO"));
                    result.add(c);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return result;
    }

}
