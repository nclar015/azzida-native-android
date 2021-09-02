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
import com.azzida.model.ListerJobCancelledListModelDatum;

import java.util.ArrayList;


public class ListerCanceledJobAdpater extends
        RecyclerView.Adapter<ListerCanceledJobAdpater.ViewHolder> {

    /*public MyListingModel arrayMaincategories;*/
    public ArrayList<ListerJobCancelledListModelDatum> arrayMaincategories;
    private final Context _ctx;
    private final ClickView clickView;


    public ListerCanceledJobAdpater(Context _ctx, ClickView clickView, ArrayList<ListerJobCancelledListModelDatum> arrayMaincategories) {
        this._ctx = _ctx;
        this.arrayMaincategories = arrayMaincategories;
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

            holder.title.setText(arrayMaincategories.get(position).getJobTitle());

            String roundedBalance = arrayMaincategories.get(position).getAmount().replaceAll("\\.0*$", "");


            holder.txt_Amount.setText("$" + roundedBalance);

            String CompletedDate = "";

            if (arrayMaincategories.get(position).getStatus().equalsIgnoreCase("Cancelled")) {

                if (arrayMaincategories.get(position).getCancelDate() != null) {

                    CompletedDate = String.valueOf(DateFormat.format("MM/dd/yyyy", Long.parseLong(arrayMaincategories.get(position).getCancelDate().replaceAll("\\.0*$", "").replace("/Date(", "")
                            .replace(")/", ""))));

                }


            } else if (arrayMaincategories.get(position).getStatus().equalsIgnoreCase("Expired")) {

                CompletedDate = String.valueOf(DateFormat.format("MM/dd/yyyy", Long.parseLong(arrayMaincategories.get(position).getFromDate().replaceAll("\\.0*$", ""))));

            }


            holder.txt_JobCompletDate.setText(arrayMaincategories.get(position).getStatus() + " " + CompletedDate);


            holder.card_view_Recent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrayMaincategories.get(position).getStatus().equals("Expired")) {

                        clickView.clickitem(view, position);

                    } else if (arrayMaincategories.get(position).getStatus().equals("Cancelled")) {

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
        return arrayMaincategories == null ? 0 : arrayMaincategories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<ListerJobCancelledListModelDatum> arrayMaincategories) {
        this.arrayMaincategories = arrayMaincategories;
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
