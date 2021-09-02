package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.azzida.R;
import com.azzida.adapter.ViewPagerAdapter;
import com.azzida.model.GetJobDetailModel;
import com.azzida.model.ImageList;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobSeekerNotSelectedActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    LinearLayout Apply, Confirm_Availability, Application_Pending, Complete_Job, Complete, notSelected;

    CardView card_view;

    ImageView img_back_feed, img_chat;
    TextView txt_CancelJob;
    TextView title;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    ViewPagerAdapter viewPagerAdapter;
    private GoogleMap mMap;
    private TextView txt_Description, txt_Title, txt_Amount, txt_Date, txt_HowLong, txt_JobCategory;
    private ImageView img_JobPicture;
    private ArrayList<ImageList> arrayImageList;
    private String JobIdL, ListerId, ListerName, ListerProfile;
    private CircleImageView Lister_image;
    private TextView txt_ListerName, txt_ListerJob, txt_Distance;
    private String JobId;
    private Double Lat, Longs;
    private int dotscount;
    private ImageView[] dots;
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

                DataManager.getInstance().setJobID(JobId);
            }
        } else {
            JobId = (String) savedInstanceState.getSerializable("JobId");
        }

        initView();

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

        title.setText("Back To My Job");

        Apply = findViewById(R.id.Apply);
        notSelected = findViewById(R.id.notSelected);
        Application_Pending = findViewById(R.id.Application_Pending);
        Complete = findViewById(R.id.Complete);
        Confirm_Availability = findViewById(R.id.Confirm_Availability);
        Complete_Job = findViewById(R.id.Complete_Job);

        Apply.setVisibility(View.GONE);
        Confirm_Availability.setVisibility(View.GONE);
        Application_Pending.setVisibility(View.GONE);
        Complete_Job.setVisibility(View.GONE);
        Complete.setVisibility(View.GONE);
        notSelected.setVisibility(View.VISIBLE);


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

        card_view = findViewById(R.id.card_view);

        img_back_feed = findViewById(R.id.img_back_feed);

        img_chat = findViewById(R.id.img_chat);

        txt_CancelJob = findViewById(R.id.txt_CancelJob);


        Apply.setVisibility(View.GONE);
        Application_Pending.setVisibility(View.GONE);
        Confirm_Availability.setVisibility(View.GONE);
        Complete_Job.setVisibility(View.GONE);
        img_chat.setVisibility(View.GONE);
        txt_CancelJob.setVisibility(View.GONE);


        img_back_feed.setOnClickListener(this);
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

        final ProgressDialog progressdialog = new ProgressDialog(JobSeekerNotSelectedActivity.this);
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


                                arrayImageList = result.getData().getImageList();


                                if (arrayImageList.size() > 0) {

                                    viewPagerAdapter = new ViewPagerAdapter(arrayImageList, JobSeekerNotSelectedActivity.this);

                                    viewPager.setAdapter(viewPagerAdapter);
                                    sliderDotspanel.removeAllViews();

                                    dotscount = viewPagerAdapter.getCount();
                                    dots = new ImageView[dotscount];

                                    for (int i = 0; i < dotscount; i++) {

                                        dots[i] = new ImageView(JobSeekerNotSelectedActivity.this);
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

                                SimpleDateFormat format = new SimpleDateFormat("d",Locale.getDefault());

                                Date date = null;
                                try {
                                    date = inputFormat.parse(daten);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (daten.endsWith("1") && !daten.endsWith("11"))
                                    format = new SimpleDateFormat("EEE,MMMM d'st'");
                                else if (daten.endsWith("2") && !daten.endsWith("12"))
                                    format = new SimpleDateFormat("EEE,MMMM d'nd'");
                                else if (daten.endsWith("3") && !daten.endsWith("13"))
                                    format = new SimpleDateFormat("EEE,MMMM d'nd'");
                                else
                                    format = new SimpleDateFormat("EEE,MMMM d'th'");

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

                                latitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(JobSeekerNotSelectedActivity.this, AppPrefs.KEY_LAT));

                                longitude = Double.parseDouble(AppPrefs.getStringKeyvaluePrefs(JobSeekerNotSelectedActivity.this, AppPrefs.KEY_LONG));


                                float f = getDistance(new LatLng(Lat, Longs), new LatLng(latitude, longitude));

                                String mile = String.valueOf(Math.round(f));
                                txt_Distance.setText(mile + " Miles Away");


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerNotSelectedActivity.this);
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
                    Toast.makeText(JobSeekerNotSelectedActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
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


            case R.id.card_view:


                if (AppPrefs.getStringKeyvaluePrefs(JobSeekerNotSelectedActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent myIntent = new Intent(JobSeekerNotSelectedActivity.this, JobSeekerPostJobUserDetailActivity.class);
                    myIntent.putExtra("ListerId", ListerId);

                    startActivity(myIntent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobSeekerNotSelectedActivity.this);
                    builder.setMessage(getResources().getString(R.string.loginrequired))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(JobSeekerNotSelectedActivity.this,
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


    @Override
    protected void onResume() {
        super.onResume();

        String Jobid = DataManager.getInstance().getJobID();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getJobDetail(Jobid);

        }

    }

}
