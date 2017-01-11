package Sensor;

public class SensorType {

    public static final SensorType TEMPERATURE = new SensorType("Température",
            "degrés Celsius", "°C", -10, 50, 1, 0.2, 60);
    public static final SensorType HUMIDITY = new SensorType("Humidité", "", "%", 0, 100, 0, 0, 90);
    public static final SensorType BRIGHTNESS = new SensorType("Luminosité", "Lumen", "lum", 0, 1000, 2, 0.01, 5);
    public static final SensorType SOUNDVOLUME = new SensorType("Volume Sonore", "Décibel", "dB", 0, 120, 1, 0.1, 10);
    public static final SensorType LIGHTCONSUMPTION = new SensorType("Consommation Eclairage",
            "Watt", "W", 0, 3000, 0, 1, 30);
    public static final SensorType COLDWATER = new SensorType("Eau Froide", "Litre", "l", 0, 100, 1, 0, 600);
    public static final SensorType HOTWATER = new SensorType("Eau Chaude", "Litre", "l", 0, 1000, 1, 0, 60);
    public static final SensorType WINDSPEED = new SensorType("Vitesse Vent",
            "kilomètre par heure", "km/h", 0, 30, 1, 0.3, 20);
    public static final SensorType ATMOSPHERICPRESSURE = new SensorType("Pression Atmosphérique",
            "Hectopascal", "hPa", 1000, 1100, 1, 0, 300);

    private String type;
    private String unit;
    private String short_unit;
    private int[] interval = new int[2];
    private int precision;
    private double margin;
    private int frequency;

    private SensorType(String type, String unit, String short_unit, int interval_min, int interval_max,
                       int precision, double margin, int frequency) {
        this.type = type;
        this.unit = unit;
        this.short_unit = short_unit;
        interval[0] = interval_min;
        interval[1] = interval_max;
        this.precision = precision;
        this.margin = margin;
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public String getShort_unit() {
        return short_unit;
    }

    public int[] getInterval() {
        return interval;
    }

    public int getPrecision() {
        return precision;
    }

    public double getMargin() {
        return margin;
    }

    public int getFrequency() {
        return frequency;
    }

    public String toString() {
        return getType();
    }

    public static SensorType STRINGTOTYPE(String s) {
        SensorType type = null;
        switch (s) {
            case "Température":
                type = SensorType.TEMPERATURE;
                break;
            case "Humidité":
                type = SensorType.HUMIDITY;
                break;
            case "Luminosité":
                type = SensorType.BRIGHTNESS;
                break;
            case "Volume Sonore":
                type = SensorType.SOUNDVOLUME;
                break;
            case "Consommation Eclairage":
                type = SensorType.LIGHTCONSUMPTION;
                break;
            case "Eau Froide":
                type = SensorType.COLDWATER;
                break;
            case "Eau Chaude":
                type = SensorType.HOTWATER;
                break;
            case "Vitesse Vent":
                type = SensorType.WINDSPEED;
                break;
            case "Pression Atmosphérique":
                type = SensorType.ATMOSPHERICPRESSURE;
                break;
            default:
                break;
        }
        return type;
    }
}
