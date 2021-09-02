package com.azzida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.GetCustomerCardsModelDatum;

import java.util.ArrayList;


public class SaveCardAdpater extends
        RecyclerView.Adapter<SaveCardAdpater.ViewHolder> {

    private final Context _ctx;
    private final ClickView clickView;
    public ArrayList<GetCustomerCardsModelDatum> recentModelData;


    public SaveCardAdpater(Context _ctx, ClickView clickView, ArrayList<GetCustomerCardsModelDatum> recentModelData) {
        this._ctx = _ctx;
        this.recentModelData = recentModelData;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_payment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.txt_CardType.setText(recentModelData.get(position).getCardType());
            holder.txt_Type.setText("Card");
            holder.txt_CardNo.setText("**** **** " + recentModelData.get(position).getCardNumber());
            holder.txt_exp.setText("Exp " + recentModelData.get(position).getExpiryMonth() + "/" + recentModelData.get(position).getExpiryYear().substring(2));


            if (recentModelData.get(position).getCardType().equalsIgnoreCase("Visa")) {

                holder.Cardimg.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_visa));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("Mastercard")) {

                holder.Cardimg.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_mastercard));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("Discover")) {


                holder.Cardimg.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_discover));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("American Express")) {

                holder.Cardimg.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_amex));

            } else {

                holder.Cardimg.setImageResource(android.R.color.transparent);

            }


            holder.CardDelete.setOnClickListener(new View.OnClickListener() {
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
        return recentModelData == null ? 0 : recentModelData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<GetCustomerCardsModelDatum> recentModelData) {
        this.recentModelData = recentModelData;
        notifyDataSetChanged();

    }

    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_CardType, txt_Type, txt_CardNo, txt_exp, CardDelete;
        public ImageView Cardimg;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_CardType = itemView.findViewById(R.id.txt_CardType);
            txt_Type = itemView.findViewById(R.id.txt_Type);
            txt_CardNo = itemView.findViewById(R.id.txt_CardNo);
            txt_exp = itemView.findViewById(R.id.txt_exp);
            Cardimg = itemView.findViewById(R.id.Cardimg);
            CardDelete = itemView.findViewById(R.id.CardDelete);

        }
    }
}
