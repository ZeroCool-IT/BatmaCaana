/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/ContactCard.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import it.zerocool.batmacaana.utilities.Constraints;

/**
 * Class representing places' contact info
 *
 * @author Marco
 */
public class ContactCard {

    protected String address;
    protected String telephone;
    protected String email;
    protected String url;
    protected String fbLink;
    protected String gpLink;
    protected String taLink;
    protected String fsqrLink;

    /**
     * Public constructor
     */
    public ContactCard() {
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        if (!address.equals(Constraints.EMPTY_VALUE)) {
            this.address = address;
        } else
            this.address = null;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        if (!telephone.equals(Constraints.EMPTY_VALUE)) {
            this.telephone = telephone;
        } else
            this.telephone = null;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        if (!email.equals(Constraints.EMPTY_VALUE)) {
            this.email = email;
        } else
            this.email = null;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        if (!url.equals(Constraints.EMPTY_VALUE)) {
            String toSet = url.replace("\\/", "/");
            this.url = toSet;
        } else
            this.url = null;
    }

    /**
     * @return the link to 4square
     */
    public String getFsqrLink() {
        return fsqrLink;
    }

    /**
     * @param fsqrLink the 4square link to set
     */
    public void setFsqrLink(String fsqrLink) {
        if (!fsqrLink.equals(Constraints.EMPTY_VALUE)) {
            this.fsqrLink = fsqrLink;
        } else
            this.fsqrLink = null;
    }

    /**
     * @return the link to Facebook page
     */
    public String getFbLink() {
        return fbLink;
    }

    /**
     * @param fbLink is the FB link to set
     */
    public void setFbLink(String fbLink) {
        if (!fbLink.equals(Constraints.EMPTY_VALUE)) {
            this.fbLink = fbLink;
        } else
            this.fbLink = null;
    }

    /**
     * @return the link Google Plus page
     */
    public String getGpLink() {
        return gpLink;
    }

    /**
     * @param gpLink is the G+ link to set
     */
    public void setGpLink(String gpLink) {
        if (!gpLink.equals(Constraints.EMPTY_VALUE)) {
            this.gpLink = gpLink;
        } else
            this.gpLink = null;
    }

    /**
     * @return the TripAdvisor link
     */
    public String getTaLink() {
        return taLink;
    }

    /**
     * @param taLink is the link to TripAdvisor page
     */
    public void setTaLink(String taLink) {
        if (!taLink.equals(Constraints.EMPTY_VALUE)) {
            this.taLink = taLink;
        } else
            this.taLink = null;
    }

    /**
     * Check if the place has at least one social network link
     *
     * @return true if has at least one social network link, false otherwise
     */
    public boolean hasSocial() {
        return getFbLink() != null || getFsqrLink() != null || getTaLink() != null || getGpLink() != null;
    }

}
