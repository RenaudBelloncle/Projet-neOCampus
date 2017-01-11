package VisualisationInterface;

import Sensor.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class TestReadFile {
    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        String name = "data.txt";
        List<InSensor> inSensors = new ArrayList<>();
        List<OutSensor> outSensors = new ArrayList<>();
        Map<String, List<Data>> data = new HashMap<>();

        File dataFile = new File(path + "/" + name);
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length == 3) {
                    String id = tokens[0];
                    Date date = Date.from(Instant.parse(tokens[1]));
                    Double value = Double.valueOf(tokens[2]);

                    List<Data> sensorValues = new ArrayList<>();
                    if (data.containsKey(id))
                        sensorValues = data.get(id);
                    sensorValues.add(new Data(value, date));

                    if (data.containsKey(id))
                        data.replace(id, sensorValues);
                    else
                        data.put(id, sensorValues);
                } else {
                    SensorType type = SensorType.STRINGTOTYPE(tokens[1]);
                    switch (tokens[2]) {
                        case "Intérieur":
                            inSensors.add(new InSensor(tokens[0], type, tokens[3],
                                    tokens[4], tokens[5], tokens[6]));
                            break;
                        case "Extérieur":
                            outSensors.add(new OutSensor(tokens[0], type, tokens[3], tokens[4]));
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(inSensors);
        System.out.println(outSensors);
        System.out.println(data);
    }
}
