package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.GetSaveCardAdpater;
import com.azzida.helper.Config;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.model.TipModel;
import com.azzida.model.UserModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_job_back;

    String SeekerId, Amount, JobId, TippingAmount, AmountPay, AmtRef, paymentId, TipId;

    CircleImageView iv_profile_image;

    TextView SeekerName, txt_JobCost, txt_JobTotal;

    TextView txt_5, txt_10, txt_20, txt_CustomAmt;

    LinearLayout tip_5, tip_10, tip_20, Done;

    Boolean t5 = false;
    Boolean t10 = false;
    Boolean t20 = false;
    RecyclerView RecyclerPayment;
    AlertDialog Card;
    RatingBar mRatingBarSeeker;
    private GetSaveCardAdpater getSaveCardAdpater;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_complete);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                SeekerId = null;
                Amount = null;
                JobId = null;
                paymentId = null;
                TipId = null;

            } else {
                SeekerId = extras.getString("SeekerId");
                Amount = extras.getString("Amount");
                JobId = extras.getString("JobId");
                paymentId = extras.getString("paymentId");
                TipId = extras.getString("TipId");

                DataManager.getInstance().setSeekerId(SeekerId);
                DataManager.getInstance().setAmount(Amount);
                DataManager.getInstance().setJobID(JobId);
                DataManager.getInstance().setPaymentId(paymentId);
                DataManager.getInstance().setTipId(TipId);

            }
        } else {
            SeekerId = (String) savedInstanceState.getSerializable("SeekerId");
            Amount = (String) savedInstanceState.getSerializable("Amount");
            JobId = (String) savedInstanceState.getSerializable("JobId");
            TipId = (String) savedInstanceState.getSerializable("TipId");
            paymentId = (String) savedInstanceState.getSerializable("paymentId");
        }

        initView();
    }


    private void initView() {

        img_job_back = findViewById(R.id.img_job_back);

        TippingAmount = "0";

        iv_profile_image = findViewById(R.id.iv_profile_image);
        SeekerName = findViewById(R.id.SeekerName);
        txt_JobCost = findViewById(R.id.txt_JobCost);
        txt_JobTotal = findViewById(R.id.txt_JobTotal);

        txt_CustomAmt = findViewById(R.id.txt_CustomAmt);

        txt_5 = findViewById(R.id.txt_5);
        txt_10 = findViewById(R.id.txt_10);
        txt_20 = findViewById(R.id.txt_20);

        tip_5 = findViewById(R.id.tip_5);
        tip_10 = findViewById(R.id.tip_10);
        tip_20 = findViewById(R.id.tip_20);
        Done = findViewById(R.id.Done);

        mRatingBarSeeker = findViewById(R.id.mRatingBarSeeker);

        img_job_back.setOnClickListener(this);

        txt_CustomAmt.setOnClickListener(this);

        tip_5.setOnClickListener(this);
        tip_10.setOnClickListener(this);
        tip_20.setOnClickListener(this);

        Done.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            setResult(2);
            finish();
        }
    }

    private void getUserDetail(String SeekerId) {
        final ProgressDialog progressdialog = new ProgressDialog(JobCompleteActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<UserModel> call = service.GetUser(SeekerId);
        call.enqueue(new Callback<UserModel>() {


            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    UserModel result = response.body();
                    Log.d("fgh", response.toString());
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                try {

                                    if (result.getData().getProfilePicture().length() > 0) {

                                        Picasso.get().load(result.getData().getProfilePicture())
                                                .placeholder(R.drawable.no_profile)
                                                .error(R.drawable.no_profile)
                                                .into(iv_profile_image);

                                    }


                                    SeekerName.setText("How would you rate " + result.getData().getFirstName() + " " + result.getData().getLastName() + "?");
                                    txt_JobCost.setText("Your job was $" + Amount);
                                    txt_JobTotal.setText("Your job was $" + Amount);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
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
                    Toast.makeText(JobCompleteActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_job_back:

                finish();


                break;

            case R.id.txt_CustomAmt:


                AlertDialog.Builder bF = new AlertDialog.Builder(JobCompleteActivity.this);

                CardView viewbF = (CardView) JobCompleteActivity.this.getLayoutInflater().inflate(R.layout.dialog_cus_amt, null);

                Button btn_close = viewbF.findViewById(R.id.btn_close);

                Button btn_submit = viewbF.findViewById(R.id.btn_submit);

                EditText edt_Amount = viewbF.findViewById(R.id.edt_Amount);

                bF.setView(viewbF);

                final AlertDialog dbf = bF.create();

                dbf.setCanceledOnTouchOutside(false);
                dbf.setCancelable(false);
                dbf.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dbf.show();

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dbf.cancel();

                    }
                });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double t1 = Double.parseDouble(edt_Amount.getText().toString());
                        double t2 = Double.parseDouble(Amount);
                        double totalCus = t1 + t2;
                        String total = new DecimalFormat("#.00").format(totalCus);

                        TippingAmount = edt_Amount.getText().toString();

                        txt_JobTotal.setText("Your job was $" + total);

                        t5 = false;
                        t10 = false;
                        t20 = false;
                        tip_5.setBackgroundResource(R.drawable.border_card);
                        txt_5.setTextColor(ContextCompat.getColor(JobCompleteActivity.this, R.color.black));
                        txt_10.setTextColor(ContextCompat.getColor(JobCompleteActivity.this, R.color.black));
                        txt_20.setTextColor(ContextCompat.getColor(JobCompleteActivity.this, R.color.black));
                        tip_10.setBackgroundResource(R.drawable.border_card);
                        tip_20.setBackgroundResource(R.drawable.border_card);


                        dbf.cancel();
                    }
                });


                break;

            case R.id.Done:


                String status = NetworkUtils.getConnectivityStatus(this);
                if (status.equals("404")) {
                    Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {

                    if (mRatingBarSeeker.getRating() > 0) {


                        if (TippingAmount.equalsIgnoreCase("0")) {

                            setTip();

                        } else if (!TipId.equals("0")) {

                            setTip();

                        } else {

                            try {

                                String s_Balance = AppPrefs.getStringKeyvaluePrefs(JobCompleteActivity.this, AppPrefs.KEY_Balance);

                                double bal1 = Double.parseDouble(TippingAmount);
                                double bal2 = Double.parseDouble(s_Balance);

                                if (bal2 > bal1) {

                                    String Msg = "Amount debited from Referral Balance: $" + TippingAmount;

                                    AmountPay = TippingAmount;
                                    AmtRef = TippingAmount;

                                    AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
                                    builder.setMessage(Msg)
                                            .setCancelable(false)

                                            .setPositiveButton(Html.fromHtml("<font color='#3e77bb'>Yes</font>"), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();

                                                    createPayment("");

                                                }
                                            })
                                            .setNegativeButton(Html.fromHtml("<font color='#ff0000'>No</font>"), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle(getString(R.string.Alert));

                                    alert.show();

                                } else {

                                    AlertDialog.Builder b = new AlertDialog.Builder(JobCompleteActivity.this, R.style.MyCustomTheme);

                                    LinearLayout PaymentView = (LinearLayout) JobCompleteActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment, null);

                                    TextView Txt_Direct_PAy = PaymentView.findViewById(R.id.Txt_Direct_PAy);
                                    RecyclerPayment = PaymentView.findViewById(R.id.RecyclerPayment);
                                    final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                                    RecyclerPayment.setLayoutManager(mLayoutManager);
                                    RecyclerPayment.setHasFixedSize(true);
                                    RecyclerPayment.setItemAnimator(new DefaultItemAnimator());
                                    RecyclerPayment.setAdapter(getSaveCardAdpater);
                                    b.setView(PaymentView);

                                    Card = b.create();

                                    Card.setCanceledOnTouchOutside(true);
                                    Card.setCancelable(true);
                                    Card.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                                    WindowManager.LayoutParams wmlp = Card.getWindow().getAttributes();

                                    wmlp.gravity = Gravity.BOTTOM;

                                    Window window = Card.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    Card.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    Card.show();

                                    Txt_Direct_PAy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Card.cancel();
                                            String TotalAmount = txt_JobTotal.getText().toString().replace("Your job was $", "");
                                            String Rating = String.valueOf(mRatingBarSeeker.getRating());
                                            String Amount = txt_JobCost.getText().toString().replace("Your job was $", "");
                                            Intent myIntent = new Intent(JobCompleteActivity.this, PaymentCheckOut.class);
                                            myIntent.putExtra("Rating", Rating);
                                            myIntent.putExtra("Amount", Amount);
                                            myIntent.putExtra("TipAmount", TippingAmount);
                                            myIntent.putExtra("SeekerId", SeekerId);
                                            myIntent.putExtra("TotalAmount", TotalAmount);
                                            myIntent.putExtra("JobId", JobId);
                                            myIntent.putExtra("paymentId", paymentId);
                                            myIntent.putExtra("PaymentType", "Tip");
                                            myIntent.putExtra("Screen", "1");
                                            startActivityForResult(myIntent, 2);

                                        }
                                    });


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    } else {

                        Toast.makeText(this, "Give Rating", Toast.LENGTH_SHORT).show();

                    }
                }


                break;

            case R.id.tip_5:
                if (!t5) {
                    t5 = true;
                    t10 = false;
                    t20 = false;
                    tip_5.setBackgroundResource(R.drawable.blue_fill__rounded_color);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.white));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.black));
                    tip_10.setBackgroundResource(R.drawable.border_card);
                    tip_20.setBackgroundResource(R.drawable.border_card);
                    double a5 = Double.parseDouble(Amount);
                    double b5 = 5;
                    double c5 = 100;
                    double result5 = (a5 * b5) / c5;

                    TippingAmount = new DecimalFormat("#.00").format(result5);
                    String total5 = new DecimalFormat("#.00").format(result5 + a5);
                    txt_JobTotal.setText("Your job was $" + total5);
                } else {
                    t5 = false;
                    tip_5.setBackgroundResource(R.drawable.border_card);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.black));
                    tip_10.setBackgroundResource(R.drawable.border_card);
                    tip_20.setBackgroundResource(R.drawable.border_card);

                    TippingAmount = "0";
                    txt_JobTotal.setText("Your job was $" + Amount);
                }


                break;

            case R.id.tip_10:
                if (!t10) {
                    t5 = false;
                    t10 = true;
                    t20 = false;
                    tip_5.setBackgroundResource(R.drawable.border_card);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.white));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.black));
                    tip_10.setBackgroundResource(R.drawable.blue_fill__rounded_color);
                    tip_20.setBackgroundResource(R.drawable.border_card);

                    double a10 = Double.parseDouble(Amount);
                    double b10 = 10;
                    double c10 = 100;
                    double result10 = (a10 * b10) / c10;

                    TippingAmount = new DecimalFormat("#.00").format(result10);
                    String total10 = new DecimalFormat("#.00").format(result10 + a10);

                    txt_JobTotal.setText("Your job was $" + total10);

                } else {
                    t10 = false;
                    tip_5.setBackgroundResource(R.drawable.border_card);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.black));
                    tip_10.setBackgroundResource(R.drawable.border_card);
                    tip_20.setBackgroundResource(R.drawable.border_card);

                    TippingAmount = "0";
                    txt_JobTotal.setText("Your job was $" + Amount);
                }
                break;

            case R.id.tip_20:
                if (!t20) {
                    t5 = false;
                    t10 = false;
                    t20 = true;
                    tip_5.setBackgroundResource(R.drawable.border_card);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.white));
                    tip_10.setBackgroundResource(R.drawable.border_card);
                    tip_20.setBackgroundResource(R.drawable.blue_fill__rounded_color);

                    double a20 = Double.parseDouble(Amount);
                    double b20 = 20;
                    double c20 = 100;
                    double result20 = (a20 * b20) / c20;

                    TippingAmount = new DecimalFormat("#.00").format(result20);
                    String total20 = new DecimalFormat("#.00").format(result20 + a20);
                    txt_JobTotal.setText("Your job was $" + total20);
                } else {
                    t20 = false;
                    tip_5.setBackgroundResource(R.drawable.border_card);
                    txt_5.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_10.setTextColor(ContextCompat.getColor(this, R.color.black));
                    txt_20.setTextColor(ContextCompat.getColor(this, R.color.black));
                    tip_10.setBackgroundResource(R.drawable.border_card);
                    tip_20.setBackgroundResource(R.drawable.border_card);

                    TippingAmount = "0";
                    txt_JobTotal.setText("Your job was $" + Amount);
                }
                break;
        }

    }


    private void createPayment(String customerId) {

        final ProgressDialog progressdialog = new ProgressDialog(JobCompleteActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, SeekerId, AmtRef, customerId, AmountPay, "", "Tip", "", Config.Token_Used);
        call.enqueue(new Callback<CreatePaymentModel>() {

            @Override
            public void onResponse(@NonNull Call<CreatePaymentModel> call, @NonNull Response<CreatePaymentModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    CreatePaymentModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                if (result.getData().getIsSuccess()) {

                                    AppPrefs.setStringKeyvaluePrefs(JobCompleteActivity.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    Card.cancel();
                                    setTip();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
                                    builder.setMessage(R.string.PaymentFailed)
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

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
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
                    Toast.makeText(JobCompleteActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    private void setTip() {

        final ProgressDialog progressdialog = new ProgressDialog(JobCompleteActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        String TotalAmount = txt_JobTotal.getText().toString().replace("Your job was $", "");
        String Rating = String.valueOf(mRatingBarSeeker.getRating());
        Call<TipModel> call = service.Tip(TipId, s_userId, JobId, TippingAmount, TotalAmount, SeekerId, Rating, paymentId);
        call.enqueue(new Callback<TipModel>() {


            @Override
            public void onResponse(@NonNull Call<TipModel> call, @NonNull Response<TipModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    TipModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                if (result.getData().getStatus().equalsIgnoreCase("Success")) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
                                    builder.setMessage("Payment Success")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    setResult(2);
                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle(getString(R.string.Alert));
                                    alert.show();

                                } else {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
                                    builder.setMessage(result.getData().getStatus().replaceAll("\u0027", ""))
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();

                                                    TipId = result.getData().getId().toString();


                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle(getString(R.string.Alert));
                                    alert.show();


                                }


                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
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
                    Toast.makeText(JobCompleteActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TipModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SeekerId = DataManager.getInstance().getSeekerId();

        Amount = DataManager.getInstance().getAmount();

        JobId = DataManager.getInstance().getJobID();

        paymentId = DataManager.getInstance().getPaymentId();

        TipId = DataManager.getInstance().getTipId();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getUserDetail(SeekerId);
            getSaveCard();

        }
    }

    private void getSaveCard() {
        final ProgressDialog progressdialog = new ProgressDialog(JobCompleteActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(JobCompleteActivity.this, AppPrefs.KEY_User_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetCustomerCardsModel> call = service.GetCustomerCards(s_userId);
        call.enqueue(new Callback<GetCustomerCardsModel>() {


            @Override
            public void onResponse(@NonNull Call<GetCustomerCardsModel> call, @NonNull Response<GetCustomerCardsModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetCustomerCardsModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arraySaveCard != null && arraySaveCard.size() > 0 && getSaveCardAdpater != null) {

                                arraySaveCard.clear();
                                getSaveCardAdpater.notifyDataSetChanged();
                            }

                            arraySaveCard = result.getData();

                            getSaveCardAdpater = new GetSaveCardAdpater(JobCompleteActivity.this, new GetSaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    try {

                                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(JobCompleteActivity.this, AppPrefs.KEY_Balance);

                                        double bal1 = Double.parseDouble(TippingAmount);
                                        double bal2 = Double.parseDouble(s_Balance);
                                        double tt = bal1 + bal2;
                                        double Total = 0.0;

                                        DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                        String formatted = formatter.format(bal1);

                                        String Msg = "";

                                        if (s_Balance.equalsIgnoreCase("0")) {

                                            Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                            Msg = Msg + "\n" + "Amount to be paid: $" + formatted;
                                            AmountPay = TippingAmount;
                                            AmtRef = s_Balance;

                                        } else {

                                            if (bal1 > bal2) {

                                                Total = bal1 - bal2;
                                                Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                Msg = Msg + "\n\n" + "Total Payment: $" + formatted;
                                                Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                                Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                                AmountPay = new DecimalFormat("#.00").format(bal1);
                                                AmtRef = s_Balance;

                                            } else {

                                                Total = bal2 - bal1;

                                                Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                                Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                                AmountPay = new DecimalFormat("#.00").format(bal1);
                                                AmtRef = s_Balance;
                                            }


                                        }


                                        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
                                        builder.setMessage(Msg)
                                                .setCancelable(false)

                                                .setPositiveButton(Html.fromHtml("<font color='#3e77bb'>Yes</font>"), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                        createPayment(arraySaveCard.get(position).getCustomerId());

                                                    }
                                                })
                                                .setNegativeButton(Html.fromHtml("<font color='#ff0000'>No</font>"), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.setTitle(getString(R.string.Alert));
                                        alert.show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, arraySaveCard);


                           /* if (!(result.getData().size() > 0)) {

                                RecyclerPayment.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                RecyclerPayment.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);

                            }
*/
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompleteActivity.this);
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
                    Toast.makeText(JobCompleteActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


}
