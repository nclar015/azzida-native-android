package com.azzida.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.azzida.adapter.ViewPaymentTransactionAdpater;
import com.azzida.model.ViewPaymentTransactionModel;
import com.azzida.model.ViewPaymentTransactionModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public ArrayList<ViewPaymentTransactionModelDatum> viewPaymentTransactionModelData;
    TextView title;
    ImageView profile_back;
    ArrayList<String> items;
    private RecyclerView Recycler_Transaction;
    private ViewPaymentTransactionAdpater viewPaymentTransactionAdpater;
    private LinearLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        InitView();
    }

    private void InitView() {

        viewPaymentTransactionModelData = new ArrayList<ViewPaymentTransactionModelDatum>();

        title = findViewById(R.id.title);

        title.setText("Back to Profile");

        profile_back = findViewById(R.id.profile_back);

        noData = findViewById(R.id.noData);

        Recycler_Transaction = findViewById(R.id.Recycler_Transaction);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        Recycler_Transaction.setLayoutManager(mLayoutManager);
        Recycler_Transaction.setHasFixedSize(true);
        Recycler_Transaction.setItemAnimator(new DefaultItemAnimator());


        profile_back.setOnClickListener(this);


        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getTransactionDetails();

        }

    }

    private void getTransactionDetails() {

        Utils.showProgressdialog(TransactionDetailsActivity.this, "Please Wait....");

        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(TransactionDetailsActivity.this, AppPrefs.KEY_User_ID);
        Call<ViewPaymentTransactionModel> call = service.ViewPaymentTransaction(s_userId);
        call.enqueue(new Callback<ViewPaymentTransactionModel>() {


            @Override
            public void onResponse(@NonNull Call<ViewPaymentTransactionModel> call, @NonNull Response<ViewPaymentTransactionModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ViewPaymentTransactionModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {


                            if (viewPaymentTransactionModelData != null && viewPaymentTransactionModelData.size() > 0 && viewPaymentTransactionAdpater != null) {

                                viewPaymentTransactionModelData.clear();
                                viewPaymentTransactionAdpater.notifyDataSetChanged();
                            }

                            /*viewPaymentTransactionModelData = result.getData();*/

                            for (int i = 0; i < result.getData().size(); i++) {

                                if ((result.getData().get(i).getListerPaymentDone() || result.getData().get(i).getSeekerPaymentDone())) {

                                    if (result.getData().get(i).getReceivedFrom() != null) {

                                        if (result.getData().get(i).getSeekerPaymentDone()) {

                                            viewPaymentTransactionModelData.add(result.getData().get(i));

                                        }

                                    } else {

                                        viewPaymentTransactionModelData.add(result.getData().get(i));

                                    }


                                }


                            }

                            viewPaymentTransactionAdpater = new ViewPaymentTransactionAdpater(TransactionDetailsActivity.this, new ViewPaymentTransactionAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    AlertDialog.Builder b = new AlertDialog.Builder(TransactionDetailsActivity.this);

                                    RelativeLayout Rview = (RelativeLayout) TransactionDetailsActivity.this.getLayoutInflater().inflate(R.layout.dialog_transaction_detail, null);

                                    b.setView(Rview);

                                    ImageView img_close = Rview.findViewById(R.id.img_close);
                                    TextView JobTitle = Rview.findViewById(R.id.JobTitle);
                                    TextView JobsPosted = Rview.findViewById(R.id.JobsPosted);
                                    TextView PerformerNames = Rview.findViewById(R.id.PerformerNames);
                                    TextView txt_Date = Rview.findViewById(R.id.txt_Date);
                                    TextView DateCompletion = Rview.findViewById(R.id.DateCompletion);
                                    TextView PaymentType = Rview.findViewById(R.id.PaymentType);

                                    TextView txt_JobAmount = Rview.findViewById(R.id.txt_JobAmount);
                                    TextView txt_FeesPaid = Rview.findViewById(R.id.txt_FeesPaid);
                                    TextView txt_Total = Rview.findViewById(R.id.txt_Total);

                                    txt_JobAmount.setText("$" + viewPaymentTransactionModelData.get(position).getJobAmount());

                                    if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("payment")) {


                                        if (viewPaymentTransactionModelData.get(position).getReceivedFrom() != null) {

                                            double JobAmount = Double.parseDouble(viewPaymentTransactionModelData.get(position).getJobAmount());
                                            double SeekerAmount = Double.parseDouble(viewPaymentTransactionModelData.get(position).getSeekerPaymentAmount());
                                            double FeeAmount = JobAmount - SeekerAmount;

                                            String Amount = new DecimalFormat("0.00").format(FeeAmount);

                                            txt_FeesPaid.setText("$" + Amount);
                                            txt_Total.setText("$" + new DecimalFormat("0.00").format(SeekerAmount));


                                        } else {

                                            double JobAmount = Double.parseDouble(viewPaymentTransactionModelData.get(position).getJobAmount());
                                            double TotalAmount = Double.parseDouble(viewPaymentTransactionModelData.get(position).getTotalAmount());
                                            double FeeAmount = TotalAmount - JobAmount;

                                            String Amount = new DecimalFormat("0.00").format(FeeAmount);

                                            txt_FeesPaid.setText("$" + Amount);
                                            txt_Total.setText("$" + new DecimalFormat("0.00").format(TotalAmount));

                                        }

                                    } else {

                                        double TotalAmount = Double.parseDouble(viewPaymentTransactionModelData.get(position).getTotalAmount());
                                        txt_FeesPaid.setText("$0");
                                        txt_Total.setText("$" + new DecimalFormat("0.00").format(TotalAmount));


                                    }


                                    if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Checker")) {

                                        JobTitle.setText("Azzida Check");


                                    } else {

                                        JobTitle.setText(viewPaymentTransactionModelData.get(position).getJobTitle());

                                    }


                                    if (viewPaymentTransactionModelData.get(position).getListerId() != null) {

                                        String s_firstName = AppPrefs.getStringKeyvaluePrefs(TransactionDetailsActivity.this, AppPrefs.KEY_User_FirstName);
                                        String s_lastName = AppPrefs.getStringKeyvaluePrefs(TransactionDetailsActivity.this, AppPrefs.KEY_User_LastName);

                                        JobsPosted.setText(s_firstName + " " + s_lastName);

                                        PerformerNames.setText(viewPaymentTransactionModelData.get(position).getToName());


                                        if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("payment")) {

                                            PaymentType.setText("Job Payment");

                                        } else {

                                            PaymentType.setText(viewPaymentTransactionModelData.get(position).getPaymentType());

                                        }

                                    } else {

                                        String s_firstName = AppPrefs.getStringKeyvaluePrefs(TransactionDetailsActivity.this, AppPrefs.KEY_User_FirstName);
                                        String s_lastName = AppPrefs.getStringKeyvaluePrefs(TransactionDetailsActivity.this, AppPrefs.KEY_User_LastName);


                                        JobsPosted.setText(viewPaymentTransactionModelData.get(position).getSenderName());

                                        PerformerNames.setText(s_firstName + " " + s_lastName);

                                        if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("payment")) {

                                            PaymentType.setText("Job Payment");

                                        } else {

                                            PaymentType.setText(viewPaymentTransactionModelData.get(position).getPaymentType());

                                        }


                                    }

                                    if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Dispute")) {

                                        txt_Date.setText("Date of dispute");

                                        String Date = viewPaymentTransactionModelData.get(position).getCreatedDate().replace("/Date(", "")
                                                .replace(")/", "");

                                        DateCompletion.setText(DateFormat.format("MMM dd, yyyy", Long.parseLong(Date)).toString());


                                    } else if (viewPaymentTransactionModelData.get(position).getPaymentType().equalsIgnoreCase("Checker")) {

                                        txt_Date.setText("Date of Azzida Check");

                                        String Date = viewPaymentTransactionModelData.get(position).getCreatedDate().replace("/Date(", "")
                                                .replace(")/", "");

                                        DateCompletion.setText(DateFormat.format("MMM dd, yyyy", Long.parseLong(Date)).toString());


                                    } else {

                                        txt_Date.setText("Date of job completion");

                                        String Date = viewPaymentTransactionModelData.get(position).getCreatedDate().replace("/Date(", "")
                                                .replace(")/", "");

                                        DateCompletion.setText(DateFormat.format("MMM dd, yyyy", Long.parseLong(Date)).toString());


                                    }


                                    AlertDialog Rviews = b.create();

                                    Rviews.setCanceledOnTouchOutside(false);
                                    Rviews.setCancelable(true);
                                    Rviews.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Rviews.show();

                                    img_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Rviews.cancel();
                                        }
                                    });


                                }
                            }, viewPaymentTransactionModelData);

                            Recycler_Transaction.setAdapter(viewPaymentTransactionAdpater);


                            if (!(result.getData().size() > 0)) {

                                Recycler_Transaction.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                Recycler_Transaction.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);
                            }

                            if (viewPaymentTransactionModelData == null) {

                                Recycler_Transaction.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);

                            } else {

                                Recycler_Transaction.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);

                            }


                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetailsActivity.this);
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
                    Toast.makeText(TransactionDetailsActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViewPaymentTransactionModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                finish();

                break;


        }
    }


}
