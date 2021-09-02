package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
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
import androidx.viewpager.widget.ViewPager;

import com.azzida.R;
import com.azzida.adapter.ViewPagerAdapter;
import com.azzida.helper.Config;
import com.azzida.helper.StripeConnect;
import com.azzida.model.GetJobDetailModel;
import com.azzida.model.ImageList;
import com.azzida.model.JobApplicationModel;
import com.azzida.model.JobCompleteModel;
import com.azzida.model.OfferAcceptModel;
import com.azzida.model.SaveRateModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSeekerApplyActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public int ADD_ACCOUNT = 30;
    LinearLayout Apply, Confirm_Availability, Confirm_Cancellation, Application_Pending, Complete_Job, Complete;
    CardView card_view;

    ImageView img_back_feed, img_chat;
    TextView txt_CancelJob;
    TextView title;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    ViewPagerAdapter viewPagerAdapter;
    EditText edt_Cancellation_Reason;
    private GoogleMap mMap;
    private TextView txt_Description, txt_Title, txt_Amount, txt_Date, txt_HowLong, txt_JobCategory, txt_Address;
    private ImageView img_JobPicture;
    private ArrayList<ImageList> arrayImageList;
    private String JobIdL, ListerId, ListerName, ListerProfile;
    private CircleImageView Lister_image;
    private TextView txt_ListerName, txt_ListerJob, txt_Distance;
    private String JobId, TitleName, status;
    private boolean isApply, ApplicationAccepted, OfferAccepted, IsComplete;
    private String IsCompleteTxt;
    private Double Lat, Longs;
    private long timeInMilliseconds;
    private AlertDialog d2, d3;
    private RatingBar mBar;
    private int dotscount;
    private ImageView[] dots;
    private Boolean ShowDialog = false;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_apply);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                JobId = null;
            } else {
                JobId = String.valueOf(extras.getInt("JobId"));
                TitleName = extras.getString("ScreenName");
                DataManager.getInstance().setJobID(JobId);
            }
        } else {
            JobId = (String) savedInstanceState.getSerializable("JobId");
            TitleName = (String) savedInstanceState.getSerializable("ScreenName");
        }

        initView();

        status = NetworkUtils.getConnectivityStatus(JobSeekerApplyActivity.this);
