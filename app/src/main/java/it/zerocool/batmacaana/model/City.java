/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Model class representing nearby cities
 * Created by Marco Battisti on 20/02/2015.
 */
public class City extends Place {

    private String link;

    /**
     * Public constructor
     *
     * @param id is City id
     */
    public City(int id) {
        super(id);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

}
