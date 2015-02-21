/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Model class representing routes
 * Created by Marco Battisti on 21/02/2015.
 */
public class Route implements Cardable {

    protected int id;
    protected String name;
    protected LinkedList<String> tags;
    protected float length;
    protected String duration;
    protected String level;
    protected String image;
    protected String kml;
    protected String description;
    protected int type;
    protected String json;

    public Route(int id) {
        this.id = id;
        this.type = Constant.TYPE_ROUTE;
        tags = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            this.name = null;
        }
    }

    /**
     * @return the tags list
     */
    public LinkedList<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags list to set
     */
    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }

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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        if (duration != null && !duration.isEmpty()) {
            this.duration = duration;
        } else {
            this.duration = null;
        }
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        if (level != null && !level.isEmpty()) {
            this.level = level;
        } else {
            this.level = null;
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image != null && !image.isEmpty()) {
            this.image = image;
        } else {
        }
    }

    public String getKml() {
        return kml;
    }

    public void setKml(String kml) {
        if (kml != null && !kml.isEmpty()) {
            this.kml = kml;
        } else {
            this.kml = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else {
            this.description = null;
        }
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
        float distance = getLength();
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

    public void setJson(String json) {
        if (json != null && !json.isEmpty()) {
            this.json = json;
        } else {
            this.json = null;
        }
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
}
