/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana.model;

import android.location.Location;

import java.util.ArrayList;

/**
 * Class representing app main cities
 * Created by Marco Battisti on 27/02/2015.
 */
public class MainCity {

    private int id;
    private int userID;
    private String name;
    private ContactCard contactCard;
    private String history;
    private String infos;
    private ArrayList<String> images;
    private Location location;
    private String json;


    public MainCity(int id, int userID) {
        this.id = id;
        this.userID = userID;
        this.images = new ArrayList<>();
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

    public int getUserID() {
        return userID;
    }


    public ContactCard getContactCard() {
        return contactCard;
    }

    public void setContactCard(ContactCard contactCard) {
        this.contactCard = contactCard;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        if (history != null && !history.isEmpty()) {
            this.history = history;
        } else {
            history = null;
        }
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        if (infos != null && !infos.isEmpty()) {
            this.infos = infos;
        } else {
            this.infos = null;
        }
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
