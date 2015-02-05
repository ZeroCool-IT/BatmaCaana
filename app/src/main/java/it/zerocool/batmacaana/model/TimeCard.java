/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

/**
 * Project: Pandora
 * File: it.zerocool.batmacaana.model/TimeCard.java
 * @author Marco Battisti
 */
package it.zerocool.batmacaana.model;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.R;
import it.zerocool.batmacaana.utilities.ApplicationContextProvider;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;

/**
 * Public class representing opening time and days for a Place
 *
 * @author Marco Battisti
 */
public class TimeCard {

    protected GregorianCalendar amOpening;
    protected GregorianCalendar amClosing;
    protected GregorianCalendar pmOpening;
    protected GregorianCalendar pmClosing;
    protected String notes;
    protected ArrayList<GregorianCalendar> closingDays;


    /**
     * Public constructor
     */
    public TimeCard() {
        this.closingDays = new ArrayList<GregorianCalendar>();
    }


    /**
     * @return the AM opening hour
     */
    public GregorianCalendar getAmOpening() {
        return amOpening;
    }

    /**
     * Set the place AM opening from String
     *
     * @param amOpening the AM opening hour to set
     */
    public void setAmOpening(String amOpening) {
        GregorianCalendar g = ParsingUtilities.parseHour(amOpening);
        setAmOpening(g);
    }

    /**
     * Set the place AM opening
     *
     * @param amOpening the AM opening hour to set
     */
    public void setAmOpening(GregorianCalendar amOpening) {
        this.amOpening = amOpening;
    }

    /**
     * @return the AM closing hour
     */
    public GregorianCalendar getAmClosing() {
        return amClosing;
    }

    /**
     * Set the place AM closing from String
     *
     * @param amClosing the AM opening hour to set
     */
    public void setAmClosing(String amClosing) {
        GregorianCalendar g = ParsingUtilities.parseHour(amClosing);
        setAmClosing(g);
    }

    /**
     * Set the place AM closing
     *
     * @param amClosing AM closing hour to set
     */
    public void setAmClosing(GregorianCalendar amClosing) {
        this.amClosing = amClosing;
    }

    /**
     * @return the PM opening hour
     */
    public GregorianCalendar getPmOpening() {
        return pmOpening;
    }

    /**
     * Set the place PM opening from String
     *
     * @param pmOpening the PM opening hour to set
     */
    public void setPmOpening(String pmOpening) {
        GregorianCalendar g = ParsingUtilities.parseHour(pmOpening);
        setPmOpening(g);
    }

    /**
     * Set the place PM opening
     *
     * @param pmOpening the PM opening hour to set
     */
    public void setPmOpening(GregorianCalendar pmOpening) {
        this.pmOpening = pmOpening;
    }

    /**
     * @return the PM closing hour
     */
    public GregorianCalendar getPmClosing() {
        return pmClosing;
    }

    /**
     * Set the place PM closing from String
     *
     * @param pmClosing the PM opening hour to set
     */
    public void setPmClosing(String pmClosing) {
        GregorianCalendar g = ParsingUtilities.parseHour(pmClosing);
        setPmClosing(g);
    }

    /**
     * Set the place PM closing
     *
     * @param pmClosing the PM closing hour to set
     */
    public void setPmClosing(GregorianCalendar pmClosing) {
        this.pmClosing = pmClosing;
    }

    /**
     * @return the closing days list
     */
    public ArrayList<GregorianCalendar> getClosingDays() {
        return closingDays;
    }


    /**
     * @param closingDays the closing days list to set
     */
    public void setClosingDays(ArrayList<GregorianCalendar> closingDays) {
        this.closingDays = closingDays;
    }

    /**
     * Add the closing days to list from a string in CSV format
     *
     * @param csv is the string in CSV format
     */
    public void setClosingDaysFromCSV(String csv) {
        if (csv != null && !csv.equals(Constraints.EMPTY_VALUE)) {
            StringTokenizer tokenizer = new StringTokenizer(csv, ",");
            while (tokenizer.hasMoreTokens()) {
                String toAdd = tokenizer.nextToken();
                toAdd = toAdd.trim();
                GregorianCalendar g = new GregorianCalendar();
                int day = Integer.parseInt(toAdd);
                switch (day) {
                    case Constraints.MONDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.MONDAY);
                        break;
                    case Constraints.TUESDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.TUESDAY);
                        break;
                    case Constraints.WEDNESDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.WEDNESDAY);
                        break;
                    case Constraints.THURSDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.THURSDAY);
                        break;
                    case Constraints.FRIDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.FRIDAY);
                        break;
                    case Constraints.SATURDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SATURDAY);
                        break;
                    case Constraints.SUNDAY:
                        g.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
                        break;
                    default:
                        break;
                }
                getClosingDays().add(g);
            }
        }
    }


    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }


    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }


    /**
     * @return a String representing AM opening hours
     */
    public String openAMtoString() {
        String amOp, amCl, res = null;
        if (getAmClosing() != null && getAmOpening() != null) {
//            DecimalFormat format = new DecimalFormat("00");
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//            amOp = format.format(getAmOpening().get(GregorianCalendar.HOUR_OF_DAY)) + ":00";
//            amCl = format.format(getAmClosing().get(GregorianCalendar.HOUR_OF_DAY)) + ":00";
            amOp = dateFormat.format(getAmOpening().getTime());
            amCl = dateFormat.format(getAmClosing().getTime());

            res = "AM: " + amOp + " - " + amCl;
        }
        return res;
    }

    /**
     * @return a String representing PM opening hours
     */
    public String openPMtoString() {
        String pmOp, pmCl, res = null;
        if (getPmClosing() != null && getPmOpening() != null) {
//            DecimalFormat format = new DecimalFormat("00");
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//            pmOp = format.format(getPmOpening().get(GregorianCalendar.HOUR_OF_DAY)) + ":00";
//            pmCl = format.format(getPmClosing().get(GregorianCalendar.HOUR_OF_DAY)) + ":00";
            pmOp = dateFormat.format(getPmOpening().getTime());
            pmCl = dateFormat.format(getPmClosing().getTime());
            res = "PM: " + pmOp + " - " + pmCl;
        }
        return res;
    }

    /**
     * @return a String representing place closing days
     */
    public String closingDayToString() {
        String res = null;
        if (!getClosingDays().isEmpty()) {
            Iterator<GregorianCalendar> it = getClosingDays().iterator();
            List<String> result = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            while (it.hasNext()) {
                result.add(dateFormat.format(it.next().getTime()));
            }
            res = ApplicationContextProvider.getContext().getResources().getString(R.string.closed) + " " + TextUtils.join(", ", result);
        }
        return res;

    }

    /**
     * @return a string representing the time card of the place
     */
    public String toString() {
        if (openAMtoString() != null || openPMtoString() != null || getNotes() != null || !getClosingDays().isEmpty()) {
            String res = "";
            if (openAMtoString() != null) {
                res += openAMtoString() + "\n";
            }
            if (openPMtoString() != null) {
                res += openPMtoString() + "\n";
            }
            if (closingDayToString() != null) {
                res += closingDayToString() + "\n";
            }
            if (getNotes() != null) {
                res += getNotes();
            }
            if (res != Constraints.EMPTY_VALUE) {
                return res;
            }
        }
        return null;

    }
}



