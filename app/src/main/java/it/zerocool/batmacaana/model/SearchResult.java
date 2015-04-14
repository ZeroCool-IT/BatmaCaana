/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Model class representing search results
 * Created by Marco on 27/01/2015.
 */
public class SearchResult {

    @NonNull
    private final List<String> tags;
    private int id;
    @Nullable
    private String header;
    private int type;
    @Nullable
    private String description;

    /**
     * Public constructor
     */
    public SearchResult() {
        tags = new LinkedList<>();
    }

    /**
     * Get the ID of the object result
     *
     * @return the ID of the object
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of th object result
     *
     * @param id is the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the header of the object result
     */
    @Nullable
    public String getheader() {
        return header;
    }

    /**
     * Set the header of the object result
     *
     * @param header is the header to set
     */
    public void setheader(@NonNull String header) {
        if (!header.equals(Constant.EMPTY_VALUE))
            this.header = header;
        else
            this.header = null;
    }

    /**
     * @return the type of the object result
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of the object result
     *
     * @param type is the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the tags list of the object result
     */
    @NonNull
    public List<String> getTags() {
        return tags;
    }

// --Commented out by Inspection START (05/03/2015 16:38):
//    /**
//     * Set the tags list of the object result
//     *
//     * @param tags is the list to set
//     */
//    public void setTags(List<String> tags) {
//        this.tags = tags;
//    }
// --Commented out by Inspection STOP (05/03/2015 16:38)

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
     * @return the description of the object result
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the object result
     *
     * @param description is the descriptio to set
     */
    public void setDescription(@NonNull String description) {
        if (!description.equals(Constant.EMPTY_VALUE)) {
            this.description = description;
        } else {
            this.description = null;
        }
    }
}
