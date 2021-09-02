package com.azzida.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.MyJobAdpater;
import com.azzida.model.MyJobModel;
import com.azzida.model.MyJobModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.ui.activity.AddFeedActivity;
import com.azzida.ui.activity.JobListerDetailActivity;
import com.azzida.ui.activity.JobSeekerApplyActivity;
import com.azzida.ui.activity.LoginActivity;
import com.azzida.utills.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSeekerFragment extends Fragment {

    private View rootView;
    private MyJobAdpater myJobAdpater;
    private ArrayList<MyJobModelDatum> arraymyJob;
    private RecyclerView Recycler_MyJob;
    private LinearLayout noData;
    private LinearLayout addFeed;
    private double latitude, longitude;
    private ProgressDialog progressdialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_job_seeker, container, false);
        initView();

        return rootView;

    }

    private void initView() {

        progressdialog = new ProgressDialog(getActivity());

        addFeed = rootView.findViewById(R.id.addFeed);

        noData = rootView.findViewById(R.id.noData);

        Recycler_MyJob = rootView.findViewById(R.id.Recycler_MyJob);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        Recycler_MyJob.setLayoutManager(mLayoutManager);
        Recycler_MyJob.setHasFixedSize(true);
        Recycler_MyJob.setItemAnimator(new DefaultItemAnimator());


        addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPrefs.getStringKeyvaluePrefs(getActivity(),
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent myIntent = new Intent(getActivity(), AddFeedActivity.class);
                    startActivity(myIntent);

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                Intent intent = new Intent(getActivity(),
                                        LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });

    }

    private void setMyJob() {
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_User_ID);
        String CategoryList;


        CategoryList = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_CategoryList);

        Log.d("CategoryList", CategoryList);

        double Dis = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_Distance)) * 1609.344;

        String Distance = String.valueOf(Dis).replaceAll("\\.0*$", "");

        String PriceMin = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_PriceMin);
        String PriceMax = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_PriceMax);
        String swich = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_Switch);
        String swichA = "";
        if (AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_Switch_Active).trim().isEmpty()) {
            swichA = "false";
        } else {
            swichA = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_Switch_Active);
        }

        if (!(latitude > 0.0)) {
            latitude = 37.545677;
        }

        if (!(longitude > 0.0)) {
            longitude = -77.664465;
        }

        Call<MyJobModel> call = service.MyJob(s_userId, CategoryList, Distance, String.valueOf(latitude), String.valueOf(longitude), PriceMin, PriceMax, swich, swichA);
        call.enqueue(new Callback<MyJobModel>() {


            @Override
            public void onResponse(@NonNull Call<MyJobModel> call, @NonNull Response<MyJobModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    MyJobModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arraymyJob != null && arraymyJob.size() > 0 && myJobAdpater != null) {

                                arraymyJob.clear();
                                myJobAdpater.notifyDataSetChanged();
                            }

                            arraymyJob = result.getData();

                            myJobAdpater = new MyJobAdpater(getActivity(), new MyJobAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    String s_userId = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_User_ID);
                                    if (arraymyJob.get(position).getUserId().equals(s_userId)) {

                                        Intent myIntent = new Intent(getActivity(), JobListerDetailActivity.class);
                                        myIntent.putExtra("JobId", arraymyJob.get(position).getId());
                                        myIntent.putExtra("ScreenName", "Back To Feed");
                                        startActivity(myIntent);

                                    } else {

                                        Intent myIntent = new Intent(getActivity(), JobSeekerApplyActivity.class);
                                        myIntent.putExtra("JobId", arraymyJob.get(position).getId());
                                        myIntent.putExtra("ScreenName", "Back to My Feed");
                                        startActivity(myIntent);

                                    }


                                }
                            }, arraymyJob, latitude, longitude);

                            Recycler_MyJob.setAdapter(myJobAdpater);

                            if (!(result.getData().size() > 0)) {

                                Recycler_MyJob.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                Recycler_MyJob.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    Toast.makeText(getActivity(), R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyJobModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_LAT).length() > 0) {

            latitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_LAT));

            longitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_LONG));
        }

        String status = NetworkUtils.getConnectivityStatus(getActivity());
        if (status.equals("404")) {
            Toast.makeText(getActivity(), R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            setMyJob();

        }
    }

    @Override
    public void onDetach() {
        if (progressdialog != null && progressdialog.isShowing())
            progressdialog.dismiss();
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressdialog != null)
            progressdialog.cancel();

    }

    public void jobRefresh() {
    }

}
