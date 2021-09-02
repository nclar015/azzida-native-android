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
import com.azzida.model.SeekerCompletedJobModelDatum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SeekerCompletedJobAdpater extends
        RecyclerView.Adapter<SeekerCompletedJobAdpater.ViewHolder> {

    public ArrayList<SeekerCompletedJobModelDatum> recentModelData;
    private Context _ctx;
    private ClickView clickView;


    public SeekerCompletedJobAdpater(Context _ctx, ClickView clickView, ArrayList<SeekerCompletedJobModelDatum> recentModelData) {
        this._ctx = _ctx;
        this.recentModelData = recentModelData;
        this.clickView = clickView;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job_completed, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder viewHolder = holder;

        try {

            viewHolder.title.setText(recentModelData.get(position).getJobTitle());

            String roundedBalance = recentModelData.get(position).getAmount().replaceAll("\\.0*$", "");


            viewHolder.txt_Amount.setText("$" + roundedBalance);

            String CompletedDate = String.valueOf(DateFormat.format("MM/dd/yyyy", Long.parseLong(recentModelData.get(position).getCompletedDate())));


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String CurrentDate = df.format(c);

            String Days =getCountOfDays(CurrentDate,CompletedDate);

            viewHolder.txt_JobCompletDate.setText("Jobs Performed" + CompletedDate);



            viewHolder.card_view_Recent.setOnClickListener(new View.OnClickListener() {
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
    public int getItemViewType(int position)
    {
        return position;
    }

    public void updatedata(ArrayList<SeekerCompletedJobModelDatum> recentModelData) {
        this.recentModelData = recentModelData;
        notifyDataSetChanged();


    }


    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return String.valueOf((dayCount));
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
