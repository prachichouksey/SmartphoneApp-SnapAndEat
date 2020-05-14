package com.example.snapandeat;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {
    private static final String CALORIES = "calories";
    private static final String CARBS = "carbs";
    private static final String FATS = "fats";
    private static final String PROTEINS = "proteins";

    public BarFragment() {
        // Required empty public constructor
    }

    public static BarFragment newInstance(float calories, float carbs, float fats, float proteins) {
        BarFragment fragment = new BarFragment();
        Bundle args = new Bundle();
        args.putFloat(CALORIES, calories);
        args.putFloat(CARBS, carbs);
        args.putFloat(FATS, fats);
        args.putFloat(PROTEINS, proteins);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bar, container, false);

        BarChart barChart = view.findViewById(R.id.barchart);
        barChart.getDescription().setEnabled(false);
//        barChart.setExtraOffsets(5, 10, 5, 5);
//        barChart.setDragDecelerationFrictionCoef(0.95f);
//
//        Legend legend = barChart.getLegend();
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setXEntrySpace(7f);
//        legend.setYEntrySpace(0f);
//        legend.setYOffset(0f);

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0f, getArguments().getFloat(CALORIES)));
        values.add(new BarEntry(1f, getArguments().getFloat(CARBS)));
        values.add(new BarEntry(2f, getArguments().getFloat(FATS)));
        values.add(new BarEntry(3f, getArguments().getFloat(PROTEINS)));

        BarDataSet dataSet = new BarDataSet(values, "Nutritions Intake");
        dataSet.setBarBorderWidth(1f);
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartCalories));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartCarbs));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartFats));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartProteins));

        dataSet.setColors(colors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
//        labels.add("Calories");
//        labels.add("Carbs");
//        labels.add("Fats");
//        labels.add("Proteins");

//        ArrayList<String> xAxis = new ArrayList<>();
//        xAxis.add("Calories");
//        xAxis.add("Carbs");
//        xAxis.add("Fats");
//        xAxis.add("Proteins");

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
//        data.setBarWidth(0.9f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(30);
        xAxis.setSpaceMax(5f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);

        Legend l=barChart.getLegend();
        LegendEntry l1=new LegendEntry("Calories", Legend.LegendForm.DEFAULT,10f,2f,null,ContextCompat.getColor(getContext(),R.color.colorBarChartCalories));
        LegendEntry l2=new LegendEntry("Carbs", Legend.LegendForm.DEFAULT,10f,2f,null, ContextCompat.getColor(getContext(),R.color.colorBarChartCarbs));
        LegendEntry l3=new LegendEntry("Fats", Legend.LegendForm.DEFAULT,10f,1f,null,ContextCompat.getColor(getContext(),R.color.colorBarChartFats));
        LegendEntry l4=new LegendEntry("Proteins", Legend.LegendForm.DEFAULT,10f,2f,null, ContextCompat.getColor(getContext(),R.color.colorBarChartProteins));

//        barChart.setFitBars(true);
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        l.setCustom(new LegendEntry[]{l1,l2,l3,l4});
        barChart.setData(data);
        barChart.setVisibleXRange(1,4);
        barChart.invalidate();
//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxis));
//        barChart.getXAxis().setGranularity(1);
//        barChart.getXAxis().setGranularityEnabled(true);
        barChart.animateY(1000);
        return view;
    }
}
