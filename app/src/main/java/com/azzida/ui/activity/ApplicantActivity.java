package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.ApplicantAdpater;
import com.azzida.model.ViewApplicantListModel;
import com.azzida.model.ViewApplicantListModelDatum;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView img_back_feed;

    CardView card_view_applicant;

    private RecyclerView RecyclerApplicant;
    private LinearLayout noData;

    private String JobId, Amount;

    private ApplicantAdpater applicantAdpater;
    private ArrayList<ViewApplicantListModelDatum> arrayApplicant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                JobId = null;
                Amount = null;
            } else {
                JobId = extras.getString("JobId");
                Amount = extras.getString("Amount");
            }
        } else {
            JobId = (String) savedInstanceState.getSerializable("JobId");
            Amount = (String) savedInstanceState.getSerializable("Amount");
        }

        initView();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getApplicant(JobId);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            setResult(2);
            finish();
        }
    }

    private void initView() {


        img_back_feed = findViewById(R.id.img_back_feed);

        noData = findViewById(R.id.noData);

        RecyclerApplicant = findViewById(R.id.RecyclerApplicant);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        RecyclerApplicant.setLayoutManager(mLayoutManager);
        RecyclerApplicant.setHasFixedSize(true);
        RecyclerApplicant.setItemAnimator(new DefaultItemAnimator());


        img_back_feed.setOnClickListener(this);


    }


    private void getApplicant(String jobId) {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ViewApplicantListModel> call = service.ViewApplicantList(jobId);
        call.enqueue(new Callback<ViewApplicantListModel>() {


            @Override
            public void onResponse(@NonNull Call<ViewApplicantListModel> call, @NonNull Response<ViewApplicantListModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ViewApplicantListModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arrayApplicant != null && arrayApplicant.size() > 0 && applicantAdpater != null) {

                                arrayApplicant.clear();
                                applicantAdpater.notifyDataSetChanged();
                            }

                            arrayApplicant = result.getData();

                            if (!(result.getData().size() > 0)) {

                                RecyclerApplicant.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                RecyclerApplicant.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);
                            }

                            applicantAdpater = new ApplicantAdpater(ApplicantActivity.this, new ApplicantAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    Intent myIntent = new Intent(ApplicantActivity.this, ApplicantlDetailActivity.class);
                                    myIntent.putExtra("SeekerId", arrayApplicant.get(position).getSeekerId());
                                    myIntent.putExtra("ListerId", arrayApplicant.get(position).getListerId());
                                    myIntent.putExtra("JobId", JobId);
                                    myIntent.putExtra("Amount", Amount);
                                    startActivityForResult(myIntent, 2);


                                }
                            }, arrayApplicant);

                            RecyclerApplicant.setAdapter(applicantAdpater);

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantActivity.this);
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
                    Toast.makeText(ApplicantActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViewApplicantListModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back_feed:

                finish();


                break;
        }

    }


}
