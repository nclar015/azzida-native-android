package com.azzida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.ViewPaymentTransactionModelDatum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewPaymentTransactionAdpater extends
        RecyclerView.Adapter<ViewPaymentTransactionAdpater.ViewHolder> {

    private final Context _ctx;
    private final ClickView clickView;
    public ArrayList<ViewPaymentTransactionModelDatum> viewPaymentTransactionModelData;


    public ViewPaymentTransactionAdpater(Context _ctx, ClickView clickView, ArrayList<ViewPaymentTransactionModelDatum> viewPaymentTransactionModelData) {
        this._ctx = _ctx;
        this.viewPaymentTransactionModelData = viewPaymentTransactionModelData;
        this.clickView = clickView;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_transaction, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            if (viewPaymentTransactionModelData.get(position).getReceivedFrom() != null) {

                holder.txt_JobTitle.setText(viewPaymentTransactionModelData.get(position).getJobTitle());

                holder.txt_Pay_title.setText("Received from");

                holder.txt_Name.setText(viewPaymentTransactionModelData.get(position).getSenderName());

                String roundedBalance;

                if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("payment")) {

                    roundedBalance = viewPaymentTransactionModelData.get(position).getSeekerPaymentAmount().replaceAll("\\.0*$", "");


                } else {

                    roundedBalance = viewPaymentTransactionModelData.get(position).getTotalAmount().replaceAll("\\.0*$", "");


                }


                holder.txt_Amount.setText("$" + roundedBalance);
                holder.txt_Amount.setTextColor(_ctx.getResources().getColor(R.color.Green));

                holder.L2.setBackgroundResource(R.drawable.border_green_new);

                if (viewPaymentTransactionModelData.get(position).getSenderProfilePicture().length() > 0) {
                    Picasso.get().load(viewPaymentTransactionModelData.get(position).getSenderProfilePicture())
                            .placeholder(R.drawable.no_profile)
                            .error(R.drawable.no_profile)
                            .into(holder.img_Profile);
                }


            } else {
                if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Checker")) {

                    holder.txt_JobTitle.setText("Azzida Check");

                } else {

                    holder.txt_JobTitle.setText(viewPaymentTransactionModelData.get(position).getJobTitle());

                }


                holder.txt_Pay_title.setText("Paid to");

                if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Dispute")) {

                    holder.txt_Name.setText("Admin (Dispute)");

                } else if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Checker")) {

                    holder.txt_Name.setText("Admin (Azzida Check)");

                } else if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("CancelJob")) {

                    holder.txt_Name.setText("Admin (Job Cancel)");

                } else {

                    holder.txt_Name.setText(viewPaymentTransactionModelData.get(position).getToName() + " for " +
                            viewPaymentTransactionModelData.get(position).getJobTitle());

                }

                String roundedBalance = viewPaymentTransactionModelData.get(position).getTotalAmount().replaceAll("\\.0*$", "");

                holder.txt_Amount.setText("$" + roundedBalance);

                holder.txt_Amount.setTextColor(_ctx.getResources().getColor(R.color.Red));

                holder.L2.setBackgroundResource(R.drawable.border_red);

                if (viewPaymentTransactionModelData.get(position).getToProfilePicture().length() > 1) {
                    Picasso.get().load(viewPaymentTransactionModelData.get(position).getToProfilePicture())
                            .placeholder(R.drawable.no_profile)
                            .error(R.drawable.no_profile)
                            .into(holder.img_Profile);
                }


            }


            holder.card_view_Transaction.setOnClickListener(new View.OnClickListener() {
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
        return viewPaymentTransactionModelData == null ? 0 : viewPaymentTransactionModelData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<ViewPaymentTransactionModelDatum> viewPaymentTransactionModelData) {
        this.viewPaymentTransactionModelData = viewPaymentTransactionModelData;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_Name, txt_Amount, txt_Pay_title, txt_JobTitle;
        public CardView card_view_Transaction;
        CircleImageView img_Profile;
        LinearLayout L2;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_Name = itemView.findViewById(R.id.txt_Name);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_Pay_title = itemView.findViewById(R.id.txt_Pay_title);
            card_view_Transaction = itemView.findViewById(R.id.card_view_Transaction);
            img_Profile = itemView.findViewById(R.id.img_Profile);
            txt_JobTitle = itemView.findViewById(R.id.txt_JobTitle);
            L2 = itemView.findViewById(R.id.L2);

        }
    }
}
