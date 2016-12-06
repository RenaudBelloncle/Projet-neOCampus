package Sensor;

public class InSensor extends Sensor{

    private String building;
    private String floor;
    private String room;
    private String description;
    private InType type;

    public InSensor(String id, String building, String floor, String room, String description, InType type) {
        super(id);
        this.building = building;
        this.floor = floor;
        this.room = room;
        this.description = description;
        this.type = type;
    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public InType getType() {
        return type;
    }

    public boolean isIn() {
        return true;
    }
}
