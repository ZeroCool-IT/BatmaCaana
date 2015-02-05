/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/News.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing city's news
 *
 * @author Marco Battisti
 */
public class News implements Cardable {

    protected int id;
    protected int type;
    protected String json;
    protected String title;
    protected String body;
    protected GregorianCalendar date;
    protected String image;
    protected String url;
    protected LinkedList<String> tags;


    /**
     * Public constructor
     */
    public News(int id) {
        this.id = id;
        tags = new LinkedList<String>();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the title of the news
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the news to set
     */
    public void setTitle(String title) {
        if (!title.equals(Constraints.EMPTY_VALUE)) {
            this.title = title;
        } else
            this.title = null;
    }

    /**
     * @return the body of the news
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        if (!body.equals(Constraints.EMPTY_VALUE)) {
            this.body = body;
        } else
            this.body = null;
    }

    /**
     * @return the date of the news
     */
    public GregorianCalendar getDate() {
        return date;
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

    /**
     * @param date the date of the news to set
     */
    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

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
        if (csv != null && !csv.equals(Constraints.EMPTY_VALUE)) {
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
    public String getImage() {
        return image;
    }

    /**
     * @param image the image of the news to set
     */
    public void setImage(String image) {
        if (!image.equals(Constraints.EMPTY_VALUE)) {
            this.image = image;
        } else
            this.image = null;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url of the news to set
     */
    public void setUrl(String url) {
        if (!url.equals(Constraints.EMPTY_VALUE)) {
            String toSet = url.replace("\\/", "/");
            this.url = toSet;
        } else
            this.url = null;
    }

    /**
     * Redefine equals: 2 news are equals if their ids are equals
     */
    public boolean equals(Object o) {
        if (o != null && o.getClass() == News.class) {
            News n = (News) o;
            if (n.getId() == this.getId())
                return true;
            else
                return false;
        }
        return false;
    }

    public String getItemURI() {
        return Constraints.PLACE_URI + Integer.valueOf(getId()).toString() + "&" + Integer.valueOf(getType()).toString();
    }

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Override
    public String getHeader() {
        return getTitle();
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
            dateFormat = new SimpleDateFormat("dd\nMMM", l);
        } else {
            dateFormat = new SimpleDateFormat("MMM\ndd", l);
        }
        String result = dateFormat.format(getDate().getTime());
        return result;
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
