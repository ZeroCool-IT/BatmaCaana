/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/Cardable.java
 * @author Marco Battisti
 */

package it.zerocool.batmacaana.model;

import org.jetbrains.annotations.Nullable;

/**
 * Public interface representing all the items that can be represented with a CardView
 */
public interface Cardable {

    /**
     * Get the header title of the item
     *
     * @return a String representing card's header
     */
    @Nullable
    public String getHeader();

    /**
     * Get the imagery for the item
     *
     * @return a String representing the imagery for the card
     */
    @Nullable
    public String getImagery();

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @Nullable
    public String getSubheader();

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Nullable
    public String getAccentInfo();

    /**
     * Get a JSON String representing the object
     *
     * @return a JSON String representing the object
     */
    @Nullable
    public String getJson();

    /**
     * Get an Integer representing the type of the object
     *
     * @return an Integer representing the type of the object
     */
    public int getType();
}
