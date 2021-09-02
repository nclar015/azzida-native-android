package com.azzida.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.azzida.adapter.SeekerCompletedJobAdpater;
import com.azzida.model.SeekerCompletedJobModel;
import com.azzida.model.SeekerCompletedJobModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobCompletedFragment extends Fragment {

    View rootView;

    private RecyclerView Recycler_CompJob;
    private LinearLayout noData;

    private SeekerCompletedJobAdpater recentAdpater;
    private ArrayList<SeekerCompletedJobModelDatum> arraymyRecent;
    private ProgressDialog progressdialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_job_completed, container, false);
        initView();
        return rootView;

    }

    private void initView() {

        noData = rootView.findViewById(R.id.noData);

        Recycler_CompJob = rootView.findViewById(R.id.Recycler_CompJob);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        Recycler_CompJob.setLayoutManager(mLayoutManager);
        Recycler_CompJob.setHasFixedSize(true);
        Recycler_CompJob.setItemAnimator(new DefaultItemAnimator());
    }


    private void getRecent(String Userid) {

        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SeekerCompletedJobModel> call = service.SeekerCompletedJob(Userid);
        call.enqueue(new Callback<SeekerCompletedJobModel>() {


            @Override
            public void onResponse(Call<SeekerCompletedJobModel> call, Response<SeekerCompletedJobModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SeekerCompletedJobModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arraymyRecent != null && arraymyRecent.size() > 0 && recentAdpater != null) {

                                arraymyRecent.clear();
                                recentAdpater.notifyDataSetChanged();
                            }

                            arraymyRecent = result.getData();

                            recentAdpater = new SeekerCompletedJobAdpater(getActivity(), new SeekerCompletedJobAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {


                                }
                            }, arraymyRecent);

                            Recycler_CompJob.setAdapter(recentAdpater);

                            if (!(result.getData().size() > 0)) {

                                Recycler_CompJob.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                Recycler_CompJob.setVisibility(View.VISIBLE);
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
            public void onFailure(@NonNull Call<SeekerCompletedJobModel> call, @NonNull Throwable t) {
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

            getRecent(s_userId);

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
