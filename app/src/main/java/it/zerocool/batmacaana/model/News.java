/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/News.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constant;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing city's news
 *
 * @author Marco Battisti
 */
public class News implements Cardable {

    private final int id;
    @NonNull
    private final LinkedList<String> tags;
    private int type;
    private String json;
    @Nullable
    private String title;
    @Nullable
    private String body;
    private GregorianCalendar date;
    @Nullable
    private String image;
    @Nullable
    private String url;


    /**
     * Public constructor
     */
    public News(int id) {
        this.id = id;
        tags = new LinkedList<>();
    }

    /**
     * @return the id
     */
    int getId() {
        return id;
    }

// --Commented out by Inspection START (05/03/2015 16:35):
//    /**
//     * @param id the id to set
//     */
//    public void setId(int id) {
//        this.id = id;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:35)

    /**
     * @return the title of the news
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the news to set
     */
    public void setTitle(@NonNull String title) {
        if (!title.equals(Constant.EMPTY_VALUE)) {
            this.title = title;
        } else
            this.title = null;
    }

    /**
     * @return the body of the news
     */
    @Nullable
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(@NonNull String body) {
        if (!body.equals(Constant.EMPTY_VALUE)) {
            this.body = body;
        } else
            this.body = null;
    }

    /**
     * @return the date of the news
     */
    GregorianCalendar getDate() {
        return date;
    }

    /**
     * @param date the date of the news to set
     */
    void setDate(GregorianCalendar date) {
        this.date = date;
    }

    /**
     * Set the news date parsing infos from String
     *
     * @param date it's the start date to set (YYYY-mm-DD format)
     */
    public void setDate(String date) {
        GregorianCalendar g = ParsingUtilities.parseDate(date);
        setDate(g);

    }

    @Nullable
    public String getDateToString() {
        if (getDate() != null) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
            return dateFormat.format(getDate().getTime());
        } else
            return null;
    }

    /**
     * @return the tags list
     */
    @NonNull
    public LinkedList<String> getTags() {
        return tags;
    }

// --Commented out by Inspection START (05/03/2015 16:35):
//    /**
//     * @param tags the tags list to set
//     */
//    public void setTags(LinkedList<String> tags) {
//        this.tags = tags;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:35)

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

    /**
     * @return the image
     */
    @Nullable
    public String getImage() {
        return image;
    }

    /**
     * @param image the image of the news to set
     */
    public void setImage(@NonNull String image) {
        if (!image.equals(Constant.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }

    /**
     * @return the url
     */
    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url of the news to set
     */
    public void setUrl(@NonNull String url) {
        if (!url.equals(Constant.EMPTY_VALUE)) {
            this.url = url.replace("\\/", "/");
        } else
            this.url = null;
    }

    /**
     * Redefine equals: 2 news are equals if their ids are equals
     */
    public boolean equals(@Nullable Object o) {
        if (o != null && o.getClass() == News.class) {
            News n = (News) o;
            return n.getId() == this.getId();
        }
        return false;
    }

    @NonNull
    public String getItemURI() {
        return Constant.SHARE_URI + Integer.valueOf(getId()).toString() + "&" + Integer.valueOf(getType()).toString();
    }

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Nullable
    @Override
    public String getHeader() {
        return getTitle();
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
        Locale l = Locale.getDefault();
        SimpleDateFormat dateFormat;
        if (l.equals(Locale.ITALY)) {
            dateFormat = new SimpleDateFormat("dd\nMMM", l);
        } else {
            dateFormat = new SimpleDateFormat("MMM\ndd", l);
        }
        return dateFormat.format(getDate().getTime());
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
     * Set a JSON String representing the object
     *
     * @param json is the JSON String to set
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
     * Set an Integer representing the type of the object
     *
     * @param type is the type to set
     */
    public void setType(int type) {
        this.type = type;
    }


}
