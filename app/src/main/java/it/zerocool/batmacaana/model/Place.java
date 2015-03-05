/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File it.zerocool.batmacaana.model/Place.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.location.Location;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Class representing city's places
 *
 * @author Marco Battisti
 */
public class Place implements Cardable {

    private final int id;
    private final LinkedList<String> tags;
    private String name;
    private String image;
    private int type;
    private boolean favorite;
    private String description;
    private ContactCard contact;
    private TimeCard timeCard;
    private Location location;
    private float distanceFromCurrentPosition;
    private String json;
    private boolean accessible;


    /**
     * Public constructor
     */
    Place(int id) {
        this.id = id;
        this.tags = new LinkedList<>();
        favorite = false;
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


// --Commented out by Inspection START (05/03/2015 16:36):
//    /**
//     * @param id the id to set
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:36)

    /**
     * @return the name of the place
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name  of the place to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the image of the place
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image of the place to set
     */
    public void setImage(String image) {
        if (!image.equals(Constant.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }


    /**
     * @return true if the place is in the favorite list, false otherwise
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Change the state of the place, favorite or not
     *
     * @param favorite true if the place is favorite, false otherwise
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * @return the tags list
     */
    public LinkedList<String> getTags() {
        return tags;
    }

// --Commented out by Inspection START (05/03/2015 17:07):
//    /**
//     * @param tags the tags list to set
//     */
//    public void setTags(LinkedList<String> tags) {
//        this.tags = tags;
//    }
// --Commented out by Inspection STOP (05/03/2015 17:07)

    /**
     * Add the tags to tags' list from a string in CSV format
     *
     * @param csv is the string in CSV format
     */
    public void setTagsFromCSV(String csv) {
        if (csv != null && !csv.equals(Constant.EMPTY_VALUE)) {
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
     * @return the description of the place
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description of the place to set
     */
    public void setDescription(String description) {
        if (!description.equals(Constant.EMPTY_VALUE)) {
            this.description = description;
        } else
            this.description = null;
    }

    /**
     * @return the contact card of the place
     */
    public ContactCard getContact() {
        return contact;
    }

    /**
     * @param contact the contact card of the place to set
     */
    public void setContact(ContactCard contact) {
        this.contact = contact;
    }

    /**
     * @return the timeCard
     */
    public TimeCard getTimeCard() {
        return timeCard;
    }

    /**
     * @param timeCard the timeCard to set
     */
    public void setTimeCard(TimeCard timeCard) {
        this.timeCard = timeCard;
    }

    /**
     * @return the location of the place
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location is the location of the place to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return true if the place is accessible for disabled false otherwise
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     *
     */
    public void setAccessible(String accessible) {
        this.accessible = accessible != null && !accessible.isEmpty();
    }

// --Commented out by Inspection START (05/03/2015 16:36):
//    /**
//     * Set place accessibility
//     *
//     * @param accessible true if place is accessible for disabled, false otherwise
//     */
//    public void setAccessible(boolean accessible) {
//        this.accessible = accessible;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:36)

    /**
     * @return the distance in meters from the current position
     */
    public float getDistanceFromCurrentPosition() {
        return distanceFromCurrentPosition;
    }

    /**
     * Set the distance in meters from the current position
     *
     * @param distanceFromCurrentPosition is the distance to set
     */
    public void setDistanceFromCurrentPosition(float distanceFromCurrentPosition) {
        this.distanceFromCurrentPosition = distanceFromCurrentPosition;
    }

    /**
     * Redefine equals: 2 places are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == Place.class) {
            Place p = (Place) o;
            return p.getId() == this.getId();
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
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Override
    public String getAccentInfo() {
        String res;
        float distance = getDistanceFromCurrentPosition();
        if (distance > 1000) {
            distance /= 1000;
            DecimalFormat format = new DecimalFormat("###.#");
            res = format.format(distance);
            res += " Km";
        } else {
            DecimalFormat format = new DecimalFormat("###");
            res = format.format(distance);
            res += " m";
        }
        return res;

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
     * Set a JSON representation fo the place
     *
     * @param json is the JSON representing the object
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
     * Set an integer representing the type of the object
     *
     * @param type is the type to set
     */
    public void setType(int type) {
        this.type = type;
    }


}
