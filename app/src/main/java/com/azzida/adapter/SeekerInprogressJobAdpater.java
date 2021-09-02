package com.azzida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.SeekerInprogressJobModelDatum;

import java.util.ArrayList;


public class SeekerInprogressJobAdpater extends
        RecyclerView.Adapter<SeekerInprogressJobAdpater.ViewHolder> {

    /*public MyListingModel arrayMaincategories;*/
    public ArrayList<SeekerInprogressJobModelDatum> arrayMaincategories;
    private Context _ctx;
    private ClickView clickView;


    public SeekerInprogressJobAdpater(Context _ctx, ClickView clickView, ArrayList<SeekerInprogressJobModelDatum> arrayMaincategories) {
        this._ctx = _ctx;
        this.arrayMaincategories = arrayMaincategories;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job_seeker, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.title.setText(arrayMaincategories.get(position).getJobTitle());
            String roundedBalance = arrayMaincategories.get(position).getAmount().replaceAll("\\.0*$", "");

            holder.txt_Amount.setText("$" + roundedBalance);


            if (arrayMaincategories.get(position).getIsApply()) {

                holder.txt_JobStatus.setText("Applied");
            }

            if (arrayMaincategories.get(position).getApplicationAccepted() != null) {
                if (arrayMaincategories.get(position).getApplicationAccepted()) {

                    holder.txt_JobStatus.setText("Application Accepted");
                }
            }
            if (arrayMaincategories.get(position).getOfferAccepted() != null) {
                if (arrayMaincategories.get(position).getOfferAccepted()) {

                    holder.txt_JobStatus.setText("Active Awaiting Completion");

                }
            }

            if (arrayMaincategories.get(position).getIsComplete() != null) {

                if (arrayMaincategories.get(position).getIsComplete()) {

                    holder.txt_JobStatus.setText("Completion Pending");

                }

            }

            holder.card_view_Seeker_InProgressJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickView.clickitem(view, position);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return arrayMaincategories == null ? 0 : arrayMaincategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<SeekerInprogressJobModelDatum> arrayMaincategories) {
        this.arrayMaincategories = arrayMaincategories;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txt_Amount, txt_JobStatus;
        public CardView card_view_Seeker_InProgressJob;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_JobStatus = itemView.findViewById(R.id.txt_JobStatus);
            card_view_Seeker_InProgressJob = itemView.findViewById(R.id.card_view_Seeker_InProgressJob);


        }
    }
}
