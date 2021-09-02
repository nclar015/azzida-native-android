package com.azzida.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.SeekerJobCancelledListModelDatum;

import java.util.ArrayList;


public class SeekerCanceledJobAdpater extends
        RecyclerView.Adapter<SeekerCanceledJobAdpater.ViewHolder> {

    public ArrayList<SeekerJobCancelledListModelDatum> recentModelData;
    private Context _ctx;
    private ClickView clickView;


    public SeekerCanceledJobAdpater(Context _ctx, ClickView clickView, ArrayList<SeekerJobCancelledListModelDatum> recentModelData) {
        this._ctx = _ctx;
        this.recentModelData = recentModelData;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job_completed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.title.setText(recentModelData.get(position).getJobTitle());

            String roundedBalance = recentModelData.get(position).getAmount().replaceAll("\\.0*$", "");


            holder.txt_Amount.setText("$" + roundedBalance);

            String CompletedDate = "";

            if (recentModelData.get(position).getStatus().equalsIgnoreCase("Cancelled")) {

                if (recentModelData.get(position).getCancelDate() != null) {

                    CompletedDate = String.valueOf(DateFormat.format("MM/dd/yyyy", Long.parseLong(recentModelData.get(position).getCancelDate().replaceAll("\\.0*$", "").replace("/Date(", "")
                            .replace(")/", ""))));

                }


            } else if (recentModelData.get(position).getStatus().equalsIgnoreCase("Expired")) {

                CompletedDate = String.valueOf(DateFormat.format("MM/dd/yyyy", Long.parseLong(recentModelData.get(position).getFromDate().replaceAll("\\.0*$", ""))));

            }

            holder.txt_JobCompletDate.setText(recentModelData.get(position).getStatus() + " " + CompletedDate);


            holder.card_view_Recent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recentModelData.get(position).getStatus().equals("Not Selected")) {

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
        return recentModelData == null ? 0 : recentModelData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<SeekerJobCancelledListModelDatum> recentModelData) {
        this.recentModelData = recentModelData;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txt_Amount, txt_JobCompletDate;
        public CardView card_view_Recent;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_JobCompletDate = itemView.findViewById(R.id.txt_JobCompletDate);
            card_view_Recent = itemView.findViewById(R.id.card_view_Recent);


        }
    }
}
