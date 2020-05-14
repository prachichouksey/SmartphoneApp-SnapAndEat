package com.example.snapandeat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.example.snapandeat.ListViewFragment.OnFragmentInteractionListener;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private final List<HashMap<String, String>> mPlaces;
    private final OnFragmentInteractionListener mListener;

    public ListViewAdapter(List<HashMap<String, String>> places, OnFragmentInteractionListener listener) {
        mPlaces = places;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> nearByCafe = mPlaces.get(position);
        holder.mCafeName.setText(nearByCafe.get("place_name"));
        holder.mCafeAddress.setText(nearByCafe.get("vicinity"));
        holder.mCafeRating.setText(nearByCafe.get("rating"));
        holder.mCafeDistance.setText(nearByCafe.get("distance")+" miles");
        String pricingSymbol = "";
        if(nearByCafe.get("pricing")!="-NA-"){
            try{
                for(int i=0;i<=Integer.parseInt(nearByCafe.get("pricing"));i++){
                    pricingSymbol += "$";
                }
            } catch(NumberFormatException e){

            }

        } else {
            pricingSymbol = "-NA-";
        }
        holder.mCafePricing.setText(pricingSymbol);
        holder.mCafeTiming.setText(nearByCafe.get("timing"));
        ImageView img = holder.mCafeImage;
        String imageUrl = constructUrl(nearByCafe.get("photo_reference"));
        new DownloadImageFromInternet(img).execute(imageUrl);

        holder.mSaveReport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onSaveReport(holder.mCafeName.getText().toString());
                    }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCafeName;
        public final TextView mCafeAddress;
        public final TextView mCafeRating;
        public final TextView mCafePricing;
        public final TextView mCafeDistance;
        public final TextView mCafeTiming;
        public final ImageView mCafeImage;
        public final ImageButton mSaveReport;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCafeName = view.findViewById(R.id.cafe_name);
            mCafeAddress = view.findViewById(R.id.cafe_address);
            mCafeRating = view.findViewById(R.id.cafe_rating);
            mCafeImage = view.findViewById(R.id.cafe_image);
            mCafePricing = view.findViewById(R.id.cafe_pricing);
            mCafeTiming = view.findViewById(R.id.cafe_timing);
            mCafeDistance = view.findViewById(R.id.cafe_distance);
            mSaveReport = view.findViewById(R.id.save_report);
        }
    }

    private String constructUrl(String photoReference)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=960");
        googleURL.append("&key=" + "AIzaSyCD0GTdc64QDR_WojsuRpCYArwae25_f00");
        googleURL.append("&sensor=true");
        googleURL.append("&photoreference=" + photoReference);
        return googleURL.toString();
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
