package it.zerocool.batmacaana.model.transport;

/**
 * Object representing train station
 */
public class TrainStation {

    private final String id;
    private final String extendedName;
    private final String shortName;
    private final String label;

    public TrainStation(String id, String extendedName, String shortName, String label) {
        this.id = id;
        this.extendedName = extendedName;
        this.shortName = shortName;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLabel() {
        return label;
    }

    public String getExtendedName() {

        return extendedName;
    }

    public boolean equals(Object o) {
        if (o != null && o.getClass() == TrainStation.class) {
            return ((TrainStation)o).getId().equals(this.getId());

        }
        return false;
    }

    public String toString() {
        return getExtendedName();
    }
}
