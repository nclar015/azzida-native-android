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
import com.azzida.model.ListerInProgressJobModelDatum;

import java.util.ArrayList;


public class ListerInProgressJobAdpater extends
        RecyclerView.Adapter<ListerInProgressJobAdpater.ViewHolder> {

    /*public MyListingModel arrayMaincategories;*/
    public ArrayList<ListerInProgressJobModelDatum> arrayMaincategories;
    private final Context _ctx;
    private final ClickView clickView;


    public ListerInProgressJobAdpater(Context _ctx, ClickView clickView, ArrayList<ListerInProgressJobModelDatum> arrayMaincategories) {
        this._ctx = _ctx;
        this.arrayMaincategories = arrayMaincategories;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job_inprogress, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.title.setText(arrayMaincategories.get(position).getJobTitle());
            String roundedBalance = arrayMaincategories.get(position).getAmount().replaceAll("\\.0*$", "");

            holder.txt_Amount.setText("$" + roundedBalance);

            holder.txt_JobStatus.setText(arrayMaincategories.get(position).getApplicantCount() + " Applications");

            if (arrayMaincategories.get(position).getApplicationAccepted() != null) {

                if (arrayMaincategories.get(position).getApplicationAccepted()) {

                    holder.txt_JobStatus.setText("Application Pending");
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


            holder.card_view_Lis_InProgressJob.setOnClickListener(new View.OnClickListener() {
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

    public void updatedata(ArrayList<ListerInProgressJobModelDatum> arrayMaincategories) {
        this.arrayMaincategories = arrayMaincategories;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txt_Amount, txt_JobStatus;
        public CardView card_view_Lis_InProgressJob;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_JobStatus = itemView.findViewById(R.id.txt_JobStatus);
            card_view_Lis_InProgressJob = itemView.findViewById(R.id.card_view_Lis_InProgressJob);


        }
    }
}
