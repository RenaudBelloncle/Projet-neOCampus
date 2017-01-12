package VisualisationInterface;

import Sensor.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Sensor.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;


/**
 * Created by bastien on 04/01/17.
 */
public class VisualisationGraphPanel extends ChartPanel {
    protected XYDataset dataset;
    protected JFreeChart chart;

    public VisualisationGraphPanel(Sensor sensor, List<Data> data){
        super(createChart(createDataSet(sensor, data), sensor));
    }

    private static XYDataset createDataSetTest(){
        TimeSeries s1 = new TimeSeries("L&G European Index Trust");
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 137.0);
        s1.add(new Month(7, 2002), 132.8);

        TimeSeries s2 = new TimeSeries("L&G UK Index Trust");
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 110.3);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);

        TimeSeries s3 = new TimeSeries("andres2255");
        s3.add(new Month(2, 2001), 129.6);

        s3.add(new Month(10, 2001), 106.1);
        s3.add(new Month(1, 2002), 111.7);
        s3.add(new Month(2, 2002), 111.0);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);

        return dataset;
    }

    private static XYDataset createDataSet(Sensor sensor, List<Data> datas){
        String type = sensor.getSensorType().getType();
        TimeSeries s1 = new TimeSeries(type);
        for(Data data : datas){
            Date date = data.getDate();
            s1.add(new Second(date), data.getData());
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        return dataset;
    }

    private static JFreeChart createChart(XYDataset dataset, Sensor sensor){
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Capteur",
                "Date",
                sensor.getSensorType().getType()+"("+sensor.getSensorType().getUnit()+")",
                dataset,
                true, true, false);

        XYPlot plot = chart.getXYPlot();//a verifier
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        return chart;
    }


    public static void main(String[] args) {
        JFrame fen = new JFrame();
        fen.setSize(300, 300);
        JFreeChart chart;
        OutSensor sensor = new OutSensor("salon", SensorType.HOTWATER, "lol", "mdr");
        List<Data> data = new ArrayList<Data>();
        Date date = new Date();
        Date dat = new Date(1996, 7, 15, 6, 30, 22);
        Date dat1 = new Date(1996, 6, 10, 6, 30, 22);
        Date dat2 = new Date(1996, 5, 16, 6, 30, 22);
        data.add(new Data(12., dat));
        data.add(new Data(18., dat1));
        data.add(new Data(5., dat2));

        VisualisationGraphPanel test = new VisualisationGraphPanel(sensor, data);
        //chart = test.createChart(test.createDataSet(sensor, data), sensor);
        //ChartPanel graph = new ChartPanel(chart);
        //fen.add(fen.getContentPane().add(graph));
        fen.add(test);
        fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fen.setVisible(true);

    }
}
