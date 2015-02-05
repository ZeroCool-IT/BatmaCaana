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

}
