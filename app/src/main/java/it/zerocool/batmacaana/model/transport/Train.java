package it.zerocool.batmacaana.model.transport;

import java.util.GregorianCalendar;

/**
 * This object represents trains
 */
public class Train {

    private final String departureStation;
    private final String finalDestination;
    private final GregorianCalendar departureTime;
    private final GregorianCalendar arrivalTime;
    private final String category;
    private final String categoryDescription;
    private final String trainNumber;


    public Train(GregorianCalendar arrivalTime, String departureStation,
                 String finalDestination, GregorianCalendar departureTime, String category,
                 String categoryDescription, String trainNumber) {
        this.arrivalTime = arrivalTime;
        this.departureStation = departureStation;
        this.finalDestination = finalDestination;
        this.departureTime = departureTime;
        this.category = category;
        this.categoryDescription = categoryDescription;
        this.trainNumber = trainNumber;
    }

    public GregorianCalendar getArrivalTime() {
        return arrivalTime;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public GregorianCalendar getDepartureTime() {
        return departureTime;
    }


    public String getFinalDestination() {
        return finalDestination;
    }

    public String getTrainNumber() {
        return trainNumber;
    }
}
