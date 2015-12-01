package it.zerocool.batmacaana.model;

import org.jetbrains.annotations.Nullable;

/**
 * Object representing public transport
 * selection item. Every transport item represent a public transport choice
 * in the city.
 */
public class PublicTransport implements Cardable {

    private int imageId; //Image representing the transport
    private String name;    //The name of the transport

    public PublicTransport(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
        return null;
    }

    /**
     * Get the sub-header, if any
     *
     * @return a String representing card's sub-header
     */
    @Nullable
    @Override
    public String getSubheader() {
        return null;
    }

    /**
     * Get the accent info, if any
     *
     * @return a String representing the accent information of the card
     */
    @Nullable
    @Override
    public String getAccentInfo() {
        return null;
    }

    /**
     * Get a JSON String representing the object
     *
     * @return a JSON String representing the object
     */
    @Nullable
    @Override
    public String getJson() {
        return null;
    }

    /**
     * Get an Integer representing the type of the object
     *
     * @return an Integer representing the type of the object
     */
    @Override
    public int getType() {
        return 0;
    }
}
