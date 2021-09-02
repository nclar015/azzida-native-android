package com.azzida.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.CategoryModel;
import com.azzida.model.GetJobCategoryModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CategoryAdpater extends
        RecyclerView.Adapter<CategoryAdpater.ViewHolder> {

    private final Context _ctx;
    private final ClickView clickView;
    public ArrayList<GetJobCategoryModelDatum> recentModelData;
    public ArrayList<String> List;
    public CategoryModel categoryModels;


    public CategoryAdpater(Context _ctx, ClickView clickView, ArrayList<GetJobCategoryModelDatum> recentModelData) {
        this._ctx = _ctx;
        this.recentModelData = recentModelData;
        this.clickView = clickView;
        List = new ArrayList<String>();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.item_job_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

            holder.checkBox1.setText(recentModelData.get(position).getCategoryName());
            holder.checkBox1.setChecked(false);


            String Catag = AppPrefs.getStringKeyvaluePrefs(_ctx, AppPrefs.KEY_CategoryList);

            if (Catag != null && Catag.length() > 0){

                String[] elements = AppPrefs.getStringKeyvaluePrefs(_ctx, AppPrefs.KEY_CategoryList).split(",");

                List<String> JobLisr = Arrays.asList(elements);

                for (int i = 0; i < JobLisr.size(); i++) {


                    if (recentModelData.get(position).getCategoryName().equalsIgnoreCase(JobLisr.get(i).trim())){


                        holder.checkBox1.setChecked(true);
                        List.add(recentModelData.get(position).getCategoryName());


                    }

                }

            }

            holder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        List.add(holder.checkBox1.getText().toString());
                        String idList = List.toString().replace("[", "").replace("]", "");
                        AppPrefs.setStringKeyvaluePrefs(_ctx, AppPrefs.KEY_CategoryList, idList);


                        Log.e("TAGMai", idList);
                        Log.e("TAG", String.valueOf(List));
                    } else {
                        List.remove(holder.checkBox1.getText().toString());
                        String idList = List.toString().replace("[", "").replace("]", "");
                        AppPrefs.setStringKeyvaluePrefs(_ctx, AppPrefs.KEY_CategoryList, idList);


                        Log.e("TAGMai", idList);
                        Log.e("TAG", String.valueOf(List));
                    }

                }
            });

          /*  viewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickView.clickitem(view, position);
                }
            });*/
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

    public void updatedata(ArrayList<GetJobCategoryModelDatum> recentModelData) {
        this.recentModelData = recentModelData;
        notifyDataSetChanged();

    }


    public interface ClickView {
        void clickitem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox1;


        public ViewHolder(View itemView) {
            super(itemView);
            checkBox1 = itemView.findViewById(R.id.checkBox1);


        }
    }
}
