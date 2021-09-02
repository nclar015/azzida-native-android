package com.azzida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.GetCustomerCardsModelDatum;

import java.util.ArrayList;


public class GetSaveCardAdpater extends
        RecyclerView.Adapter<GetSaveCardAdpater.ViewHolder> {

    public ArrayList<GetCustomerCardsModelDatum> recentModelData;
    private final Context _ctx;
    private final ClickView clickView;


    public GetSaveCardAdpater(Context _ctx, ClickView clickView, ArrayList<GetCustomerCardsModelDatum> recentModelData) {
        this._ctx = _ctx;
        this.recentModelData = recentModelData;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_savecard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.txt_CardNo.setText("Ending with " + recentModelData.get(position).getCardNumber());


            if (recentModelData.get(position).getCardType().equalsIgnoreCase("Visa")) {

                holder.img_card.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_visa));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("Mastercard")) {

                holder.img_card.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_mastercard));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("Discover")) {


                holder.img_card.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_discover));

            } else if (recentModelData.get(position).getCardType().equalsIgnoreCase("American Express")) {

                holder.img_card.setImageDrawable(ContextCompat.getDrawable(_ctx, R.drawable.ic_amex));

            } else {

                holder.img_card.setImageResource(android.R.color.transparent);

            }


            holder.card_view_GetSaveCard.setOnClickListener(new View.OnClickListener() {
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
        public TextView txt_CardNo;
        public CardView card_view_GetSaveCard;
        public ImageView img_card;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_CardNo = itemView.findViewById(R.id.txt_CardNo);
            img_card = itemView.findViewById(R.id.img_card);
            card_view_GetSaveCard = itemView.findViewById(R.id.card_view_GetSaveCard);


        }
    }
}
