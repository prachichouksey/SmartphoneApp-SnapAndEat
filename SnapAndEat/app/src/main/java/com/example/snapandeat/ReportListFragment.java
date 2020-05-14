package com.example.snapandeat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportListFragment extends Fragment {

    private static final String ARG_REPORT = "reports";
    private static final String ARG_TOTAL_ITEMS = "totalItems";
    private static final String ARG_CALORIES = "calories";
    private static final String ARG_CARBS = "carbs";
    private static final String ARG_FATS = "fats";
    private static final String ARG_PROTEINS = "proteins";
    TextView totalItemsView, totalCaloriesView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReportListFragment() {
    }

    public static ReportListFragment newInstance(ArrayList<Report> reports, int totalItems,
                                                 float calories, float carbs, float fats, float proteins ) {
        ReportListFragment fragment = new ReportListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_REPORT, reports);
        args.putInt(ARG_TOTAL_ITEMS, totalItems);
        args.putFloat(ARG_CALORIES, calories);
        args.putFloat(ARG_CARBS, carbs);
        args.putFloat(ARG_FATS, fats);
        args.putFloat(ARG_PROTEINS, proteins);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        RecyclerView recyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_report_list, container, false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        totalItemsView = recyclerView.findViewById(R.id.totalItems);
//        totalCaloriesView = recyclerView.findViewById(R.id.totalCalories);
//
//        totalItemsView.setText(Integer.toString(getArguments().getInt(ARG_TOTAL_ITEMS)));
//        totalCaloriesView.setText(Float.toString(getArguments().getFloat(ARG_CALORIES)));
//
//        ArrayList<Report> reports = (ArrayList<Report>) getArguments().getSerializable(ARG_REPORT);
//
//        recyclerView.setAdapter(new ReportListAdapter(reports, getContext()));
//
//        return recyclerView;

        View view =  inflater.inflate(R.layout.fragment_report_list, container, false);


        totalItemsView = view.findViewById(R.id.totalItems);
        totalCaloriesView = view.findViewById(R.id.totalCalories);

        totalItemsView.setText(Integer.toString(getArguments().getInt(ARG_TOTAL_ITEMS)));
        totalCaloriesView.setText(Float.toString(getArguments().getFloat(ARG_CALORIES)));

        ArrayList<Report> reports = (ArrayList<Report>) getArguments().getSerializable(ARG_REPORT);

        RecyclerView recyclerView = view.findViewById(R.id.report_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ReportListAdapter(reports, getContext()));

        return view;
    }
}
