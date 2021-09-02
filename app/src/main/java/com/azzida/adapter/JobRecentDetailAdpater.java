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
import com.azzida.model.GetrecentactivityModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class JobRecentDetailAdpater extends
        RecyclerView.Adapter<JobRecentDetailAdpater.ViewHolder> {

    public ArrayList<GetrecentactivityModel> getrecentactivityModels;
    private final Context _ctx;
    private final ClickView clickView;


    public JobRecentDetailAdpater(Context _ctx, ClickView clickView, ArrayList<GetrecentactivityModel> getrecentactivityModels) {
        this._ctx = _ctx;
        this.getrecentactivityModels = getrecentactivityModels;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.title.setText(getrecentactivityModels.get(position).getJobTitle());

            String roundedBalance = getrecentactivityModels.get(position).getAmount().replaceAll("\\.0*$", "");


            holder.txt_Amount.setText("$" + roundedBalance);

            String CompletedDate = String.valueOf(DateFormat.format("dd/MM/yyyy", Long.parseLong(getrecentactivityModels.get(position).getCompletedDate())));


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            String CurrentDate = df.format(c);

            String Days = getCountOfDays(CurrentDate, CompletedDate);

            holder.txt_JobCompletDate.setText("Jobs Performed " + Days.replace("-", "").replaceAll("\\.0*$", "")
                    + " days ago");


           /* viewHolder.card_view_Recent_Job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickView.clickitem(view, position);
                }
            });
*/
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    @Override
    public int getItemCount() {
        return getrecentactivityModels == null ? 0 : getrecentactivityModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updatedata(ArrayList<GetrecentactivityModel> getrecentactivityModels) {
        this.getrecentactivityModels = getrecentactivityModels;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txt_Amount, txt_JobCompletDate;
        public CardView card_view_Recent_Job;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_JobCompletDate = itemView.findViewById(R.id.txt_JobCompletDate);
            card_view_Recent_Job = itemView.findViewById(R.id.card_view_Recent_Job);


        }
    }
}
