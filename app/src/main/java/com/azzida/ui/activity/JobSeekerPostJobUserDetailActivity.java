package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.JobRecentDetailAdpater;
import com.azzida.model.GetrecentactivityModel;
import com.azzida.model.ViewListerUserModel;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSeekerPostJobUserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    String ListerId;
    private RecyclerView Recycler_JobListerDetail;
    private LinearLayout noData, LayoutRating;

    private CircleImageView iv_profile_image;
    private ArrayList<GetrecentactivityModel> arraymyJob;
    private JobRecentDetailAdpater jobRecentDetailAdpater;
    private ImageView img_Azzida_Verifid;

    private RatingBar RateAVg;

    private TextView txt_ListerName, txt_Join_Date, txt_JobsListed, txt_JobsCompleted, txt_Reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posted_job);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                ListerId = null;
            } else {
                ListerId = extras.getString("ListerId");
            }
        } else {
            ListerId = (String) savedInstanceState.getSerializable("ListerId");
        }

        initView();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getListerDetail(ListerId);

        }


    }


    private void initView() {


        img_back = findViewById(R.id.img_back);

        noData = findViewById(R.id.noData);

        Recycler_JobListerDetail = findViewById(R.id.Recycler_JobListerDetail);

        img_Azzida_Verifid = findViewById(R.id.img_Azzida_Verifid);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        Recycler_JobListerDetail.setLayoutManager(mLayoutManager);
        Recycler_JobListerDetail.setHasFixedSize(true);
        Recycler_JobListerDetail.setItemAnimator(new DefaultItemAnimator());

        iv_profile_image = findViewById(R.id.iv_profile_image);

        txt_ListerName = findViewById(R.id.txt_ListerName);
        txt_Join_Date = findViewById(R.id.txt_Join_Date);
        txt_JobsListed = findViewById(R.id.txt_JobsListed);
        txt_JobsCompleted = findViewById(R.id.txt_JobsCompleted);

        txt_Reviews = findViewById(R.id.txt_Reviews);
        RateAVg = findViewById(R.id.RateAVg);
        LayoutRating = findViewById(R.id.LayoutRating);


        img_back.setOnClickListener(this);


    }


    private void getListerDetail(String listerId) {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerPostJobUserDetailActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ViewListerUserModel> call = service.ViewListerUser(listerId);
        call.enqueue(new Callback<ViewListerUserModel>() {


            @Override
            public void onResponse(@NonNull Call<ViewListerUserModel> call, @NonNull Response<ViewListerUserModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ViewListerUserModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getListerId() != null) {


                                txt_ListerName.setText(result.getData().getName());
                                txt_JobsCompleted.setText(result.getData().getJobCompleteCount());
                                txt_JobsListed.setText(result.getData().getJobPostingcount());

                                if (result.getData().getJoinDate() != null) {

                                    String Date = result.getData().getJoinDate().replace("/Date(", "")
                                            .replace(")/", "");
                                    txt_Join_Date.setText("Joined Azzida: " + DateFormat.format("MMM dd, yyyy", Long.parseLong(Date)).toString());

                                }

                                if (!result.getData().getRateAvg().equals("0")) {

                                    RateAVg.setRating(Float.parseFloat(result.getData().getRateAvg()));
                                    txt_Reviews.setText("(" + result.getData().getRatingUserCount() + ")");
                                    LayoutRating.setVisibility(View.VISIBLE);

                                } else {

                                    LayoutRating.setVisibility(View.GONE);

                                }

/*

                                if (result.getData().getNationalStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getGlobalStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getSexOffenderStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getSsnTraceStatus().equalsIgnoreCase("clear")) {


                                    img_Azzida_Verifid.setVisibility(View.GONE);

                                } else {

                                    img_Azzida_Verifid.setVisibility(View.GONE);
                                    
                                }
*/


                                Picasso.get().load(result.getData().getProfilePicture())
                                        .placeholder(R.drawable.no_profile)
                                        .error(R.drawable.no_profile)
                                        .into(iv_profile_image);

                                if (arraymyJob != null && arraymyJob.size() > 0 && jobRecentDetailAdpater != null) {

                                    arraymyJob.clear();
                                    jobRecentDetailAdpater.notifyDataSetChanged();
                                }

                                arraymyJob = result.getData().getGetrecentactivity();

                                jobRecentDetailAdpater = new JobRecentDetailAdpater(JobSeekerPostJobUserDetailActivity.this, new JobRecentDetailAdpater.ClickView() {
                                    @Override
                                    public void clickitem(View view, final int position) {

                                      /*  Intent myIntent = new Intent(JobSeekerPostJobUserDetailActivity.this, JobSeekerApplyActivity.class);
                                        myIntent.putExtra("JobId", arraymyJob.get(position).getId());
                                        startActivity(myIntent);*/

                                    }
                                }, arraymyJob);

                                Recycler_JobListerDetail.setAdapter(jobRecentDetailAdpater);

                                if (!(result.getData().getGetrecentactivity().size() > 0)) {

                                    Recycler_JobListerDetail.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                } else {

                                    Recycler_JobListerDetail.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);
                                }

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerPostJobUserDetailActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();
                    }

                } else {
                    // Server Problem
                    Toast.makeText(JobSeekerPostJobUserDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViewListerUserModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back:

                finish();


                break;


        }

    }


}
