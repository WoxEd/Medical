package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.Arrays;

public class SummaryActivity extends AppCompatActivity {


    /**
     * ArrayList that holds the object which contains all the data
     */
    private ArrayList<SummaryObject> list = new ArrayList<>();

    /**
     * Colour value for frequency
     */
    private static final int FREQUENCY_COLOUR = Color.RED;

    /**
     * Colour value for severity
     */
    private static final int SEVERITY_COLOUR = Color.BLUE;

    /**
     * ArrayList of disabilities
     */
    private static final ArrayList<String> DISABILITY_LIST  = new ArrayList<>(Arrays.asList(MainActivity.VISION, MainActivity.SPEAKING, MainActivity.HEARING, MainActivity.WALKING,
            MainActivity.ELIMINATING, MainActivity.FEEDING, MainActivity.DRESSING, MainActivity.MENTAL));

    /**
     * Predefined index for vision used for accessing severity and frequency
     */
    private static final int VISION_INDEX = 0;

    /**
     * Predefined index for speaking used for accessing severity and frequency
     */
    private static final int SPEAKING_INDEX = 1;

    /**
     * Predefined index for hearing used for accessing severity and frequency
     */
    private static final int HEARING_INDEX = 2;

    /**
     * Predefined index for walking used for accessing severity and frequency
     */
    private static final int WALKING_INDEX = 3;

    /**
     * Predefined index for eliminating used for accessing severity and frequency
     */
    private static final int ELIMINATING_INDEX = 4;

    /**
     * Predefined index for feeding used for accessing severity and frequency
     */
    private static final int FEEDING_INDEX = 5;

    /**
     * Predefined index for dressing used for accessing severity and frequency
     */
    private static final int DRESSING_INDEX = 6;

    /**
     * Predefined index for mental used for accessing severity and frequency
     */
    private static final int MENTAL_INDEX = 7;

    /**
     * Start of x axis
     */
    private static final int X_AXIS_START = VISION_INDEX;

    /**
     * End of x axis
     */
    private static final int X_AXIS_END = MENTAL_INDEX;

