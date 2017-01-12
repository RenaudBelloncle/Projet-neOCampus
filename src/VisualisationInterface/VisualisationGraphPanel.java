package VisualisationInterface;

import Sensor.Sensor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class VisualisationGraphPanel extends ChartPanel {

    public VisualisationGraphPanel(Sensor sensor, List<Data> data){
        super(createChart(createDataSet(sensor, data), sensor));
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
        JFreeChart chart = ChartFactory.createTimeSeriesChart(null,
                "Date",
                sensor.getSensorType().getType()+"("+sensor.getSensorType().getShort_unit()+")",
                dataset,
                false, true, false);

        XYPlot plot = chart.getXYPlot();//a verifier
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        return chart;
    }
}
