/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/ContactCard.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import it.zerocool.batmacaana.utilities.Constant;

/**
 * Class representing places' contact info
 *
 * @author Marco
 */
public class ContactCard {

    @Nullable
    private String address;
    @Nullable
    private String telephone;
    @Nullable
    private String email;
    @Nullable
    private String url;
    @Nullable
    private String fbLink;
    @Nullable
    private String gpLink;
    @Nullable
    private String taLink;
    @Nullable
    private String fsqrLink;
    private String ytLink;
    private String twttrLink;

    /**
     * Public constructor
     */
    public ContactCard() {
    }

    /**
     * @return the address
     */
    @Nullable
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(@NonNull String address) {
        if (!address.equals(Constant.EMPTY_VALUE)) {
            this.address = address;
        } else
            this.address = null;
    }

    /**
     * @return the telephone
     */
    @Nullable
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(@NonNull String telephone) {
        if (!telephone.equals(Constant.EMPTY_VALUE)) {
            this.telephone = telephone;
        } else
            this.telephone = null;
    }

    /**
     * @return the email
     */
    @Nullable
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(@NonNull String email) {
        if (!email.equals(Constant.EMPTY_VALUE)) {
            this.email = email;
        } else
            this.email = null;
    }

    /**
     * @return the url
     */
    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(@NonNull String url) {
        if (!url.equals(Constant.EMPTY_VALUE)) {
            this.url = url.replace("\\/", "/");
        } else
            this.url = null;
    }

    /**
     * @return the link to 4square
     */
    @Nullable
    public String getFsqrLink() {
        return fsqrLink;
    }

    /**
     * @param fsqrLink the 4square link to set
     */
    public void setFsqrLink(@NonNull String fsqrLink) {
        if (!fsqrLink.equals(Constant.EMPTY_VALUE)) {
            this.fsqrLink = fsqrLink;
        } else
            this.fsqrLink = null;
    }

    /**
     * @return the link to Facebook page
     */
    @Nullable
    public String getFbLink() {
        return fbLink;
    }

    /**
     * @param fbLink is the FB link to set
     */
    public void setFbLink(@NonNull String fbLink) {
        if (!fbLink.equals(Constant.EMPTY_VALUE)) {
            this.fbLink = fbLink;
        } else
            this.fbLink = null;
    }

    /**
     * @return the link Google Plus page
     */
    @Nullable
    public String getGpLink() {
        return gpLink;
    }

    /**
     * @param gpLink is the G+ link to set
     */
    public void setGpLink(@NonNull String gpLink) {
        if (!gpLink.equals(Constant.EMPTY_VALUE)) {
            this.gpLink = gpLink;
        } else
            this.gpLink = null;
    }

    /**
     * @return the TripAdvisor link
     */
    @Nullable
    public String getTaLink() {
        return taLink;
    }

    /**
     * @param taLink is the link to TripAdvisor page
     */
    public void setTaLink(@NonNull String taLink) {
        if (!taLink.equals(Constant.EMPTY_VALUE)) {
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
        return getFbLink() != null || getFsqrLink() != null || getTaLink() != null || getGpLink() != null || getTwttrLink() != null && getYtLink() != null;
    }

    /**
     * @return the Youtube Channel link
     */
    String getYtLink() {
        return ytLink;
    }

    /**
     * Set the Youtube Channel link
     *
     * @param ytLink is the link to set
     */
    public void setYtLink(String ytLink) {
        this.ytLink = ytLink;
    }

    /**
     * @return the Twitter Profile link
     */
    String getTwttrLink() {
        return twttrLink;
    }

    /**
     * Set the Twitter Profile link
     *
     * @param twttrLink is the link to set
     */
    public void setTwttrLink(String twttrLink) {
        this.twttrLink = twttrLink;
    }
}
