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
import com.azzida.model.ViewApplicantListModelDatum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ApplicantAdpater extends
        RecyclerView.Adapter<ApplicantAdpater.ViewHolder> {

    private ArrayList<ViewApplicantListModelDatum> arrayApplicant;
    private final Context _ctx;
    private final ClickView clickView;


    public ApplicantAdpater(Context _ctx, ClickView clickView, ArrayList<ViewApplicantListModelDatum> arrayApplicant) {
        this._ctx = _ctx;
        this.arrayApplicant = arrayApplicant;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_view_applicants, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.txt_FullName.setText(arrayApplicant.get(position).getFirstName() + " " + arrayApplicant.get(position).getLastName());

            holder.txt_JobCompleted.setText(arrayApplicant.get(position).getJobCompleteCount() + " " + "Jobs" + " " + "Performed");

            Picasso.get().load(arrayApplicant.get(position).getProfilePicture())
                    .placeholder(R.drawable.no_profile)
                    .error(R.drawable.no_profile)
                    .into(holder.iv_profile_image);

            holder.card_view_applicant.setOnClickListener(new View.OnClickListener() {
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
        return arrayApplicant == null ? 0 : arrayApplicant.size();
    }

    public void updatedata(ArrayList<ViewApplicantListModelDatum> arrayApplicant) {
        this.arrayApplicant = arrayApplicant;
        notifyDataSetChanged();


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_FullName, txt_JobCompleted;
        public CardView card_view_applicant;
        public CircleImageView iv_profile_image;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_FullName = itemView.findViewById(R.id.txt_FullName);
            txt_JobCompleted = itemView.findViewById(R.id.txt_JobCompleted);
            card_view_applicant = itemView.findViewById(R.id.card_view_applicant);
            iv_profile_image = itemView.findViewById(R.id.iv_profile_image);


        }
    }
}
