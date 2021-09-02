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
import com.azzida.adapter.SeekerInprogressJobAdpater;
import com.azzida.model.SeekerInprogressJobModel;
import com.azzida.model.SeekerInprogressJobModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.ui.activity.JobSeekerApplyActivity;
import com.azzida.utills.NetworkUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobInProgressFragment extends Fragment {

    View rootView;
    private RecyclerView Recycler_InProgress;
    private LinearLayout noData;

    private SeekerInprogressJobAdpater seekerInprogressJobAdpater;
    private ArrayList<SeekerInprogressJobModelDatum> arraySeekerInprogress;


    private ProgressDialog progressdialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_job_inprogress, container, false);
        initView();
        return rootView;

    }

    private void initView() {

        progressdialog = new ProgressDialog(getActivity());


        noData = rootView.findViewById(R.id.noData);

        Recycler_InProgress = rootView.findViewById(R.id.Recycler_InProgress);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        Recycler_InProgress.setLayoutManager(mLayoutManager);
        Recycler_InProgress.setHasFixedSize(true);
        Recycler_InProgress.setItemAnimator(new DefaultItemAnimator());


    }

    private void getActiveJob(String s_userId) {

        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SeekerInprogressJobModel> call = service.SeekerInprogressJob(s_userId);
        call.enqueue(new Callback<SeekerInprogressJobModel>() {


            @Override
            public void onResponse(@NonNull Call<SeekerInprogressJobModel> call, @NonNull Response<SeekerInprogressJobModel> response) {
                try {

                    progressdialog.dismiss();
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        SeekerInprogressJobModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("1")) {
                            if (result.getData() != null) {

                                if (arraySeekerInprogress != null && arraySeekerInprogress.size() > 0 && seekerInprogressJobAdpater != null) {

                                    arraySeekerInprogress.clear();
                                    seekerInprogressJobAdpater.notifyDataSetChanged();
                                }

                                arraySeekerInprogress = result.getData();

                                seekerInprogressJobAdpater = new SeekerInprogressJobAdpater(getActivity(), new SeekerInprogressJobAdpater.ClickView() {
                                    @Override
                                    public void clickitem(View view, final int position) {

                                        Intent myIntent = new Intent(getActivity(), JobSeekerApplyActivity.class);
                                        myIntent.putExtra("JobId", arraySeekerInprogress.get(position).getJobId());
                                        myIntent.putExtra("ScreenName", "Back to My Job");
                                        startActivity(myIntent);

                                    }
                                }, arraySeekerInprogress);

                                Recycler_InProgress.setAdapter(seekerInprogressJobAdpater);

                                if (!(result.getData().size() > 0)) {

                                    Recycler_InProgress.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                } else {

                                    Recycler_InProgress.setVisibility(View.VISIBLE);
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
            public void onFailure(@NonNull Call<SeekerInprogressJobModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

        String status = NetworkUtils.getConnectivityStatus(getActivity());
        if (status.equals("404")) {
            Toast.makeText(getActivity(), R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            String s_userId = AppPrefs.getStringKeyvaluePrefs(getActivity(), AppPrefs.KEY_User_ID);

            getActiveJob(s_userId);

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

}
