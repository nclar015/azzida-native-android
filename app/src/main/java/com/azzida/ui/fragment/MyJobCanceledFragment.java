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
import com.azzida.adapter.SeekerCanceledJobAdpater;
import com.azzida.model.SeekerJobCancelledListModel;
import com.azzida.model.SeekerJobCancelledListModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.ui.activity.JobSeekerNotSelectedActivity;
import com.azzida.utills.NetworkUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobCanceledFragment extends Fragment {

    View rootView;
    private RecyclerView Recycler_CanceledJobSeeker;
    private LinearLayout noData;
    private ProgressDialog progressdialog;
    private SeekerCanceledJobAdpater seekerCanceledJobAdpater;
    private ArrayList<SeekerJobCancelledListModelDatum> arraymyActiveJob;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_job_canceled, container, false);
        initView();
        return rootView;

    }

    private void initView() {

        progressdialog = new ProgressDialog(getActivity());


        noData = rootView.findViewById(R.id.noData);

        Recycler_CanceledJobSeeker = rootView.findViewById(R.id.Recycler_CanceledJobSeeker);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        Recycler_CanceledJobSeeker.setLayoutManager(mLayoutManager);
        Recycler_CanceledJobSeeker.setHasFixedSize(true);
        Recycler_CanceledJobSeeker.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onResume() {
        super.onResume();

        String status = NetworkUtils.getConnectivityStatus(getActivity());
        if (status.equals("404")) {
            Toast.makeText(getActivity(), R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            String s_userId = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_User_ID);

            getListerCanceledJob(s_userId);

        }
    }

    private void getListerCanceledJob(String s_userId) {

        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SeekerJobCancelledListModel> call = service.SeekerJobCancelledList(s_userId);
        call.enqueue(new Callback<SeekerJobCancelledListModel>() {


            @Override
            public void onResponse(@NonNull Call<SeekerJobCancelledListModel> call, @NonNull Response<SeekerJobCancelledListModel> response) {
                try {

                    progressdialog.dismiss();
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        SeekerJobCancelledListModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("1")) {
                            if (result.getData() != null) {

                                if (arraymyActiveJob != null && arraymyActiveJob.size() > 0 && seekerCanceledJobAdpater != null) {

                                    arraymyActiveJob.clear();
                                    seekerCanceledJobAdpater.notifyDataSetChanged();
                                }

                                arraymyActiveJob = result.getData();

                                seekerCanceledJobAdpater = new SeekerCanceledJobAdpater(getActivity(), new SeekerCanceledJobAdpater.ClickView() {
                                    @Override
                                    public void clickitem(View view, final int position) {

                                        Intent myIntent = new Intent(getActivity(), JobSeekerNotSelectedActivity.class);
                                        myIntent.putExtra("JobId", arraymyActiveJob.get(position).getJobId());
                                        startActivity(myIntent);

                                    }
                                }, arraymyActiveJob);

                                Recycler_CanceledJobSeeker.setAdapter(seekerCanceledJobAdpater);

                                if (!(result.getData().size() > 0)) {

                                    Recycler_CanceledJobSeeker.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                } else {

                                    Recycler_CanceledJobSeeker.setVisibility(View.VISIBLE);
                                    noData.setVisibility(View.GONE);
                                }


                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
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

                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                }

            }

            @Override
            public void onFailure(@NonNull Call<SeekerJobCancelledListModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

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
}