    /**
     * Predefined array used to store values for average severity. Accessible by predefined indexes
     */
    private int[] averageSeverity = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * Predefined array used to store values for total frequency. Accessible by predefined indexes
     */
    private int[] totalFrequency = {0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * BarChart view which is found in activity_summary.xml
     */
    private BarChart chart;

    /**
     * String of a filter to display both frequency and severity
     */
    private static final String FILTER_BOTH = "BOTH";

    /**
     * String of a filer to display only frequency
     */
    private static final String FILTER_FREQUENCY = "FREQUENCY";

    /**
     * String of a filter to display Severity
     */
    private static final String FILTER_SEVERITY = "SEVERITY";

    /**
     * Default filter set to FILTER_BOTH
     */
    private static final String DEFAULT_FILTER = FILTER_BOTH;

    /**
     * String for getting current filter
     */
    private String filter;

    /**
     * BarDataSet used to store frequency data
     */
    private BarDataSet frequencyDataSet;

    /**
     * BarDataSet used to store severityDataSet
     */
    private BarDataSet severityDataSet;

    /**
     * Animation time for bar
     */
    private static final int X_ANIMATE_TIME = 1000;

    /**
     * Animation time for bar
     */
    private static final int Y_ANIMATE_TIME = 1000;

    /**
     * Angle for labels
     */
    private static final int X_AXIS_ANGLE = 90;

    /**
     * On Create does the following
     * 1) Creates references to chart, and radio group in the layout
     * 2) Initializes s the filter to the default value
     * 3) Fetches an ArrayList passed from ListView activity
     * 4) If the list is not null it will create BarGraph and add click listeners to radio filters
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //1) Initialize values
        chart = findViewById(R.id.bar_chart);
        RadioGroup radioFilters = findViewById(R.id.graph_filter);

        //2) Sets default filter
        filter = DEFAULT_FILTER;

        //3) Fetches intent
        list = (ArrayList<SummaryObject>) getIntent().getSerializableExtra(MainActivity.LIST);

        //4) Processes list and chart if list is not null
        if(list != null && list.size() > 0) {
            processList();
            makeBarChart();
            addRadioListeners(radioFilters);
        }
    }

    /**
     * Reads the ArrayList of data to display
     * Using the disability type, rating, frequency it sets values for two predefined arrays
     * averageSeverity and totalFrequency which are used to create BarDataSets
     */
    private void processList() {
        for(SummaryObject obj : list) {
            int index = fetchIndex(obj.getDisabilityType());
            averageSeverity[index] += obj.getRating();
            totalFrequency[index]++;
            }

        for(int i=X_AXIS_START; i<=X_AXIS_END; i++) {
            if(totalFrequency[i] > 0)
                averageSeverity[i] /= totalFrequency[i];
        }
    }

    /**
     * Method returns a predefined index based on a String disability type
     * @param disabilityType String for disability
     * @return integer based on disability type
     */
    private int fetchIndex(String disabilityType) {
        switch (disabilityType) {
            case MainActivity.SPEAKING:
                return SPEAKING_INDEX;
            case MainActivity.HEARING:
                return HEARING_INDEX;
            case MainActivity.WALKING:
                return WALKING_INDEX;
            case MainActivity.ELIMINATING:
                return ELIMINATING_INDEX;
            case MainActivity.FEEDING:
                return FEEDING_INDEX;
            case MainActivity.DRESSING:
                return DRESSING_INDEX;
            case MainActivity.MENTAL:
                return MENTAL_INDEX;
            default: //default case will act like Vision
                return VISION_INDEX;
        }
    }

    /**
     * Sets click functions for radio group. By default it will assume BOTH has been clicked.
     * When an option is clicked it changes the String filter to the radio group filter type
     * Then calls on makeBarChart to remake the chart based on the filter
     * @param radioGroup The radio group click functions will be added to
     */
    private void addRadioListeners(RadioGroup radioGroup) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId) {
                case(R.id.filter_frequency):
                    filter = FILTER_FREQUENCY;
                    makeBarChart();
                    break;
                case(R.id.filter_severity):
                    filter = FILTER_SEVERITY;
                    makeBarChart();
                    break;
                default:
                    filter = FILTER_BOTH;
                    makeBarChart();
                    break;
            }
        });
    }

    /**
     * Sets the chart data to data created by the getData() method
     * Performs basic chart functions such as setting axis and animation time.
     */
    public void makeBarChart() {
        BarData data = new BarData(DISABILITY_LIST, getData());
        chart.animateXY(X_ANIMATE_TIME,Y_ANIMATE_TIME);
        chart.getXAxis().setLabelRotationAngle(X_AXIS_ANGLE);
        chart.getXAxis().setTextSize(12f);
        chart.getLegend().setTextSize(20f);
        chart.setData(data);
        chart.getData().setValueTextSize(10f);

    }

    /**
     * Returns a BarDataSet containing either frequency, severity, or both data sets
     * What is returned depends on the current filter which is set when the radio button is clicked.
     * @return ArrayList of BarDataSets containing either frequency, severity, or both data sets
     */
    public ArrayList getData() {
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        if(frequencyDataSet == null)
            frequencyDataSet = makeFrequencyDataSet();
        if(severityDataSet == null)
            severityDataSet = makeSeverityDataSet();

        switch(filter) {
            case(FILTER_FREQUENCY):
                barDataSets.add(frequencyDataSet);
                chart.setDescription(getString(R.string.chart_description_frequency));
                break;
            case(FILTER_SEVERITY):
                barDataSets.add(severityDataSet);
                chart.setDescription(getString(R.string.chart_description_severity));
                break;
            default:
                barDataSets.add(frequencyDataSet);
                barDataSets.add(severityDataSet);
                chart.setDescription(getString(R.string.chart_description_both));
        }
        chart.setDescriptionTextSize(15f);
        return barDataSets;
    }

    /**
     * Creates BarDataSet for frequency of each disability
     * @return BarDataSet
     */
    private BarDataSet makeFrequencyDataSet() {
        ArrayList<BarEntry> frequencyBars = new ArrayList<>();
        for(int i = X_AXIS_START; i <= X_AXIS_END; i++) {
            frequencyBars.add(new BarEntry(totalFrequency[i], i));
        }
        BarDataSet frequencyDataSet = new BarDataSet(frequencyBars, getString(R.string.chart_label_frequency));
        frequencyDataSet.setColor(FREQUENCY_COLOUR);
        return frequencyDataSet;
    }

    /**
     * Creates BarDataSet for average severity of each disability
     * @return BarDataSet
     */
    private BarDataSet makeSeverityDataSet() {
        ArrayList<BarEntry> severityBars = new ArrayList<>();
        for(int i=X_AXIS_START; i<=X_AXIS_END; i++) {
            severityBars.add(new BarEntry(averageSeverity[i], i));
        }
        BarDataSet severityDataSet = new BarDataSet(severityBars, getString(R.string.chart_label_average_severity));
        severityDataSet.setColor(SEVERITY_COLOUR);
        return severityDataSet;
    }
}