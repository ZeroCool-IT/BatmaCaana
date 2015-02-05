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
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.model.Cardable;
import it.zerocool.batmacaana.model.ContactCard;
import it.zerocool.batmacaana.model.Eat;
import it.zerocool.batmacaana.model.Event;
import it.zerocool.batmacaana.model.News;
import it.zerocool.batmacaana.model.Place;
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
    public static GregorianCalendar parseDate(String d) {
        if (d != null && !d.equals(Constraints.EMPTY_VALUE)) {
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
    public static GregorianCalendar parseHour(String h) {
        if (h != null && !h.equals(Constraints.EMPTY_VALUE)) {
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
                    Place p = null;
                    switch (type) {
                        case Constraints.TYPE_TOSEE:
                            p = new ToSee(id);
                            break;
                        case Constraints.TYPE_EAT:
                            p = new Eat(id);
                            break;
                        case Constraints.TYPE_SLEEP:
                            p = new Sleep(id);
                            break;
                        case Constraints.TYPE_SERVICE:
                            p = new Service(id);
                            break;
                        case Constraints.TYPE_SHOP:
                            p = new Shop(id);
                            break;
                        default:
                            break;
                    }
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
                    result.add(p);


                }
            }

        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("Coordinate conversion exception", e.getMessage());
        }
        return result;
    }

    /**
     * Build a single place object from json string
     *
     * @param json is the JSON string
     * @return the place object
     */
    public static Place parseSinglePlace(String json) {
        try {
            JSONObject toBuild = new JSONObject(json);
            int type = Integer.parseInt(toBuild.getString("TYPE"));
            int id = Integer.parseInt((toBuild.getString("LUOGO_ID")));
            Place p = null;
            switch (type) {
                case Constraints.TYPE_TOSEE:
                    p = new ToSee(id);
                    break;
                case Constraints.TYPE_EAT:
                    p = new Eat(id);
                    break;
                case Constraints.TYPE_SLEEP:
                    p = new Sleep(id);
                    break;
                case Constraints.TYPE_SERVICE:
                    p = new Service(id);
                    break;
                case Constraints.TYPE_SHOP:
                    p = new Shop(id);
                    break;
                default:
                    break;
            }
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
            return p;
        } catch (JSONException e) {
            Log.e("JSON Exception", e.getMessage());
        }
        return null;
    }

    /**
     * Build an ArrayList containing Event objects from a string in JSON format
     *
     * @param json is the JSON string
     * @return the list of Event objects
     */
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
                    //TODO Location
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
            //TODO Location
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
            e.printStackTrace();
        }
        return null;
    }

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

}
