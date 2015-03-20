/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import android.location.Location;

import java.util.ArrayList;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Model class representing nearby cities
 * Created by Marco Battisti on 20/02/2015.
 */
public class City implements Cardable {

    private final int id;
    private final ArrayList<String> pictures;
    private String name;
    private String description;
    private ContactCard contact;
    private Location location;
    private String json;
    private int userID;
    private String province;
    private String region;
    private String cap;
    private String info;
    private String avatar;


    /**
     * Public constructor
     *
     * @param id is City id
     */
    public City(int id) {
        this.id = id;
        this.pictures = new ArrayList<>();
    }

    /**
     * @return an ArrayList containing City's pictures
     */
    public ArrayList<String> getPictures() {
        return pictures;
    }

    /**
     * Add a picture to pictures' array from a String
     *
     * @param picture is the picture to add
     */
    public void addPictureFromString(String picture) {
        if (picture != null && !picture.isEmpty()) {
            getPictures().add(picture);
        }
    }

    /**
     * @return the ID of the City
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name of the City
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the city
     *
     * @param name is the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description of the City
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the City
     *
     * @param description is the description to set
     */
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else
            this.description = null;
    }

    /**
     * @return the Contact Card of the city
     */
    public ContactCard getContact() {
        return contact;
    }

    /**
     * Set the Contact Card of the City
     *
     * @param contact is the ContactCard to set
     */
    public void setContact(ContactCard contact) {
        this.contact = contact;
    }

    /**
     * @return the Location of the City
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location of the City
     *
     * @param location is the Location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Override
    public String getHeader() {
        return name;
    }

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    @Override
    public String getImagery() {
        return getPictures().get(0);
    }

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @Override
    public String getSubheader() {
        return null;
    }

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Override
    public String getAccentInfo() {
        return null;
    }

    @Override
    public String getJson() {
        return json;
    }

    /**
     * Set the JSON representation of the City
     *
     * @param json is the JSON to set
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
        return Constant.TYPE_CITY;
    }

    /**
     * @return the Explora User ID of the City
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Set the Explora user ID of the City
     *
     * @param userID is the User ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the province of the City
     */
    public String getProvince() {
        return province;
    }

    /**
     * Set the province of the City
     *
     * @param province is the province to set
     */
    public void setProvince(String province) {
        if (province != null && !province.isEmpty()) {
            this.province = province;
        } else {
            this.province = null;
        }
    }

    /**
     * @return the Region of the City
     */
    public String getRegion() {
        return region;
    }

    /**
     * Set the Region of the City
     *
     * @param region is the Region to set
     */
    public void setRegion(String region) {
        if (region != null && !region.isEmpty()) {
            this.region = region;
        } else {
            this.region = null;
        }
    }

    /**
     * @return th CAP (ZIP Code) of the City
     */
    public String getCap() {
        return cap;
    }

    /**
     * Set the CAP (ZIP Code) of the City
     *
     * @param cap id the CAP to set
     */
    public void setCap(String cap) {
        if (cap != null && !cap.isEmpty()) {
            this.cap = cap;
        } else {
            this.cap = null;
        }
    }

    /**
     * @return the Geographic Info of the City
     */
    public String getInfo() {
        return info;
    }

    /**
     * Set the Geographic Info of the City
     *
     * @param info is the info to set
     */
    public void setInfo(String info) {
        if (info != null && !info.isEmpty()) {
            this.info = info;
        } else {
            this.info = null;
        }
    }

    /**
     * @return the avatar representing the City
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set the avatar of the City
     *
     * @param avatar is the avatar to set
     */
    public void setAvatar(String avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            this.avatar = avatar;
        } else {
            this.avatar = null;
        }
    }

    /**
     * Compares this instance with the specified object and indicates if they
     * are equal. In order to be equal, {@code o} must represent the same object
     * as this instance using a class-specific comparison. The general contract
     * is that this comparison should be reflexive, symmetric, and transitive.
     * Also, no object reference other than null is equal to null.
     * <p/>
     * <p>The default implementation returns {@code true} only if {@code this ==
     * o}. See <a href="{@docRoot}reference/java/lang/Object.html#writing_equals">Writing a correct
     * {@code equals} method</a>
     * if you intend implementing your own {@code equals} method.
     * <p/>
     * <p>The general contract for the {@code equals} and {@link
     * #hashCode()} methods is that if {@code equals} returns {@code true} for
     * any two objects, then {@code hashCode()} must return the same value for
     * these objects. This means that subclasses of {@code Object} usually
     * override either both methods or neither of them.
     *
     * @param o the object to compare this instance with.
     * @return {@code true} if the specified object is equal to this {@code
     * Object}; {@code false} otherwise.
     * @see #hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == City.class) {
            City c = (City) o;
            return this.getId() == c.getId();
        }
        return false;
    }
}
