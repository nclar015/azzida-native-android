package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.GetSaveCardAdpater;
import com.azzida.adapter.JobRecentDetailAdpater;
import com.azzida.helper.Config;
import com.azzida.model.ApplicationAcceptedModel;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.model.GetrecentactivityModel;
import com.azzida.model.PromoCodeModel;
import com.azzida.model.ViewApplicantDetailModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantlDetailActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView img_back;
    LinearLayout AcceptOffer;
    RecyclerView RecyclerPayment;
    TextView Promocode;
    EditText edt_PromoCode;
    private String SeekerId, ListerId, JobId, OrignelAmount, Amount, AmountPay, AmtRef;
    private CircleImageView iv_profile_image;
    private TextView txt_Name, txt_Joined, txt_JobsCompleted, txt_Skills, txt_Reviews;
    private GetSaveCardAdpater getSaveCardAdpater;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;
    private ArrayList<PromoCodeModel.Datum> arrayPromocode;
    private ArrayList<GetrecentactivityModel> arraymyJob;
    private JobRecentDetailAdpater jobRecentDetailAdpater;
    private RecyclerView Recycler_JobRecentDetail;
    private ImageView img_Azzida_Verifid;
    private LinearLayout noData;
    private String PromocodeS;

    private RatingBar RateAVg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_applicant);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                SeekerId = null;
                ListerId = null;
                JobId = null;
                OrignelAmount = null;
            } else {
                SeekerId = String.valueOf(extras.getInt("SeekerId"));
                ListerId = String.valueOf(extras.getInt("ListerId"));
                JobId = extras.getString("JobId");
                OrignelAmount = extras.getString("Amount");
            }
        } else {
            SeekerId = (String) savedInstanceState.getSerializable("SeekerId");
            ListerId = (String) savedInstanceState.getSerializable("ListerId");
            JobId = (String) savedInstanceState.getSerializable("JobId");
            OrignelAmount = (String) savedInstanceState.getSerializable("Amount");
        }

        initView();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getApplicantDetail();
            getSaveCard();
            getPromocode();

        }
    }


    private void initView() {


        img_back = findViewById(R.id.img_back);

        AcceptOffer = findViewById(R.id.AcceptOffer);

        noData = findViewById(R.id.noData);

        RateAVg = findViewById(R.id.RateAVg);

        img_Azzida_Verifid = findViewById(R.id.img_Azzida_Verifid);

        Recycler_JobRecentDetail = findViewById(R.id.Recycler_JobRecentDetail);


        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        Recycler_JobRecentDetail.setLayoutManager(mLayoutManager);
        Recycler_JobRecentDetail.setHasFixedSize(true);
        Recycler_JobRecentDetail.setItemAnimator(new DefaultItemAnimator());

        txt_Name = findViewById(R.id.txt_Name);
        txt_Joined = findViewById(R.id.txt_Joined);
        txt_JobsCompleted = findViewById(R.id.txt_JobsCompleted);
        txt_Reviews = findViewById(R.id.txt_Reviews);
        txt_Skills = findViewById(R.id.txt_Skills);

        iv_profile_image = findViewById(R.id.iv_profile_image);

        img_back.setOnClickListener(this);

        AcceptOffer.setOnClickListener(this);


    }


    private void getApplicantDetail() {

        final ProgressDialog progressdialog = new ProgressDialog(ApplicantlDetailActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ViewApplicantDetailModel> call = service.ViewApplicantDetail(SeekerId);
        call.enqueue(new Callback<ViewApplicantDetailModel>() {


            @Override
            public void onResponse(@NonNull Call<ViewApplicantDetailModel> call, @NonNull Response<ViewApplicantDetailModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ViewApplicantDetailModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getSeekerId() != null) {

                                try {


                                    Picasso.get().load(result.getData().getUserProfile())
                                            .placeholder(R.drawable.no_profile)
                                            .error(R.drawable.no_profile)
                                            .into(iv_profile_image);


                                    String Date = result.getData().getJoindate().replace("/Date(", "")
                                            .replace(")/", "");

                                    txt_Name.setText(result.getData().getSeekerName());

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

                                    txt_Skills.setText(result.getData().getSkillExperience());
                                    txt_JobsCompleted.setText(result.getData().getJobCompleteCount());
                                    /*txt_Reviews.setText(result.getData().getRatingUserCount() + " " + "Reviews");*/
                                    txt_Reviews.setText("(" + result.getData().getRatingUserCount() + ")");

                                    RateAVg.setRating(Float.parseFloat(result.getData().getRateAvg()));

                                    txt_Joined.setText(DateFormat.format("MMM dd, yyyy", Long.parseLong(Date)).toString());

                                    if (arraymyJob != null && arraymyJob.size() > 0 && jobRecentDetailAdpater != null) {

                                        arraymyJob.clear();
                                        jobRecentDetailAdpater.notifyDataSetChanged();
                                    }

                                    arraymyJob = result.getData().getGetRecentActivity();

                                    jobRecentDetailAdpater = new JobRecentDetailAdpater(ApplicantlDetailActivity.this, new JobRecentDetailAdpater.ClickView() {
                                        @Override
                                        public void clickitem(View view, final int position) {

                                            Intent myIntent = new Intent(ApplicantlDetailActivity.this, JobSeekerApplyActivity.class);
                                            myIntent.putExtra("JobId", arraymyJob.get(position).getId());
                                            myIntent.putExtra("ScreenName", "Back");
                                            startActivity(myIntent);

                                        }
                                    }, arraymyJob);

                                    Recycler_JobRecentDetail.setAdapter(jobRecentDetailAdpater);

                                    if (!(result.getData().getGetRecentActivity().size() > 0)) {

                                        Recycler_JobRecentDetail.setVisibility(View.GONE);
                                        noData.setVisibility(View.VISIBLE);
                                    } else {

                                        Recycler_JobRecentDetail.setVisibility(View.VISIBLE);
                                        noData.setVisibility(View.GONE);

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    } else {

                        popMessage(result.getMessage(), ApplicantlDetailActivity.this);

                    }

                } else {
                    // Server Problem
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViewApplicantDetailModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void getSaveCard() {
        String s_userId = AppPrefs.getStringKeyvaluePrefs(ApplicantlDetailActivity.this, AppPrefs.KEY_User_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetCustomerCardsModel> call = service.GetCustomerCards(s_userId);
        call.enqueue(new Callback<GetCustomerCardsModel>() {


            @Override
            public void onResponse(@NonNull Call<GetCustomerCardsModel> call, @NonNull Response<GetCustomerCardsModel> response) {
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

                            getSaveCardAdpater = new GetSaveCardAdpater(ApplicantlDetailActivity.this, new GetSaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    try {

                                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(ApplicantlDetailActivity.this, AppPrefs.KEY_Balance);


                                        double bal1 = Double.parseDouble(Amount);

                                        double bal2 = Double.parseDouble(s_Balance);
                                        double tt = bal1 + bal2;
                                        double Total = 0.00;

                                        String Msg = "";

                                        DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                        String formatted = formatter.format(bal1);

                                        if (s_Balance.equalsIgnoreCase("0")) {

                                            if (Amount.equals(".00")) {

                                                Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                Msg = Msg + "\n" + "Amount to be paid: $" + "1.00";
                                                Msg = Msg + "\n\n" + "**Payment will be held and not released to Job Performer until job completion and confirmation";
                                                AmountPay = "1.00";
                                                AmtRef = s_Balance;

                                            } else {

                                                Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                Msg = Msg + "\n" + "Amount to be paid: $" + formatted;
                                                Msg = Msg + "\n\n" + "**Payment will be held and not released to Job Performer until job completion and confirmation";
                                                AmountPay = Amount;
                                                AmtRef = s_Balance;

                                            }


                                        } else {

                                            if (bal1 != 0.0) {

                                                if (bal1 > bal2) {

                                                    Total = bal1 - bal2;
                                                    Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                    Msg = Msg + "\n\n" + "Total Payment: $" + formatted;
                                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                                    Msg = Msg + "\n\n" + "**Payment will be held and not released to Job Performer until job completion and confirmation";
                                                    AmountPay = new DecimalFormat("#.00").format(bal1);
                                                    AmtRef = s_Balance;

                                                } else {

                                                    Total = bal2 - bal1;

                                                    Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                                    Msg = Msg + "\n\n" + "**Payment will be held and not released to Job Performer until job completion and confirmation";
                                                    AmountPay = new DecimalFormat("#.00").format(bal1);
                                                    AmtRef = s_Balance;
                                                }


                                            } else {

                                                Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                                Msg = Msg + "\n" + "Amount to be paid: $" + "1.00";
                                                Msg = Msg + "\n\n" + "**Payment will be held and not released to Job Performer until job completion and confirmation";
                                                AmountPay = "1.00";
                                                AmtRef = "0";

                                            }


                                        }

                                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void getPromocode() {
        String s_userId = AppPrefs.getStringKeyvaluePrefs(ApplicantlDetailActivity.this, AppPrefs.KEY_User_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<PromoCodeModel> call = service.GetPromoCodeList(s_userId);
        call.enqueue(new Callback<PromoCodeModel>() {


            @Override
            public void onResponse(@NonNull Call<PromoCodeModel> call, @NonNull Response<PromoCodeModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    PromoCodeModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            arrayPromocode = result.getData();


                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PromoCodeModel> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            setResult(2);
            finish();
        }
    }


    private void createPayment(String customerId) {

        final ProgressDialog progressdialog = new ProgressDialog(ApplicantlDetailActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, SeekerId, AmtRef, customerId, AmountPay, "", "payment", PromocodeS, Config.Token_Used);
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

                                    AppPrefs.setStringKeyvaluePrefs(ApplicantlDetailActivity.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));


                                    ApplicationAccepted();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
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


            case R.id.AcceptOffer:


                String status = NetworkUtils.getConnectivityStatus(this);
                if (status.equals("404")) {
                    Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Amount = OrignelAmount;
                        PromocodeS = "";

                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(ApplicantlDetailActivity.this, AppPrefs.KEY_Balance);

                        double bal1 = Double.parseDouble(Amount);
                        double bal2 = Double.parseDouble(s_Balance);

                        if (bal2 > bal1) {

                            String Msg = "Amount debited from Referral Balance: $" + Amount;

                            AmountPay = Amount;
                            AmtRef = Amount;

                            AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantlDetailActivity.this);
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


                            AlertDialog.Builder b = new AlertDialog.Builder(ApplicantlDetailActivity.this, R.style.MyCustomTheme);

                            LinearLayout PaymentView = (LinearLayout) ApplicantlDetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment, null);

                            TextView Txt_Direct_PAy = PaymentView.findViewById(R.id.Txt_Direct_PAy);
                            RecyclerPayment = PaymentView.findViewById(R.id.RecyclerPayment);

                            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                            RecyclerPayment.setLayoutManager(mLayoutManager);
                            RecyclerPayment.setHasFixedSize(true);
                            RecyclerPayment.setItemAnimator(new DefaultItemAnimator());
                            RecyclerPayment.setAdapter(getSaveCardAdpater);

                            Promocode = PaymentView.findViewById(R.id.Promocode);

                            if (arrayPromocode != null && arrayPromocode.size() > 0) {

                                Promocode.setVisibility(View.VISIBLE);

                            } else {

                                Promocode.setVisibility(View.GONE);

                            }

                            Promocode.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (Promocode.getText().toString().equalsIgnoreCase("Have Promo Code")) {

                                        ShowAddPromoCodePopup();

                                    }

                                }
                            });

                            b.setView(PaymentView);

                            final AlertDialog d = b.create();

                            d.setCanceledOnTouchOutside(true);
                            d.setCancelable(true);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                            WindowManager.LayoutParams wmlp = d.getWindow().getAttributes();

                            wmlp.gravity = Gravity.BOTTOM;

                            Window window = d.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            d.show();

                            Txt_Direct_PAy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d.cancel();
                                    Intent myIntent = new Intent(ApplicantlDetailActivity.this, PaymentCheckOut.class);
                                    myIntent.putExtra("SeekerId", SeekerId);
                                    myIntent.putExtra("TotalAmount", Amount);
                                    myIntent.putExtra("JobId", JobId);
                                    myIntent.putExtra("PromoCode", PromocodeS);
                                    myIntent.putExtra("PaymentType", "payment");
                                    myIntent.putExtra("Screen", "0");
                                    startActivityForResult(myIntent, 2);

                                }
                            });


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                break;

        }

    }

    private void ShowAddPromoCodePopup() {

        AlertDialog.Builder b1 = new AlertDialog.Builder(ApplicantlDetailActivity.this);

        CardView view1 = (CardView) ApplicantlDetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_add_promo_code, null);

        Button btn_close1 = view1.findViewById(R.id.btn_close);

        Button btn_submit1 = view1.findViewById(R.id.btn_submit);

        edt_PromoCode = view1.findViewById(R.id.edt_PromoCode);

        TextInputLayout TextInputLayout_promocode = view1.findViewById(R.id.TextInputLayout_promocode);

        edt_PromoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextInputLayout_promocode.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b1.setView(view1);

        final AlertDialog d1 = b1.create();

        d1.setCanceledOnTouchOutside(false);
        d1.setCancelable(false);
        d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d1.show();

        btn_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d1.cancel();

            }
        });


        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = NetworkUtils.getConnectivityStatus(ApplicantlDetailActivity.this);
                if (status.equals("404")) {
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {

                    if (validatePromocodeEmpty()) {

                        for (int i = 0; i < arrayPromocode.size(); i++) {

                            if (edt_PromoCode.getText().toString().equalsIgnoreCase(arrayPromocode.get(i).getCode())) {

                                PromocodeS = arrayPromocode.get(i).getCode();

                                if (arrayPromocode.get(i).getTypeDiscount().equalsIgnoreCase("amount")) {

                                    double DisAmount = Double.parseDouble(arrayPromocode.get(i).getAmount());
                                    double OrigAmount = Double.parseDouble(Amount);

                                    double NewAmount = OrigAmount - DisAmount;

                                    Amount = new DecimalFormat("#.00").format(NewAmount);
                                    Promocode.setText("Promo Code Is :- " + PromocodeS);
                                    d1.cancel();

                                } else {

                                    double OrigAmount = Double.parseDouble(Amount);

                                    double amount = Double.parseDouble(arrayPromocode.get(i).getAmount());
                                    double res = (OrigAmount * amount) / 100;

                                    double NewAmount = OrigAmount - res;

                                    Amount = new DecimalFormat("#.00").format(NewAmount);

                                    Promocode.setText("Promo Code Is :- " + PromocodeS);
                                    d1.cancel();

                                }


                            } else {

                                TextInputLayout_promocode.setError("Code Not Found");

                            }

                        }

                    }


                }


            }

            public boolean validatePromocodeEmpty() {
                boolean valid = true;

                String s_Promo = edt_PromoCode.getText().toString();

                if (s_Promo.isEmpty()) {
                    edt_PromoCode.setError("Enter PromoCode");
                    edt_PromoCode.requestFocus();
                    valid = false;
                } else {
                    edt_PromoCode.setError(null);
                }

                return valid;
            }

        });


    }


    private void ApplicationAccepted() {
        final ProgressDialog progressdialog = new ProgressDialog(ApplicantlDetailActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ApplicationAcceptedModel> call = service.ApplicationAccepted(JobId, SeekerId, ListerId, "true");
        call.enqueue(new Callback<ApplicationAcceptedModel>() {


            @Override
            public void onResponse(@NonNull Call<ApplicationAcceptedModel> call, @NonNull Response<ApplicationAcceptedModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ApplicationAcceptedModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getId() != null) {

                               /* Intent intent = new Intent(ApplicantlDetailActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/
                                setResult(2);
                                finish();

                            } else {

                            }

                        }
                    } else {

                        popMessage(result.getMessage(), ApplicantlDetailActivity.this);

                    }

                } else {
                    // Server Problem
                    Toast.makeText(ApplicantlDetailActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicationAcceptedModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    public void popMessage(String errorMsg,
                           final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMsg)
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


}
