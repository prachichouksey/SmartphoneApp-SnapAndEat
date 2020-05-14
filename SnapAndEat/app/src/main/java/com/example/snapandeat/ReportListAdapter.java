package com.example.snapandeat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {

    private final List<Report> mReports;
    private final Context mContext;

    public ReportListAdapter(List<Report> reports, Context context) {

        mReports = reports;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mReport = mReports.get(position);
        holder.mCafeName.setText(mReports.get(position).getRestaurant());
        holder.mItemName.setText(mReports.get(position).getItemName());
        holder.mItemCalorie.setText(mReports.get(position).getCalories() + " calories");
        holder.mDate.setText(mReports.get(position).getDate());
        ImageView img = holder.mItemImage;
        String imageUri = mReports.get(position).getImageUri();
        Uri myUri = Uri.parse(imageUri);
//        if(position == 0){
//            img.setImageResource(R.drawable.burger);
//        } else {
//            img.setImageResource(R.drawable.pizza);
//        }
        try {

            InputStream imageStream = mContext.getContentResolver().openInputStream(myUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            img.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(img, "Something went wrong.", Snackbar.LENGTH_LONG).show();
        }

//        String imageUrl = constructUrl(nearByCafe.get("photo_reference"));
//        new DownloadImageFromInternet(img).execute(imageUrl);
    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCafeName;
        public final TextView mItemName;
        public final TextView mItemCalorie;
        public final TextView mDate;
        public final ImageView mItemImage;
        public Report mReport;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCafeName = view.findViewById(R.id.report_cafe_name);
            mItemName = view.findViewById(R.id.report_item_name);
            mItemCalorie = view.findViewById(R.id.report_item_calories);
            mItemImage = view.findViewById(R.id.report_cafe_image);
            mDate = view.findViewById(R.id.date);
        }
    }

//    private String constructUrl(String photoReference)
//    {
//        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=960");
//        googleURL.append("&key=" + "AIzaSyCD0GTdc64QDR_WojsuRpCYArwae25_f00");
//        googleURL.append("&sensor=true");
//        googleURL.append("&photoreference=" + photoReference);
//        return googleURL.toString();
//    }
//
//    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
//        ImageView imageView;
//
//        public DownloadImageFromInternet(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String imageURL = urls[0];
//            Bitmap bimage = null;
//            try {
//                InputStream in = new java.net.URL(imageURL).openStream();
//                bimage = BitmapFactory.decodeStream(in);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bimage;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            imageView.setImageBitmap(result);
//        }
//    }
}
