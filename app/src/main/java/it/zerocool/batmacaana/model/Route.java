/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

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

    private final int id;
    @NonNull
    private final LinkedList<String> tags;
    private final int type;
    @Nullable
    private String name;
    private float length;
    @Nullable
    private String duration;
    private int level;
    @Nullable
    private String image;
    @Nullable
    private String kml;
    @Nullable
    private String description;
    @Nullable
    private String json;

    public Route(int id) {
        this.id = id;
        this.type = Constant.TYPE_ROUTE;
        tags = new LinkedList<>();
    }

    int getId() {
        return id;
    }

// --Commented out by Inspection START (05/03/2015 16:37):
//    public void setId(int id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:37)

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            this.name = null;
        }
    }

    /**
     * @return the tags list
     */
    @NonNull
    public LinkedList<String> getTags() {
        return tags;
    }

// --Commented out by Inspection START (05/03/2015 16:37):
//    /**
//     * @param tags the tags list to set
//     */
//    public void setTags(LinkedList<String> tags) {
//        this.tags = tags;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:37)

    /**
     * Add the tags to tags' list from a string in CSV format
     *
     * @param csv is the string in CSV format
     */
    public void setTagsFromCSV(@Nullable String csv) {
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

    @Nullable
    public String getDuration() {
        return duration;
    }

    public void setDuration(@Nullable String duration) {
        if (duration != null && !duration.isEmpty()) {
            this.duration = duration;
        } else {
            this.duration = null;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        if (image != null && !image.isEmpty()) {
            this.image = image;
        } else {
            this.image = null;
        }
    }

    @Nullable
    public String getKml() {
        return kml;
    }

    public void setKml(@Nullable String kml) {
        if (kml != null && !kml.isEmpty()) {
            this.kml = kml;
        } else {
            this.kml = null;
        }
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else {
            this.description = null;
        }
    }

    @NonNull
    public String getItemURI() {
        return Constant.SHARE_URI + Integer.valueOf(getId()).toString() + "&" + Integer.valueOf(getType()).toString();
    }

    public String getDistanceToString() {
        return getAccentInfo();
    }

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Nullable
    @Override
    public String getHeader() {
        return getName();
    }

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    @Nullable
    @Override
    public String getImagery() {
        return getImage();
    }

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @NonNull
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
    @Nullable
    @Override
    public String getJson() {
        return json;
    }

    public void setJson(@Nullable String json) {
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
