package com.azzida.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.MyJobModelDatum;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyJobAdpater extends
        RecyclerView.Adapter<MyJobAdpater.ViewHolder> {

    private final Context _ctx;
    private final ClickView clickView;
    private final double latitude;
    private final double longitude;
    public ArrayList<MyJobModelDatum> myJobModelData;


    public MyJobAdpater(Context _ctx, ClickView clickView, ArrayList<MyJobModelDatum> myJobModelData, double latitude, double longitude) {
        this._ctx = _ctx;
        this.myJobModelData = myJobModelData;
        this.clickView = clickView;
        this.latitude = latitude;
        this.longitude = longitude;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_feed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.title.setText(myJobModelData.get(position).getJobTitle());

            double bal = Double.parseDouble(myJobModelData.get(position).getAmount().replaceAll("\\.0*$", ""));

            int baln = (int) bal;

            String roundedBalance = String.valueOf(baln);

            String daten = String.valueOf(DateFormat.format("yyyy-MM-d", Long.parseLong(myJobModelData.get(position).getFromDate().replaceAll("\\.0*$", ""))));

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());


            SimpleDateFormat format = new SimpleDateFormat("d");

            Date date = inputFormat.parse(daten);

            if (daten.endsWith("1") && !daten.endsWith("11"))
                format = new SimpleDateFormat("EEE,MMMM d'st'");
            else if (daten.endsWith("2") && !daten.endsWith("12"))
                format = new SimpleDateFormat("EEE,MMMM d'nd'");
            else if (daten.endsWith("3") && !daten.endsWith("13"))
                format = new SimpleDateFormat("EEE,MMMM d'nd'");
            else
                format = new SimpleDateFormat("EEE,MMMM d'th'");

            String yourDate = format.format(date);

            holder.txt_Date.setText(yourDate);

/*
            viewHolder.txt_Date.setText(DateFormat.format("EEE,MMMM dd", Long.parseLong(myJobModelData.get(position).getFromDate())).toString());
*/


            holder.txt_Amount.setText("$" + roundedBalance);

            double lat1 = latitude;
            double long1 = longitude;

            float f = getDistance(new LatLng(Double.parseDouble(myJobModelData.get(position).getLatitude()), Double.parseDouble(myJobModelData.get(position).getLongitude())), new LatLng(lat1, long1));

            String mile = String.valueOf(Math.round(f));
            holder.txt_Distance.setText(mile + " Miles Away");

            if (myJobModelData.get(position).getListerProfilePicture() != null) {

                if (myJobModelData.get(position).getListerProfilePicture().length() > 0) {
                    Picasso.get().load(myJobModelData.get(position).getListerProfilePicture())
                            .placeholder(R.drawable.no_profile)
                            .error(R.drawable.no_profile)
                            .into(holder.img_Job);
                }

            } else {

                Picasso.get().load(R.drawable.no_profile)
                        .placeholder(R.drawable.no_profile)
                        .error(R.drawable.no_profile)
                        .into(holder.img_Job);
            }

            holder.status.setText(myJobModelData.get(position).getStatus());

            if (myJobModelData.get(position).getStatus().
                    equalsIgnoreCase("Active")) {

                holder.card_view_MyListing.getBackground().setColorFilter(
                        Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                holder.card_view_MyListing.setEnabled(true);
                holder.status.setTextColor(Color.parseColor("#76c9b2"));


            } else {

                holder.card_view_MyListing.getBackground().setColorFilter(
                        Color.parseColor("#f8f8f8"), PorterDuff.Mode.SRC_IN);
                holder.card_view_MyListing.setEnabled(false);
                holder.status.setTextColor(Color.parseColor("#696969"));

            }


            holder.card_view_MyListing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myJobModelData.get(position).getStatus().
                            equalsIgnoreCase("Active")) {

                        clickView.clickitem(view, position);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return myJobModelData == null ? 0 : myJobModelData.size();
    }

    public void updatedata(ArrayList<MyJobModelDatum> myJobModelData) {
        this.myJobModelData = myJobModelData;
        notifyDataSetChanged();


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public float getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);

        float distance = locationA.distanceTo(locationB) / 1000;//To convert Meter in Kilometer

        return convertKmsToMiles(distance);


    }

    public float convertKmsToMiles(float kms) {
        return (float) (0.621371 * kms);
    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txt_Amount, txt_Date, txt_Distance, status;
        public CardView card_view_MyListing;
        CircleImageView img_Job;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_Date = itemView.findViewById(R.id.txt_Date);
            txt_Distance = itemView.findViewById(R.id.txt_Distance);
            card_view_MyListing = itemView.findViewById(R.id.card_view_MyListing);
            img_Job = itemView.findViewById(R.id.img_Job);
            status = itemView.findViewById(R.id.status);

        }
    }
}