/*
        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getJobDetail(JobId);

        }*/

    }


    private void initView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        viewPager = findViewById(R.id.viewPager);

        sliderDotspanel = findViewById(R.id.SliderDots);

        title = findViewById(R.id.title);

        title.setText(TitleName);

        Apply = findViewById(R.id.Apply);

        Application_Pending = findViewById(R.id.Application_Pending);

        Confirm_Availability = findViewById(R.id.Confirm_Availability);

        Confirm_Cancellation = findViewById(R.id.Confirm_Cancellation);

        Complete_Job = findViewById(R.id.Complete_Job);

        Complete = findViewById(R.id.Complete);

        Lister_image = findViewById(R.id.Lister_image);

        txt_ListerName = findViewById(R.id.txt_ListerName);
        txt_ListerJob = findViewById(R.id.txt_ListerJob);


        txt_Distance = findViewById(R.id.txt_Distance);

        img_JobPicture = findViewById(R.id.img_JobPicture);

        txt_Description = findViewById(R.id.txt_Description);
        txt_Title = findViewById(R.id.txt_Title);
        txt_Amount = findViewById(R.id.txt_Amount);

        txt_Date = findViewById(R.id.txt_Date);

        txt_HowLong = findViewById(R.id.txt_HowLong);
        txt_JobCategory = findViewById(R.id.txt_JobCategory);

        txt_Address = findViewById(R.id.txt_Address);

        card_view = findViewById(R.id.card_view);

        img_back_feed = findViewById(R.id.img_back_feed);

        img_chat = findViewById(R.id.img_chat);

        txt_CancelJob = findViewById(R.id.txt_CancelJob);

        Apply.setOnClickListener(this);
        Confirm_Availability.setOnClickListener(this);
        Confirm_Cancellation.setOnClickListener(this);
        Complete_Job.setOnClickListener(this);

        img_back_feed.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        txt_CancelJob.setOnClickListener(this);
        card_view.setOnClickListener(this);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }


    private void getJobDetail(String JobId) {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<GetJobDetailModel> call = service.GetJobDetail(JobId, s_userId);
        call.enqueue(new Callback<GetJobDetailModel>() {


            @Override
            public void onResponse(@NonNull Call<GetJobDetailModel> call, @NonNull Response<GetJobDetailModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetJobDetailModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getJobId() != null) {


                                JobIdL = String.valueOf(result.getData().getJobId());

                                ListerId = String.valueOf(result.getData().getListerId());

                                ListerName = String.valueOf(result.getData().getListerName());
                                ListerProfile = String.valueOf(result.getData().getListerProfilePicture());

                                isApply = result.getData().getIsApply();
                                OfferAccepted = result.getData().getOfferAccepted();
                                IsCompleteTxt = String.valueOf(result.getData().getComplete());
                                IsComplete = result.getData().getComplete();
                                ApplicationAccepted = result.getData().getApplicationAccepted();

                                if (result.getData().getPosterCancelRequest() && !IsComplete) {
                                    Confirm_Cancellation.setVisibility(View.VISIBLE);
                                } else {
                                    Confirm_Cancellation.setVisibility(View.GONE);
                                }

                                if (!result.getData().getPosterCancelRequest() && !IsComplete && isApply) {
                                    txt_CancelJob.setVisibility(View.VISIBLE);
                                } else {
                                    txt_CancelJob.setVisibility(View.GONE);
                                }


                                arrayImageList = result.getData().getImageList();


                                if (arrayImageList.size() > 0) {

                                    viewPagerAdapter = new ViewPagerAdapter(arrayImageList, JobSeekerApplyActivity.this);

                                    viewPager.setAdapter(viewPagerAdapter);
                                    sliderDotspanel.removeAllViews();

                                    dotscount = viewPagerAdapter.getCount();
                                    dots = new ImageView[dotscount];

                                    for (int i = 0; i < dotscount; i++) {

                                        dots[i] = new ImageView(JobSeekerApplyActivity.this);
                                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                        params.setMargins(8, 0, 8, 0);

                                        sliderDotspanel.addView(dots[i], params);

                                    }

                                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                                    img_JobPicture.setVisibility(View.GONE);

                                /*  Picasso.get().load(arrayImageList.get(0).getImageName())
                                          .placeholder(R.drawable.image_placeholder)
                                          .error(R.drawable.image_placeholder)
                                          .into(img_JobPicture);*/

                                } else {


                                  /*  Picasso.get().load(result.getData().getJobPicture())
                                            .placeholder(R.drawable.image_placeholder)
                                            .error(R.drawable.image_placeholder)
                                            .into(img_JobPicture);*/

                                    Picasso.get().load(R.drawable.no_images)
                                            .placeholder(R.drawable.no_images)
                                            .error(R.drawable.no_images)
                                            .into(img_JobPicture);
                                    img_JobPicture.setVisibility(View.VISIBLE);
                                }


                                txt_Description.setText(result.getData().getJobDescription());
                                txt_Title.setText(result.getData().getJobTitle());
                                txt_HowLong.setText(result.getData().getHowLong());
                                txt_JobCategory.setText(result.getData().getJobCategory());

                                String roundedBalance = result.getData().getAmount().replaceAll("\\.0*$", "");

                                String daten = DateFormat.format("yyyy-MM-d", Long.parseLong(result.getData().getFromDate().replaceAll("\\.0*$", ""))).toString();

                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
/*
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
*/


                                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());


                                Date date = null;
                                try {
                                    date = inputFormat.parse(daten);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (daten.endsWith("1") && !daten.endsWith("11"))
                                    format = new SimpleDateFormat("EEE,MMMM d'st'", Locale.US);
                                else if (daten.endsWith("2") && !daten.endsWith("12"))
                                    format = new SimpleDateFormat("EEE,MMMM d'nd'", Locale.US);
                                else if (daten.endsWith("3") && !daten.endsWith("13"))
                                    format = new SimpleDateFormat("EEE,MMMM d'nd'", Locale.US);
                                else
                                    format = new SimpleDateFormat("EEE,MMMM d'th'", Locale.getDefault());

                                assert date != null;
                                String yourDate = format.format(date);

                                txt_Date.setText(yourDate);

                                Picasso.get().load(result.getData().getListerProfilePicture())
                                        .placeholder(R.drawable.no_profile)
                                        .error(R.drawable.no_profile)
                                        .into(Lister_image);

                                txt_ListerName.setText(result.getData().getListerName());

                                txt_ListerJob.setText(result.getData().getListerCompleteJob() + " " + "Jobs" + " " + "Performed");


                                txt_Amount.setText("$" + roundedBalance);

                                Lat = Double.valueOf(result.getData().getLatitude());
                                Longs = Double.valueOf(result.getData().getLongitude());

                                LatLng location = new LatLng(Lat, Longs);

                                /*mMap.addMarker(new MarkerOptions().position(location));*/

                                mMap.addCircle(new CircleOptions()
                                        .center(location)
                                        .radius(100)
                                        .strokeColor(0x220000FF)
                                        .fillColor(0x220000FF)
                                        .strokeWidth(5)
                                );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                                // Zoom in, animating the camera.
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

                                latitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_LAT));

                                longitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_LONG));

                                float f = getDistance(new LatLng(Lat, Longs), new LatLng(latitude, longitude));

                                String mile = String.valueOf(Math.round(f));
                                txt_Distance.setText(mile + " Miles Away");

                                txt_Address.setText("Job Posting Address :- " + result.getData().getLocation());

                                if (isApply) {

                                    Apply.setVisibility(View.GONE);
                                    Application_Pending.setVisibility(View.VISIBLE);
                                    Confirm_Availability.setVisibility(View.GONE);
                                    Complete_Job.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.INVISIBLE);
                                    txt_Address.setVisibility(View.GONE);


                                }


                                if (ApplicationAccepted) {

                                    Apply.setVisibility(View.GONE);
                                    Application_Pending.setVisibility(View.GONE);
                                    Confirm_Availability.setVisibility(View.VISIBLE);
                                    Complete_Job.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.VISIBLE);
                                    txt_Address.setVisibility(View.GONE);

                                }

                                if (OfferAccepted) {


                                    Apply.setVisibility(View.GONE);
                                    Application_Pending.setVisibility(View.GONE);
                                    Confirm_Availability.setVisibility(View.GONE);
                                    Complete_Job.setVisibility(View.VISIBLE);
                                    img_chat.setVisibility(View.VISIBLE);
                                    txt_Address.setVisibility(View.VISIBLE);

                                }


                                if (IsCompleteTxt != null && !IsCompleteTxt.equals("null")) {
                                    if (IsComplete) {

                                        Apply.setVisibility(View.GONE);
                                        Confirm_Availability.setVisibility(View.GONE);
                                        Application_Pending.setVisibility(View.GONE);
                                        Complete_Job.setVisibility(View.GONE);
                                        img_chat.setVisibility(View.VISIBLE);
                                        Complete.setVisibility(View.VISIBLE);

                                    }
                                }


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobDetailModel> call, @NonNull Throwable t) {
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


            case R.id.Apply:

                String AccountId = AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_StripeAccId);

                if (AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {


                    if (AccountId.length() > 0) {

                        if (status.equals("404")) {
                            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            applyJob();

                        }

                    } else {

                        if (AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this,
                                AppPrefs.KEY_First_Time_Apply).
                                equalsIgnoreCase("")) {


                            AppPrefs.setStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_First_Time_Apply, "false");


                            AlertDialog.Builder StripeDialog = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                            CardView StripeDialogview = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_why_stripe, null);

                            Button btn_close = StripeDialogview.findViewById(R.id.btn_close);

                            Button btn_add = StripeDialogview.findViewById(R.id.btn_add);

                            StripeDialog.setView(StripeDialogview);

                            final AlertDialog Stripe = StripeDialog.create();

                            Stripe.setCanceledOnTouchOutside(false);
                            Stripe.setCancelable(false);
                            Stripe.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Stripe.show();

                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    Stripe.cancel();


                                }
                            });


                            btn_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Stripe.cancel();
                                    stripConnectPopup();

                                }

                            });


                        } else {

                            stripConnectPopup();

                        }


                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(JobSeekerApplyActivity.this,
                                            LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            | Intent.FLAG_ACTIVITY_NO_HISTORY);

                                    startActivity(intent);
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

            case R.id.Confirm_Cancellation:

                String msg = "You hereby confirm that job has been cancelled and no work has been performed and no payment is due for " +
                        "completion of " + txt_Title.getText().toString() + ". If this cancellation request has been sent in error and your job has been completed, " +
                        "please click “Close” and contact the Job Poster through the Azzida Chat message system on the Job Details screen.";
                JobCancelConfirmPopup(msg);

                break;

            case R.id.Confirm_Availability:

                AlertDialog.Builder b8 = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                CardView view = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_confirm_availability, null);
                b8.setView(view);

                Button btn_close = view.findViewById(R.id.btn_close);
                Button btn_submit = view.findViewById(R.id.btn_submit);


                d3 = b8.create();

                d3.setCanceledOnTouchOutside(false);
                d3.setCancelable(false);
                d3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                d3.show();

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d3.cancel();

                    }


                });


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (status.equals("404")) {
                            Toast.makeText(JobSeekerApplyActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            ApplicationAcceptedSeeker();

                        }

                    }


                });

                break;


            case R.id.Complete_Job:


                AlertDialog.Builder comp = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                CardView viewComp = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_complete, null);

                comp.setView(viewComp);


                Button btn_submist = viewComp.findViewById(R.id.btn_submit);
                Button btn_closse = viewComp.findViewById(R.id.btn_close);

                TextView txt_conpletion_text = viewComp.findViewById(R.id.txt_conpletion_text);

                txt_conpletion_text.setText("I hereby confirm that " + txt_Title.getText().toString() + " for " + ListerName + " has been completed");


                AlertDialog d2Comp = comp.create();

                d2Comp.setCanceledOnTouchOutside(false);
                d2Comp.setCancelable(false);
                d2Comp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                d2Comp.show();

                btn_submist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d2Comp.cancel();

                        Date currentTime = Calendar.getInstance().getTime();

                        String date = String.valueOf(currentTime);

                        DateTimeFormatter formatter = null;
                        LocalDateTime localDate = null;
                        timeInMilliseconds = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            localDate = LocalDateTime.parse(date, formatter);
                            timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();

                        } else {
                            SimpleDateFormat sdf4 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            try {

                                Date mDate = sdf4.parse(date);
                                assert mDate != null;
                                timeInMilliseconds = mDate.getTime();

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        Log.d("TAG", "Date in milli" + timeInMilliseconds);


                        AlertDialog.Builder b2 = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                        CardView view2 = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_done, null);

                        b2.setView(view2);

                        LinearLayout done_back = view2.findViewById(R.id.done_back);

                /*TextView txt_done_back = view2.findViewById(R.id.txt_done_back);

                txt_done_back.setText(TitleName);*/

                        LinearLayout share = view2.findViewById(R.id.share);

                        CircleImageView img_ListerProfile = view2.findViewById(R.id.img_ListerProfile);
                        TextView txt_ListerName = view2.findViewById(R.id.txt_ListerName);

                        txt_ListerName.setText("How was " + ListerName + "?");
                        Picasso.get().load(ListerProfile)
                                .placeholder(R.drawable.no_profile)
                                .error(R.drawable.no_profile)
                                .into(img_ListerProfile);

                        mBar = view2.findViewById(R.id.mRatingBar);

                        d2 = b2.create();

                        d2.setCanceledOnTouchOutside(false);
                        d2.setCancelable(false);
                        d2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        d2.show();

                        done_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String statusNew = NetworkUtils.getConnectivityStatus(JobSeekerApplyActivity.this);
                                if (statusNew.equals("404")) {
                                    Toast.makeText(JobSeekerApplyActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                                } else {

                                    completeJob();

                           /* if (mBar.getRating() > 0) {

                                completeJob();

                            } else {

                                d2.cancel();
                            }*/


                                }

                            }
                        });


                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.createDynamicLink_Advanced(JobSeekerApplyActivity.this, JobId,
                                        AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_User_ID), "I just completed this job on Azzida with a link to the Job Post");
                            }
                        });


                    }
                });

                btn_closse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d2Comp.cancel();

                    }
                });

                break;


            case R.id.card_view:


                if (AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent myIntent = new Intent(JobSeekerApplyActivity.this, JobSeekerPostJobUserDetailActivity.class);
                    myIntent.putExtra("ListerId", ListerId);

                    startActivity(myIntent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(JobSeekerApplyActivity.this,
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

            case R.id.img_chat:


                Intent Chat = new Intent(JobSeekerApplyActivity.this, ChatActivity.class);
                Chat.putExtra("UserId", ListerId);
                Chat.putExtra("Profile", ListerProfile);
                Chat.putExtra("Name", ListerName);
                Chat.putExtra("JobId", JobId);
                startActivity(Chat);


                break;

            case R.id.txt_CancelJob:


                CancelJobPopup();

                break;

        }

    }

    private void CancelJobPopup() {

        AlertDialog.Builder b = new AlertDialog.Builder(JobSeekerApplyActivity.this);

        CardView views = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_cancel, null);

        Button btn_closes = views.findViewById(R.id.btn_close);
        TextView txt_cancelText = views.findViewById(R.id.txt_cancelText);

        if (isApply || OfferAccepted || ApplicationAccepted) {

            txt_cancelText.setVisibility(View.VISIBLE);

        } else {

            txt_cancelText.setVisibility(View.GONE);
        }

        Button btn_submits = views.findViewById(R.id.btn_submit);
        edt_Cancellation_Reason = views.findViewById(R.id.edt_Cancellation_Reason);

        b.setView(views);

        final AlertDialog d = b.create();


        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d.show();

        btn_closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d.cancel();

            }
        });


        btn_submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equals("404")) {
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    if (validateReason()) {

                        CancelJobApi();
                    }


                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_ACCOUNT) {

            RetrieveStripeAccount(DataManager.getInstance().getStripeCode(), AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_User_ID), "");

        }
    }

    private void stripConnectPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this)
                .setCancelable(false)
                .setPositiveButton("Link Existing Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();


                        AlertDialog.Builder Account = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                        CardView Accountview = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_account, null);

                        EditText edt_account_no = Accountview.findViewById(R.id.edt_account_no);

                        Button btn_close_strip = Accountview.findViewById(R.id.btn_close);

                        Button btn_submit_strip = Accountview.findViewById(R.id.btn_submit);

                        Account.setView(Accountview);

                        final AlertDialog Accoun = Account.create();

                        Accoun.setCanceledOnTouchOutside(false);
                        Accoun.setCancelable(false);
                        Accoun.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Accoun.show();

                        btn_close_strip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Accoun.cancel();

                                setNotComplete();

                            }
                        });


                        btn_submit_strip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (validateAccount(edt_account_no.getText().toString())) {


                                    Accoun.cancel();

                                    RetrieveStripeAccount("", AppPrefs.getStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_User_ID), edt_account_no.getText().toString());


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
                        Intent myIntent = new Intent(JobSeekerApplyActivity.this, StripeConnect.class);
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


    private void CancelJobApi() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_Reason = edt_Cancellation_Reason.getText().toString();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        Call<SuccessModel> call = service.CancelJob(JobId, s_Reason, s_userId, ts);
        call.enqueue(new Callback<SuccessModel>() {


            @Override
            public void onResponse(@NonNull Call<SuccessModel> call, @NonNull Response<SuccessModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SuccessModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void JobCancelConfirmPopup(String msg) {


        AlertDialog.Builder b55 = new AlertDialog.Builder(JobSeekerApplyActivity.this);

        CardView views55 = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_cancel_confirm, null);

        Button btn_closes = views55.findViewById(R.id.btn_close);
        TextView txt_cancelText = views55.findViewById(R.id.txt_cancelText);

        txt_cancelText.setText(msg);

        Button btn_submits = views55.findViewById(R.id.btn_submit);

        b55.setView(views55);

        final AlertDialog d55 = b55.create();


        d55.setCanceledOnTouchOutside(false);
        d55.setCancelable(false);
        d55.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d55.show();

        btn_closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d55.cancel();

            }
        });


        btn_submits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equals("404")) {
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {

                    JobCancelConfirmApi();

                }


            }
        });


    }


    private void JobCancelConfirmApi() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        Call<SuccessModel> call = service.JobCancelApplicationAcceptReject(s_userId, JobId, "true", ts);
        call.enqueue(new Callback<SuccessModel>() {

            @Override
            public void onResponse(@NonNull Call<SuccessModel> call, @NonNull Response<SuccessModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SuccessModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    public float getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);

        float distance = locationA.distanceTo(locationB) / 1000;//To convert Meter in Kilometer

        return convertKmsToMiles(distance);


    }


    public float convertKmsToMiles(float kms) {
        return (float) (0.621371 * kms);
    }

    private void completeJob() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String timeStamp = String.valueOf(timeInMilliseconds);

        Call<JobCompleteModel> call = service.JobComplete(JobId, timeStamp, "true");
        call.enqueue(new Callback<JobCompleteModel>() {


            @Override
            public void onResponse(@NonNull Call<JobCompleteModel> call, @NonNull Response<JobCompleteModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    JobCompleteModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getId() != null) {


                                saveRating();


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobCompleteModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }

    private void saveRating() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<SaveRateModel> call = service.SaveRate(ListerId, JobId, String.valueOf(mBar.getRating()), s_userId);
        call.enqueue(new Callback<SaveRateModel>() {


            @Override
            public void onResponse(@NonNull Call<SaveRateModel> call, @NonNull Response<SaveRateModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SaveRateModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getId() != null) {
                                d2.cancel();

                                finish();


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveRateModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
                d2.cancel();
            }
        });

    }


    private void ApplicationAcceptedSeeker() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<OfferAcceptModel> call = service.OfferAccept(JobId, s_userId, ListerId, "true");
        call.enqueue(new Callback<OfferAcceptModel>() {


            @Override
            public void onResponse(@NonNull Call<OfferAcceptModel> call, @NonNull Response<OfferAcceptModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    OfferAcceptModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getId() != null) {
                                d3.cancel();

                                Apply.setVisibility(View.GONE);
                                Application_Pending.setVisibility(View.GONE);
                                Confirm_Availability.setVisibility(View.GONE);
                                Complete_Job.setVisibility(View.VISIBLE);
                                img_chat.setVisibility(View.VISIBLE);
                                txt_CancelJob.setVisibility(View.VISIBLE);

                                AlertDialog.Builder b1 = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                                CardView view1 = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_accepted, null);

                                TextView titlePop = view1.findViewById(R.id.titlePop);
                                titlePop.setText(TitleName);
                                b1.setView(view1);

                                LinearLayout Confirm_back = view1.findViewById(R.id.Confirm_back);


                                AlertDialog d1 = b1.create();

                                d1.setCanceledOnTouchOutside(false);
                                d1.setCancelable(false);
                                d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                d1.show();

                                Confirm_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        d1.cancel();
                                        finish();

                                    }


                                });


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OfferAcceptModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
                d3.cancel();
            }
        });
    }

    private void applyJob() {

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerApplyActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);

        String UserId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<JobApplicationModel> call = service.JobApplication("0", UserId, ListerId, JobIdL, "true");

        call.enqueue(new Callback<JobApplicationModel>() {


            @Override
            public void onResponse(@NonNull Call<JobApplicationModel> call, @NonNull Response<JobApplicationModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    JobApplicationModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getJobId() != null) {

                                AlertDialog.Builder b = new AlertDialog.Builder(JobSeekerApplyActivity.this);

                                CardView view = (CardView) JobSeekerApplyActivity.this.getLayoutInflater().inflate(R.layout.dialog_apply, null);

                                b.setView(view);

                                LinearLayout apply_back = view.findViewById(R.id.apply_back);

                                TextView titlePopApply = view.findViewById(R.id.titlePopApply);
                                TextView txt_Apply_Title = view.findViewById(R.id.txt_Apply_Title);

                                if (ShowDialog) {

                                    txt_Apply_Title.setText(getResources().getString(R.string.application_complete));

                                } else {

                                    txt_Apply_Title.setText(getResources().getString(R.string.thanks_for_applying));
                                }

                                titlePopApply.setText(TitleName);


                                final AlertDialog d = b.create();

                                d.setCanceledOnTouchOutside(false);
                                d.setCancelable(false);
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                d.show();

                                apply_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Apply.setVisibility(View.GONE);
                                        Application_Pending.setVisibility(View.VISIBLE);
                                        Confirm_Availability.setVisibility(View.GONE);
                                        Complete_Job.setVisibility(View.GONE);
                                        img_chat.setVisibility(View.GONE);
                                        txt_CancelJob.setVisibility(View.GONE);
                                        d.cancel();
                                        finish();
                                    }
                                });

                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobApplicationModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    public boolean validateReason() {
        boolean valid = true;

        String s_FirstName = edt_Cancellation_Reason.getText().toString();

        if (s_FirstName.isEmpty()) {
            edt_Cancellation_Reason.setError("Enter Reason");
            edt_Cancellation_Reason.requestFocus();
            valid = false;
        } else {
            edt_Cancellation_Reason.setError(null);
        }

        return valid;
    }

    @Override
    protected void onResume() {
        super.onResume();

        String Jobid = DataManager.getInstance().getJobID();

        status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getJobDetail(Jobid);

        }

    }

    private void setNotComplete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
        builder.setMessage("Link Account To Apply Job")
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

        Utils.showProgressdialog(JobSeekerApplyActivity.this, "Please Wait....");

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

                        AppPrefs.setStringKeyvaluePrefs(JobSeekerApplyActivity.this, AppPrefs.KEY_StripeAccId, AccountNo);

                        setComplete();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
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
                    Toast.makeText(JobSeekerApplyActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });

    }


    private void setComplete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerApplyActivity.this);
        builder.setMessage("Account Link Successfully")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                        if (status.equals("404")) {
                            Toast.makeText(JobSeekerApplyActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            ShowDialog = true;
                            applyJob();

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
