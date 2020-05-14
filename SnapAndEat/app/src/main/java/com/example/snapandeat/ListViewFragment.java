package com.example.snapandeat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewFragment extends Fragment {

    private static final String ARG_PARAM = "jsonPlaces";
    private OnFragmentInteractionListener mListener;
    private String jsonPlaces;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListViewFragment() {
    }

    public static ListViewFragment newInstance(String jsonPlaces) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, jsonPlaces);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<HashMap<String, String>> nearByPlacesForListView = new ArrayList<>();

        String jsonPlaces = getArguments().getString(ARG_PARAM);
        if(!jsonPlaces.equals("")){
            DataParserForList dataParserForList = new DataParserForList(((RestaurantActivity)getActivity()).mLocation);
            nearByPlacesForListView = dataParserForList.parse(jsonPlaces);
        }

        recyclerView.setAdapter(new ListViewAdapter(nearByPlacesForListView, mListener));

        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveReport(String restaurant);
    }
}
