package it.zerocool.batmacaana.model.transport;

import java.util.ArrayList;

/**
 * This object represents the train travel solution
 */
public class TrainTravelSolution {

    private final String duration;
    private final ArrayList<Train> vehicles;


    public TrainTravelSolution(String duration) {

        this.duration = duration;
        vehicles = new ArrayList<>();
    }


    public String getDuration() {
        return duration;
    }

    public ArrayList<Train> getVehicles() {
        return vehicles;
    }

    public void addTrainToSolution(Train t) {
        if (t != null) {
            vehicles.add(t);
        }
    }


}
