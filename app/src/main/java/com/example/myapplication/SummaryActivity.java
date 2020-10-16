package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    /**
     * TextViews to show the selected data
     */
    TextView selectedDisability, selectedSymptom, savedDate;
    /**
     * Buttons to go back to the main page
     */
    Button goToMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(8f, 0));
        entries.add(new BarEntry(2f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(10f, 3));
        entries.add(new BarEntry(5f, 4));
        entries.add(new BarEntry(9f, 5));

        BarDataSet bardataset = new BarDataSet(entries, "Symptoms Scale");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("10/1");
        labels.add("10/2");
        labels.add("10/3");
        labels.add("10/4");
        labels.add("10/5");
        labels.add("10/6");

        BarData data = new BarData(labels, bardataset);
        // set the data and list of labels into chart
        barChart.setData(data);
        // set the description
        barChart.setDescription("This chart tracks illness symptoms");
//        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        barChart.animateY(5000);
    }
}