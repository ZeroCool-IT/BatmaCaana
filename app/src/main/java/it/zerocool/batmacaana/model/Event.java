/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/Event.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing city's events
 *
 * @author Marco Battisti
 */
public class Event implements Cardable {

    private final int id;
    private final LinkedList<String> tags;
    private String name;
    private int type;
    private String json;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private GregorianCalendar startHour;
    private GregorianCalendar endHour;
    private String place;
    private String image;
    private String description;
    private Location location;
    private ContactCard contact;


    /**
     * Public constructor
     *
     * @param id is the unique id for the event
     */
    public Event(int id) {
        this.id = id;
        this.tags = new LinkedList<>();
    }

    /**
     * @return the id of the event
     */
    int getId() {
        return id;
    }

// --Commented out by Inspection START (05/03/2015 16:33):
//    /**
//     * @param id the id to set
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:33)

    /**
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the event to set
     */
    public void setName(String name) {
        if (!name.equals(Constant.EMPTY_VALUE)) {
            this.name = name;
        } else
            this.name = null;
    }

    /**
     * @return the start date of the event
     */
    public GregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * @param date the start date of the event to set
     */
    void setStartDate(GregorianCalendar date) {
        this.startDate = date;
    }

    /**
     * Set the event's start date parsing infos from String
     *
     * @param date it's the start date to set (YYYY-mm-DD format)
     */
    public void setStartDate(String date) {
        GregorianCalendar g = ParsingUtilities.parseDate(date);
        setStartDate(g);

    }

    /**
     * @return the end date of the event
     */
    public GregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * @param endDate is the end date of the event to set
     */
    void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    /**
     * Set the event's end date parsing infos from String
     *
     * @param endDate it's the end date to set (YYYY-mm-DD format)
     */
    public void setEndDate(String endDate) {
        GregorianCalendar g = ParsingUtilities.parseDate(endDate);
        setEndDate(g);

    }

    /**
     * @return the start hour of the event
     */
    public GregorianCalendar getStartHour() {
        return startHour;
    }

    /**
     * @param startHour the start hour of the event to set
     */
    void setStartHour(GregorianCalendar startHour) {
        this.startHour = startHour;
    }

    /**
     * Set the event's start hour parsing infos from String
     *
     * @param startHour it's the end date to set (HH:mm 24H format)
     */
    public void setStartHour(String startHour) {
        GregorianCalendar g = ParsingUtilities.parseHour(startHour);
        setStartHour(g);
    }

    /**
     * @return the start hour of the event
     */
    public GregorianCalendar getEndHour() {
        return endHour;
    }

    /**
     * @param endHour the end hour of the event to set
     */
    void setEndHour(GregorianCalendar endHour) {
        this.endHour = endHour;
    }

    /**
     * Set the event's end hour parsing infos from String
     *
     * @param endHour it's the end date to set (HH:mm 24H format)
     */
    public void setEndHour(String endHour) {
        GregorianCalendar g = ParsingUtilities.parseHour(endHour);
        setEndHour(g);
    }

    /**
     * @return The name of the place of the event
     */
    public String getPlace() {
        return place;
    }

    /**
     * Set the name of the place of the event
     *
     * @param place is the place of the event to set
     */
    public void setPlace(String place) {
        if (!place.equals(Constant.EMPTY_VALUE)) {
            this.place = place;
        } else
            this.place = null;
    }

    /**
     * @return the image of the event
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image  of the event to set
     */
    public void setImage(String image) {
        if (!image.equals(Constant.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }

    /**
     * @return the tags list of the event
     */
    public LinkedList<String> getTags() {
        return tags;
    }

// --Commented out by Inspection START (05/03/2015 16:34):
//    /**
//     * @param tags the tags list of the event to set
//     */
//    public void setTags(LinkedList<String> tags) {
//        this.tags = tags;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:34)

    /**
     * Add the tags to tags' list from a string in CSV format
     * SO FICO
     *
     * @param csv is the string in CSV format
     */
    public void setTagsFromCSV(String csv) {
        if (csv != null) {
            StringTokenizer tokenizer = new StringTokenizer(csv, ",");
            while (tokenizer.hasMoreTokens()) {
                String toAdd = tokenizer.nextToken();
                toAdd = toAdd.trim();
                toAdd = toAdd.substring(0, 1).toUpperCase(Locale.ITALY) + toAdd.substring(1);
                if (!getTags().contains(toAdd)) {
                    getTags().add(toAdd);
                }
            }
        }
    }

    /**
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description of the event to set
     */
    public void setDescription(String description) {
        if (!description.equals(Constant.EMPTY_VALUE)) {
            this.description = description;
        } else
            this.description = null;
    }

    /**
     * @return the location of the event
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location of the event to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the contact card of the event
     */
    public ContactCard getContact() {
        return contact;
    }

    /**
     * @param contact the contact card of the event to set
     */
    public void setContact(ContactCard contact) {
        this.contact = contact;
    }

    /**
     * Redefine equals: 2 events are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == Event.class) {
            Event e = (Event) o;
            return e.getId() == this.getId();
        }
        return false;
    }

    public String getItemURI() {
        return Constant.SHARE_URI + Integer.valueOf(getId()).toString() + "&" + Integer.valueOf(getType()).toString();
    }

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Override
    public String getHeader() {
        return getName();
    }

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    @Override
    public String getImagery() {
        return getImage();
    }

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @Override
    public String getSubheader() {
        return TextUtils.join(", ", getTags());
    }

    /**
     * @return Event start date to String
     */
    String startDateToString() {
        if (startDate != null) {
            if (startHour != null) {
                GregorianCalendar res = getStartDate();
                res.set(GregorianCalendar.HOUR_OF_DAY, getStartHour().get(Calendar.HOUR_OF_DAY));
                res.set(GregorianCalendar.MINUTE, getStartHour().get(Calendar.MINUTE));
                java.text.DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.SHORT);
                return dateFormat.format(res.getTime());
            } else {
                java.text.DateFormat dateFormat = SimpleDateFormat.getDateInstance(java.text.DateFormat.LONG);
                return dateFormat.format(getStartDate().getTime());
            }
        }
        return null;
    }

    /**
     * @return Event end date to String
     */
    String endDateToString() {
        if (endDate != null) {
            if (endHour != null) {
                GregorianCalendar res = getEndDate();
                res.set(GregorianCalendar.HOUR_OF_DAY, getEndHour().get(Calendar.HOUR_OF_DAY));
                res.set(GregorianCalendar.MINUTE, getEndHour().get(Calendar.MINUTE));
                java.text.DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.SHORT);
                return dateFormat.format(res.getTime());
            } else {
                java.text.DateFormat dateFormat = SimpleDateFormat.getDateInstance(java.text.DateFormat.LONG);
                return dateFormat.format(getEndDate().getTime());
            }
        }
        return null;
    }

    /**
     * Event time String representation
     *
     * @return a String with event start and end time
     */
    public String eventTimeToString() {
        Context context = ApplicationContextProvider.getContext();
        if (startDateToString() != null) {
            String res = startDateToString();
            if (endDateToString() != null) {
                res = context.getResources().getString(R.string.from) + " " + res;
                res += "\n" + context.getResources().getString(R.string.to) + " " +
                        endDateToString();
            }
            return res;
        }
        return null;
    }

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Override
    public String getAccentInfo() {
        Locale l = Locale.getDefault();
        SimpleDateFormat dateFormat;
        if (l.equals(Locale.ITALY)) {
            dateFormat = new SimpleDateFormat("dd\nMMM", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("MMM\ndd", Locale.getDefault());
        }
        return dateFormat.format(getStartDate().getTime());

    }

    /**
     * Get a JSON String representing the object
     *
     * @return a JSON String representing the object
     */
    @Override
    public String getJson() {
        return json;
    }

    /**
     * Set a string representing the object
     *
     * @param json is the json string to set
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * Get an Integer representing the type of the object
     *
     * @return an Integer representing the type of the object
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * Set an integer representing the type of th object
     *
     * @param type is the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}
