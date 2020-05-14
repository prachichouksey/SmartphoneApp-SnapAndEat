package com.example.snapandeat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieFragment extends Fragment {
    private static final String CALORIES = "calories";
    private static final String CARBS = "carbs";
    private static final String FATS = "fats";
    private static final String PROTEINS = "proteins";

    public PieFragment() {
        // Required empty public constructor
    }

    public static PieFragment newInstance(float calories, float carbs, float fats, float proteins) {
        PieFragment fragment = new PieFragment();
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
        View view = inflater.inflate(R.layout.fragment_pie, container, false);

        PieChart pieChart = view.findViewById(R.id.piechart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
//        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(90);
        pieChart.setRotationEnabled(true);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

//        ArrayList values = new ArrayList();
//        values.add(new PieEntry(getArguments().getFloat(CALORIES),0));
//        PieDataSet dataSet = new PieDataSet(values, "Nutritions Intake");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        ArrayList nutrition = new ArrayList();
//        nutrition.add("Calories");
//        PieData data = new PieData(dataSet);
//        pieChart.setData(data);
//        pieChart.animateXY(5000, 5000);
        ArrayList values = new ArrayList<PieEntry>();
        values.add(new PieEntry(getArguments().getFloat(CALORIES),"Calories"));
        values.add(new PieEntry(getArguments().getFloat(CARBS),"Carbs"));
        values.add(new PieEntry(getArguments().getFloat(FATS),"Fats"));
        values.add(new PieEntry(getArguments().getFloat(PROTEINS),"Proteins"));

        PieDataSet dataSet = new PieDataSet(values, "Nutritions Intake");
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartCalories));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartCarbs));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartFats));
        colors.add(ContextCompat.getColor(getContext(),R.color.colorBarChartProteins));
//        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new PercentFormatter());

        pieChart.setUsePercentValues(true);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateXY(1000, 1000);
        return view;
    }

//    private SpannableString generateCenterSpannableText() {
//
//        SpannableString s = new SpannableString("");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }
}
