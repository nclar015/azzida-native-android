package com.azzida.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.azzida.adapter.RecentJobsAppliedAdpater;
import com.azzida.adapter.RecentJobsPostedAdpater;
import com.azzida.helper.Config;
import com.azzida.helper.StripeConnect;
import com.azzida.model.Applied;
import com.azzida.model.GetRecentModel;
import com.azzida.model.Post;
import com.azzida.model.ProfileModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {


    public int ADD_ACCOUNT = 30;
    TextView edit_profile, edit_payment_info, edit_payment_history, my_listings, my_job, Dispute_Resolution, edit_Cashout, edit_Options;
    ImageView profile_back;
    RadioGroup toggle;
    RadioButton JobsPosted, JobsApplied;
    RadioButton checkedButton;
    String checkedButtonText;
    private String receivedAmount, accountNo;
    private Boolean AzzidaVerified;
    private RecyclerView RecyclerRecentPosted;
    private LinearLayout noDataPost;
    private RecentJobsPostedAdpater recentJobsPostedAdpater;
    private ArrayList<Post> postArrayList;


    private ProgressBar progressBar;


    private CircleImageView iv_profile_image;

    private TextView txt_ProfileName, txt_Availablebalance;

    private RatingBar RateAVg;
    private ImageView img_Azzida_Verifid;


    private RecyclerView RecyclerRecentApplied;
    private LinearLayout noDataApplied;

    private RecentJobsAppliedAdpater recentJobsAppliedAdpater;
    private ArrayList<Applied> appliedArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        InitView();

    }


    private void InitView() {

        profile_back = findViewById(R.id.profile_back);

        txt_ProfileName = findViewById(R.id.txt_ProfileName);
        img_Azzida_Verifid = findViewById(R.id.img_Azzida_Verifid);
        txt_Availablebalance = findViewById(R.id.txt_Availablebalance);
        txt_Availablebalance.setVisibility(View.GONE);

        progressBar = findViewById(R.id.Progress);


        noDataPost = findViewById(R.id.noDataPost);
        noDataApplied = findViewById(R.id.noDataApplied);

        RecyclerRecentPosted = findViewById(R.id.RecyclerRecentPosted);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(MyProfileActivity.this, 1);
        RecyclerRecentPosted.setLayoutManager(mLayoutManager);
        RecyclerRecentPosted.setHasFixedSize(true);
        RecyclerRecentPosted.setItemAnimator(new DefaultItemAnimator());


        RecyclerRecentApplied = findViewById(R.id.RecyclerRecentApplied);
        final LinearLayoutManager mLayoutManage = new GridLayoutManager(MyProfileActivity.this, 1);
        RecyclerRecentApplied.setLayoutManager(mLayoutManage);
        RecyclerRecentApplied.setHasFixedSize(true);
        RecyclerRecentApplied.setItemAnimator(new DefaultItemAnimator());


        toggle = findViewById(R.id.toggle);
        JobsPosted = findViewById(R.id.JobsPosted);
        JobsApplied = findViewById(R.id.JobsApplied);

        checkedButton = toggle.findViewById(toggle.getCheckedRadioButtonId());

        checkedButtonText = "Jobs Posted";

        if (checkedButton.getText().equals("Jobs Posted")) {

            JobsPosted.setTextColor(getResources().getColor(R.color.white));
            JobsApplied.setTextColor(getResources().getColor(R.color.black));

            RecyclerRecentPosted.setVisibility(View.VISIBLE);
            RecyclerRecentApplied.setVisibility(View.GONE);

        } else {

            JobsApplied.setTextColor(getResources().getColor(R.color.white));
            JobsPosted.setTextColor(getResources().getColor(R.color.black));

            RecyclerRecentApplied.setVisibility(View.VISIBLE);
            RecyclerRecentPosted.setVisibility(View.GONE);

        }

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {

                    checkedButtonText = checkedRadioButton.getText().toString();

                    if (checkedRadioButton.getText().equals("Jobs Posted")) {

                        JobsPosted.setTextColor(getResources().getColor(R.color.white));
                        JobsApplied.setTextColor(getResources().getColor(R.color.black));

                        RecyclerRecentPosted.setVisibility(View.VISIBLE);
                        RecyclerRecentApplied.setVisibility(View.GONE);
                        try {

                            if (postArrayList != null) {

                                if (!(postArrayList.size() > 0)) {


                                    noDataPost.setVisibility(View.VISIBLE);
                                    noDataApplied.setVisibility(View.GONE);
                                } else {

                                    noDataPost.setVisibility(View.GONE);
                                    noDataApplied.setVisibility(View.GONE);

                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                        JobsApplied.setTextColor(getResources().getColor(R.color.white));
                        JobsPosted.setTextColor(getResources().getColor(R.color.black));

                        RecyclerRecentApplied.setVisibility(View.VISIBLE);
                        RecyclerRecentPosted.setVisibility(View.GONE);

                        try {
                            if (appliedArrayList != null) {

                                if (!(appliedArrayList.size() > 0)) {

                                    noDataPost.setVisibility(View.GONE);
                                    noDataApplied.setVisibility(View.VISIBLE);
                                } else {

                                    noDataPost.setVisibility(View.GONE);
                                    noDataApplied.setVisibility(View.GONE);

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    Log.e("TAG", "Checked:" + checkedRadioButton.getText());
                }
            }
        });

        RateAVg = findViewById(R.id.RateAVg);

        iv_profile_image = findViewById(R.id.iv_profile_image);

        edit_profile = findViewById(R.id.edit_profile);
        edit_payment_info = findViewById(R.id.edit_payment_info);
        edit_Cashout = findViewById(R.id.edit_Cashout);
        edit_Options = findViewById(R.id.edit_Options);
        edit_payment_history = findViewById(R.id.edit_payment_history);
        my_listings = findViewById(R.id.my_listings);
        my_job = findViewById(R.id.my_job);
        Dispute_Resolution = findViewById(R.id.Dispute_Resolution);


        profile_back.setOnClickListener(this);

        edit_profile.setOnClickListener(this);
        edit_payment_info.setOnClickListener(this);
        edit_payment_info.setOnClickListener(this);
        edit_payment_history.setOnClickListener(this);
        my_listings.setOnClickListener(this);
        my_job.setOnClickListener(this);
        edit_Cashout.setOnClickListener(this);
        edit_Options.setOnClickListener(this);
        Dispute_Resolution.setOnClickListener(this);

    }


    private void getProfile() {

        Utils.showProgressdialog(MyProfileActivity.this, "Please Wait....");

        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ID);
        Call<ProfileModel> call = service.GetProfile(s_userId);
        call.enqueue(new Callback<ProfileModel>() {


            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ProfileModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                txt_ProfileName.setText(result.getData().getFirstName() + " " + result.getData().getLastName());
                                txt_Availablebalance.setText("Referral Balance :- " + "$ " + result.getData().getBalance().replaceAll("\\.0*$", ""));

                                receivedAmount = result.getData().getReceivedAmount();

                                AzzidaVerified = result.getData().getAzzidaVerified();


 /*                               if (result.getData().getNationalStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getGlobalStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getSexOffenderStatus().equalsIgnoreCase("clear") &&
                                        result.getData().getSsnTraceStatus().equalsIgnoreCase("clear")) {

                                    img_Azzida_Verifid.setVisibility(View.GONE);


                                } else {

                                    img_Azzida_Verifid.setVisibility(View.GONE);

                                }

   */
                                AppPrefs.setBooleanKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_Azzida_Verified, AzzidaVerified);

                                accountNo = result.getData().getStripeAccId();

                                txt_Availablebalance.setVisibility(View.VISIBLE);

                                AppPrefs.setStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());

                                AppPrefs.setStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_Balance, result.getData().getBalance().replaceAll("\\.0*$", ""));

                                RateAVg.setRating(Float.parseFloat(result.getData().getUserRatingAvg()));

                                if (result.getData().getProfilePicture() != null) {
                                    if (result.getData().getProfilePicture().length() > 0) {
                                        Picasso.get().load(result.getData().getProfilePicture())
                                                .placeholder(R.drawable.no_profile)
                                                .error(R.drawable.no_profile)
                                                .into(iv_profile_image);
                                    }

                                }

                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
                    Toast.makeText(MyProfileActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_ACCOUNT) {

            RetrieveStripeAccount(DataManager.getInstance().getStripeCode(), AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ID), "");

        }
    }


    private void getRecentPosted(String Userid) {

        progressBar.setVisibility(View.VISIBLE);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetRecentModel> call = service.GetRecent(Userid);
        call.enqueue(new Callback<GetRecentModel>() {


            @Override
            public void onResponse(@NonNull Call<GetRecentModel> call, @NonNull Response<GetRecentModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetRecentModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (postArrayList != null && postArrayList.size() > 0 && recentJobsPostedAdpater != null) {

                                postArrayList.clear();
                                recentJobsPostedAdpater.notifyDataSetChanged();
                            }

                            postArrayList = result.getData().getPost();

                            recentJobsPostedAdpater = new RecentJobsPostedAdpater(MyProfileActivity.this, new RecentJobsPostedAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    Intent myIntent = new Intent(MyProfileActivity.this, JobListerDetailActivity.class);
                                    myIntent.putExtra("JobId", postArrayList.get(position).getId());
                                    myIntent.putExtra("ScreenName", "Back to My Profile");
                                    startActivity(myIntent);


                                }
                            }, postArrayList);


                            RecyclerRecentPosted.setAdapter(recentJobsPostedAdpater);


                            if (checkedButtonText.equalsIgnoreCase("Jobs Posted")) {

                                JobsPosted.setTextColor(getResources().getColor(R.color.white));
                                JobsApplied.setTextColor(getResources().getColor(R.color.black));

                                RecyclerRecentPosted.setVisibility(View.VISIBLE);
                                RecyclerRecentApplied.setVisibility(View.GONE);
                                try {

                                    if (postArrayList != null) {

                                        if (!(postArrayList.size() > 0)) {


                                            noDataPost.setVisibility(View.VISIBLE);
                                            noDataApplied.setVisibility(View.GONE);
                                        } else {

                                            noDataPost.setVisibility(View.GONE);
                                            noDataApplied.setVisibility(View.GONE);

                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {

                                JobsApplied.setTextColor(getResources().getColor(R.color.white));
                                JobsPosted.setTextColor(getResources().getColor(R.color.black));

                                RecyclerRecentApplied.setVisibility(View.VISIBLE);
                                RecyclerRecentPosted.setVisibility(View.GONE);

                                try {
                                    if (appliedArrayList != null) {

                                        if (!(appliedArrayList.size() > 0)) {

                                            noDataPost.setVisibility(View.GONE);
                                            noDataApplied.setVisibility(View.VISIBLE);
                                        } else {

                                            noDataPost.setVisibility(View.GONE);
                                            noDataApplied.setVisibility(View.GONE);

                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
                    Toast.makeText(MyProfileActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetRecentModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void getRecentApplid(String Userid) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetRecentModel> call = service.GetRecent(Userid);
        call.enqueue(new Callback<GetRecentModel>() {


            @Override
            public void onResponse(@NonNull Call<GetRecentModel> call, @NonNull Response<GetRecentModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetRecentModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (appliedArrayList != null && appliedArrayList.size() > 0 && recentJobsAppliedAdpater != null) {

                                appliedArrayList.clear();
                                recentJobsAppliedAdpater.notifyDataSetChanged();
                            }

                            appliedArrayList = result.getData().getApplied();

                            recentJobsAppliedAdpater = new RecentJobsAppliedAdpater(MyProfileActivity.this, new RecentJobsAppliedAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    Intent myIntent = new Intent(MyProfileActivity.this, JobSeekerApplyActivity.class);
                                    myIntent.putExtra("JobId", appliedArrayList.get(position).getId());
                                    myIntent.putExtra("ScreenName", "Back to My Profile");
                                    startActivity(myIntent);

                                }
                            }, appliedArrayList);

                            RecyclerRecentApplied.setAdapter(recentJobsAppliedAdpater);

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
                    Toast.makeText(MyProfileActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetRecentModel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                finish();

                break;

            case R.id.edit_Options:

                CallOption();

                break;

            case R.id.edit_Cashout:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    String AccountId = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_StripeAccId);

                    if (!receivedAmount.equals("0") && !receivedAmount.equals("0.00") && !receivedAmount.equals("0.0")) {


                        if (AccountId.length() > 0) {


                            AlertDialog.Builder Cashout = new AlertDialog.Builder(MyProfileActivity.this);

                            CardView CashOutview = (CardView) MyProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_cash_out, null);

                            EditText edt_amount = CashOutview.findViewById(R.id.edt_amount);
                            EditText edt_account_no = CashOutview.findViewById(R.id.edt_account_no);

                            edt_amount.setText("$ " + receivedAmount);
                            edt_account_no.setText(accountNo);
                            edt_amount.setTextColor(getResources().getColor(R.color.black));
                            edt_account_no.setTextColor(getResources().getColor(R.color.black));


                            Button btn_close_repost = CashOutview.findViewById(R.id.btn_close);

                            Button btn_submit_repost = CashOutview.findViewById(R.id.btn_submit);

                            Cashout.setView(CashOutview);

                            final AlertDialog CashOut = Cashout.create();

                            CashOut.setCanceledOnTouchOutside(false);
                            CashOut.setCancelable(false);
                            CashOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            CashOut.show();

                            btn_close_repost.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    CashOut.cancel();

                                }
                            });


                            btn_submit_repost.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    CashOut.cancel();

                                    if (edt_amount.getText().toString().length() > 0 && edt_account_no.getText().toString().length() > 0) {

                                        PayoutToConnectedAccount(edt_amount.getText().toString().replace("$ ", ""), edt_account_no.getText().toString());

                                    }


                                }

                            });

                        } else {

                            AlertDialog.Builder buildernw = new AlertDialog.Builder(MyProfileActivity.this);
                            buildernw.setMessage("Stripe account is needed to Cash Out")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this)
                                                    .setCancelable(false)
                                                    .setPositiveButton("Link Existing Account", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();


                                                            AlertDialog.Builder Account = new AlertDialog.Builder(MyProfileActivity.this);

                                                            CardView Accountview = (CardView) MyProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_account, null);

                                                            EditText edt_account_no = Accountview.findViewById(R.id.edt_account_no);

                                                            Button btn_close = Accountview.findViewById(R.id.btn_close);

                                                            Button btn_submit = Accountview.findViewById(R.id.btn_submit);

                                                            Account.setView(Accountview);

                                                            final AlertDialog Accoun = Account.create();

                                                            Accoun.setCanceledOnTouchOutside(false);
                                                            Accoun.setCancelable(false);
                                                            Accoun.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                            Accoun.show();

                                                            btn_close.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {


                                                                    Accoun.cancel();

                                                                    setNotComplete();

                                                                }
                                                            });


                                                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                    if (validateAccount(edt_account_no.getText().toString())) {


                                                                        Accoun.cancel();

                                                                        RetrieveStripeAccount("", AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ID), edt_account_no.getText().toString());


                                                                    }


                                                                }

                                                                private boolean validateAccount(String s) {
                                                                    boolean valid = true;

                                                                    if (s.isEmpty()) {
                                                                        edt_account_no.setError("Enter Account No");
                                                                        edt_account_no.requestFocus();
                                                                        valid = false;
                                                                    } else {
                                                                        edt_account_no.setError(null);
                                                                    }

                                                                    return valid;
                                                                }
                                                            });


                                                        }
                                                    })
                                                    .setNegativeButton("Create New Account", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                            Intent myIntent = new Intent(MyProfileActivity.this, StripeConnect.class);
                                                            startActivityForResult(myIntent, ADD_ACCOUNT);
                                                        }
                                                    })
                                                    .setNeutralButton("Skip", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();

                                                            setNotComplete();

                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.setTitle("Link Account or Create New Account");
                                            alert.show();


                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                        }
                                    });
                            AlertDialog alert = buildernw.create();
                            alert.setTitle(getString(R.string.Alert));
                            alert.show();


                        }

                    } else {

                        Toast.makeText(this, "Insufficient Balance ", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                break;


            case R.id.edit_profile:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent edit_profile = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                    startActivity(edit_profile);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


                break;

            case R.id.edit_payment_info:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent edit_payment_info = new Intent(MyProfileActivity.this, PaymentInfoActivity.class);
                    startActivity(edit_payment_info);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;


            case R.id.edit_payment_history:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent edit_payment_history = new Intent(MyProfileActivity.this, TransactionDetailsActivity.class);
                    startActivity(edit_payment_history);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;


            case R.id.my_listings:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent my_listings = new Intent(MyProfileActivity.this, ListingActivity.class);
                    startActivity(my_listings);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;


            case R.id.my_job:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent my_job = new Intent(MyProfileActivity.this, MyJobActivity.class);
                    startActivity(my_job);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;


            case R.id.Dispute_Resolution:

                if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent Dispute_Resolution = new Intent(MyProfileActivity.this, DisputeActivity.class);
                    startActivity(Dispute_Resolution);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MyProfileActivity.this,
                                            LoginActivity.class);

                                    startActivity(intent);

                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;


        }

    }

    private void CallOption() {

        if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                AppPrefs.KEY_USER_LOGIN_DEMO).
                equalsIgnoreCase("false")) {

            Intent edit_payment_history = new Intent(MyProfileActivity.this, OptionsActivity.class);
            startActivity(edit_payment_history);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
            builder.setMessage(getResources().getString(R.string.loginrequired))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MyProfileActivity.this,
                                    LoginActivity.class);

                            startActivity(intent);

                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }


    private void setNotComplete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setMessage("Link Account To Cash Out")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void RetrieveStripeAccount(String code, String SIGNUP_ID, String AccountNo) {

        Utils.showProgressdialog(MyProfileActivity.this, "Please Wait....");

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SuccessModel> call = service.RetrieveStripeAccount(code, SIGNUP_ID, AccountNo, Config.Token_Used);
        call.enqueue(new Callback<SuccessModel>() {


            @Override
            public void onResponse(@NonNull Call<SuccessModel> call, @NonNull Response<SuccessModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SuccessModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        if (code.length() > 0) {

                            AppPrefs.setStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_StripeAccId, code);

                        } else {

                            AppPrefs.setStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_StripeAccId, AccountNo);

                        }

                        setComplete();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        setComplete();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();
                    }

                } else {
                    // Server Problem
                    Toast.makeText(MyProfileActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });

    }


    private void setComplete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setMessage("Account Link Successfully")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent myIntent = new Intent(MyProfileActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void PayoutToConnectedAccount(String Amount, String Account_No) {

        Utils.showProgressdialog(MyProfileActivity.this, "Please Wait....");

        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ID);
        Call<SuccessModel> call = service.PayoutToConnectedAccount(Amount, Account_No, s_userId, Config.Token_Used);
        call.enqueue(new Callback<SuccessModel>() {


            @Override
            public void onResponse(@NonNull Call<SuccessModel> call, @NonNull Response<SuccessModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SuccessModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        AppPrefs.setStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_USER_SIGNUP_ID, "");

                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                        builder.setMessage("Amount Added Into Your Account")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                        getProfile();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
                    Toast.makeText(MyProfileActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                AppPrefs.KEY_USER_LOGIN_DEMO).
                equalsIgnoreCase("true")) {

            String s_firstName = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_FirstName);
            String s_lastName = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_LastName);

            String s_ProfileImage = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ProfileImage);


            if (s_firstName != null && s_firstName.length() > 0) {

                txt_ProfileName.setText(s_firstName + " " + s_lastName);
            }


            if (s_ProfileImage.length() > 0) {
                Picasso.get().load(s_ProfileImage)
                        .placeholder(R.drawable.no_profile)
                        .error(R.drawable.no_profile)
                        .into(iv_profile_image);
            }
        }


        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this,
                    AppPrefs.KEY_USER_LOGIN_DEMO).
                    equalsIgnoreCase("false")) {

                getProfile();
                String s_userId = AppPrefs.getStringKeyvaluePrefs(MyProfileActivity.this, AppPrefs.KEY_User_ID);
                getRecentPosted(s_userId);
                getRecentApplid(s_userId);

            }
        }

    }


}
