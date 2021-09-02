package com.azzida.ui.notification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.azzida.R;
import com.azzida.adapter.GetSaveCardAdpater;
import com.azzida.adapter.ViewPagerAdapter;
import com.azzida.helper.Config;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.model.GetJobDetailModel;
import com.azzida.model.ImageList;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.ui.activity.ApplicantActivity;
import com.azzida.ui.activity.ChatActivity;
import com.azzida.ui.activity.EditJobActivity;
import com.azzida.ui.activity.JobCompleteActivity;
import com.azzida.ui.activity.JobListerDetailActivity;
import com.azzida.ui.activity.MainActivity;
import com.azzida.ui.activity.PaymentCheckOut;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListerNotificationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final int PICKUP_FROM_SETTING = 20;
    private static final int REQUEST_LOCATION = 1;
    LinearLayout Applicant, Confirmed, PendingConfirmation, CancelJob, CancelJobNe, CancelJob1, RePostJob, Completed;

    ImageView img_share, img_chat;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Boolean OfferAccepted = false;
    ViewPagerAdapter viewPagerAdapter;
    EditText edt_Cancellation_Reason;
    TextView edt_select_date;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog datePickerDialog;
    FusedLocationProviderClient mFusedLocationClient;
    private Date Jobdate;
    private String AmountPay, AmtRef;
    private AlertDialog Card;
    private GoogleMap mMap;
    private Boolean JobComplete;
    private TextView txt_editJob;
    private TextView txt_Description;
    private TextView txt_Applications;
    private TextView txt_Title;
    private TextView txt_Amount;
    private TextView txt_Date, txt_HowLong, txt_JobCategory;
    private TextView txt_Distance;
    private long timeInMilliseconds;
    private String JobId, SeekerId, SeekerProfile, SeekerName, Amount, AmountAdmin, paymentId, TipId;
    private ImageView img_JobPicture;
    private ArrayList<ImageList> arrayImageList;
    private Double Lat, Longs;
    private double latitude, longitude;
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();

            latitude = lat;
            longitude = longi;

            Log.e("mlat", latitude + " " + longitude);
        }
    };
    private int dotscount;
    private ImageView[] dots;
    private LocationManager Manager;
    private GetSaveCardAdpater getSaveCardAdpater;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_job_detail);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                JobId = null;
            } else {
                JobId = extras.getString("JobId");

                DataManager.getInstance().setJobID(JobId);

            }
        } else {
            JobId = (String) savedInstanceState.getSerializable("JobId");
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initView();


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        Manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


/*
        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {


            getJobDetail(JobId);

        }

*/
    }

    private void initView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        arrayImageList = new ArrayList<>();

        TextView title = findViewById(R.id.title);

        title.setText("Back to Feed");

        viewPager = findViewById(R.id.viewPager);

        sliderDotspanel = findViewById(R.id.SliderDots);

        img_JobPicture = findViewById(R.id.img_JobPicture);

        txt_Description = findViewById(R.id.txt_Description);
        txt_Applications = findViewById(R.id.txt_Applications);
        txt_Title = findViewById(R.id.txt_Title);
        txt_Amount = findViewById(R.id.txt_Amount);
        txt_Date = findViewById(R.id.txt_Date);
        txt_HowLong = findViewById(R.id.txt_HowLong);
        txt_JobCategory = findViewById(R.id.txt_JobCategory);
        txt_Distance = findViewById(R.id.txt_Distance);


        Applicant = findViewById(R.id.Applicant);
        Confirmed = findViewById(R.id.Confirmed);
        PendingConfirmation = findViewById(R.id.PendingConfirmation);

        CancelJob = findViewById(R.id.CancelJob);
        CancelJobNe = findViewById(R.id.CancelJobNe);
        CancelJob1 = findViewById(R.id.CancelJob1);

        RePostJob = findViewById(R.id.RePostJob);

        Completed = findViewById(R.id.Completed);

        img_chat = findViewById(R.id.img_chat);
        img_share = findViewById(R.id.img_share);

        txt_editJob = findViewById(R.id.txt_editJob);


        CardView mapCard = findViewById(R.id.mapCard);

        Applicant.setOnClickListener(this);
        Confirmed.setOnClickListener(this);
        PendingConfirmation.setOnClickListener(this);

        CancelJob.setOnClickListener(this);
        CancelJobNe.setOnClickListener(this);
        CancelJob1.setOnClickListener(this);

        RePostJob.setOnClickListener(this);

        CancelJob.setOnClickListener(this);
        CancelJobNe.setOnClickListener(this);
        CancelJob1.setOnClickListener(this);

        findViewById(R.id.img_back_feed).setOnClickListener(this);
        img_chat.setOnClickListener(this);
        img_share.setOnClickListener(this);

        txt_editJob.setOnClickListener(this);


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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = Manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            List<String> providers = Manager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = Manager.getLastKnownLocation(provider);

                if (l == null) {
                    continue;
                }
                if (bestLocation == null
                        || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }

            if (bestLocation != null) {
                double lat = bestLocation.getLatitude();
                double longi = bestLocation.getLongitude();
                latitude = lat;
                longitude = longi;

            } else {
                getLastLocation();
            }
        }
    }

    private void getLastLocation() {
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {

                                double lat = location.getLatitude();
                                double longi = location.getLongitude();

                                latitude = lat;
                                longitude = longi;

                                Log.e("lat", latitude + " " + longitude);

                            }
                        }
                    }
            );
        } else {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // intent.setType("image/*");
                startActivityForResult(intent, PICKUP_FROM_SETTING);
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void getJobDetail(String JobId) {

        final ProgressDialog progressdialog = new ProgressDialog(JobListerNotificationActivity.this);
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


                                if (result.getData().getApplicationAccepted()) {

                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.VISIBLE);
                                    CancelJob.setVisibility(View.VISIBLE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.VISIBLE);
                                    txt_editJob.setVisibility(View.VISIBLE);


                                } else {

                                    Applicant.setVisibility(View.VISIBLE);
                                    CancelJob1.setVisibility(View.VISIBLE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.VISIBLE);
                                    txt_editJob.setVisibility(View.VISIBLE);

                                }

                                if (result.getData().getOfferAccepted()) {

                                    OfferAccepted = result.getData().getOfferAccepted();
                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.VISIBLE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.VISIBLE);
                                    img_share.setVisibility(View.GONE);
                                    txt_editJob.setVisibility(View.GONE);


                                } else if (result.getData().getApplicationAccepted()) {

                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.VISIBLE);
                                    CancelJob.setVisibility(View.VISIBLE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.VISIBLE);
                                    txt_editJob.setVisibility(View.VISIBLE);


                                } else {

                                    Applicant.setVisibility(View.VISIBLE);
                                    CancelJob1.setVisibility(View.VISIBLE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.VISIBLE);
                                    txt_editJob.setVisibility(View.VISIBLE);

                                }

                                if (result.getData().getStatus().equals("Expired")) {

                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.GONE);
                                    txt_editJob.setVisibility(View.GONE);
                                    RePostJob.setVisibility(View.VISIBLE);


                                }

                                if (result.getData().getStatus().equals("Cancelled")) {

                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.GONE);
                                    txt_editJob.setVisibility(View.GONE);
                                    RePostJob.setVisibility(View.VISIBLE);


                                }

                                if (result.getData().getApplicantCount() != 0) {

                                    txt_editJob.setVisibility(View.GONE);

                                }

                                if (result.getData().getJobComplete()) {

                                    Applicant.setVisibility(View.GONE);
                                    CancelJob1.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    CancelJobNe.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.GONE);
                                    txt_editJob.setVisibility(View.GONE);
                                    RePostJob.setVisibility(View.GONE);
                                    Completed.setVisibility(View.VISIBLE);


                                }

                                JobComplete = result.getData().getComplete();

                                if (JobComplete) {
                                    final int sdk = android.os.Build.VERSION.SDK_INT;
                                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        Confirmed.setBackgroundDrawable(ContextCompat.getDrawable(JobListerNotificationActivity.this, R.drawable.border_green));
                                    } else {
                                        Confirmed.setBackground(ContextCompat.getDrawable(JobListerNotificationActivity.this, R.drawable.border_green));
                                    }
                                    CancelJobNe.setVisibility(View.GONE);
                                    CancelJob.setVisibility(View.GONE);
                                }

                                SeekerId = String.valueOf(result.getData().getSeekerId());
                                TipId = String.valueOf(result.getData().getTipId());
                                SeekerProfile = String.valueOf(result.getData().getSeekerimage());
                                AmountAdmin = String.valueOf(result.getData().getAmountWithAdminCharges());
                                SeekerName = String.valueOf(result.getData().getSeekerName());
                                arrayImageList = result.getData().getImageList();

                              /*  if (result.getData().getComplete()) {


                                    Applicant.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.VISIBLE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.VISIBLE);
                                    img_share.setVisibility(View.GONE);


                                }else if (result.getData().getOfferAccepted()) {


                                    Applicant.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.VISIBLE);
                                    PendingConfirmation.setVisibility(View.GONE);
                                    img_chat.setVisibility(View.VISIBLE);
                                    img_share.setVisibility(View.GONE);


                                }else if (result.getData().getApplicationAccepted()) {

                                    Applicant.setVisibility(View.GONE);
                                    Confirmed.setVisibility(View.GONE);
                                    PendingConfirmation.setVisibility(View.VISIBLE);
                                    img_chat.setVisibility(View.GONE);
                                    img_share.setVisibility(View.GONE);


                                }
*/


                                if (arrayImageList.size() > 0) {

                                    viewPagerAdapter = new ViewPagerAdapter(arrayImageList, JobListerNotificationActivity.this);
                                    sliderDotspanel.removeAllViews();
                                    viewPager.setAdapter(viewPagerAdapter);

                                    dotscount = viewPagerAdapter.getCount();
                                    dots = new ImageView[dotscount];

                                    for (int i = 0; i < dotscount; i++) {

                                        dots[i] = new ImageView(JobListerNotificationActivity.this);
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
                                txt_Applications.setText(result.getData().getApplicantCount() + " " + "Applications");
                                txt_Title.setText(result.getData().getJobTitle());

                                txt_HowLong.setText(result.getData().getHowLong());
                                txt_JobCategory.setText(result.getData().getJobCategory());

                                paymentId = result.getData().getPymntId();

                                String roundedBalance = result.getData().getAmount().replaceAll("\\.0*$", "");

                                String daten = DateFormat.format("yyyy-MM-d", Long.parseLong(result.getData().getFromDate().replaceAll("\\.0*$", ""))).toString();

                                String JobDate = DateFormat.format("EEE MMM dd HH:mm:ss z yyyy", Long.parseLong(result.getData().getFromDate().replaceAll("\\.0*$", ""))).toString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());
                                try {
                                    Jobdate = dateFormat.parse(JobDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());

                                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());

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
                                txt_Amount.setText("$" + roundedBalance);

                                Amount = roundedBalance;

                                Lat = Double.valueOf(result.getData().getLatitude());
                                Longs = Double.valueOf(result.getData().getLongitude());

                                LatLng location = new LatLng(Lat, Longs);

/*
                                mMap.addMarker(new MarkerOptions().position(location));
*/

                                mMap.addCircle(new CircleOptions()
                                        .center(location)
                                        .radius(100)
                                        .strokeColor(0x220000FF)
                                        .fillColor(0x220000FF)
                                        .strokeWidth(5)
                                );

/*
                                                                        .fillColor(Color.parseColor("#2271cce7")))
*/

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                                // Zoom in, animating the camera.
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

                                float f = getDistance(new LatLng(Lat, Longs), new LatLng(latitude, longitude));

                                String mile = String.valueOf(Math.round(f));
                                txt_Distance.setText(mile + " Miles Away");


                            } else {

                            }

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
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
                    Toast.makeText(JobListerNotificationActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobDetailModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            Intent intent = new Intent(JobListerNotificationActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();

        } else if (requestCode == PICKUP_FROM_SETTING) {

            Manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (Manager != null)
                if (!Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {

                    getLocation();

                }

        } else if (resultCode == 5) {

            Intent intent = new Intent(JobListerNotificationActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back_feed:


                Intent intent = new Intent(JobListerNotificationActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();


                break;

            case R.id.img_chat:


                Intent Chat = new Intent(JobListerNotificationActivity.this, ChatActivity.class);
                Chat.putExtra("UserId", SeekerId);
                Chat.putExtra("Profile", SeekerProfile);
                Chat.putExtra("Name", SeekerName);
                Chat.putExtra("JobId", JobId);
                startActivity(Chat);


                break;


            case R.id.img_share:

                Utils.createDynamicLink_Advanced(JobListerNotificationActivity.this, JobId,
                        AppPrefs.getStringKeyvaluePrefs(JobListerNotificationActivity.this, AppPrefs.KEY_User_ID), "Here's a job posting that may be of interest to you");


                break;


            case R.id.txt_editJob:

                Intent myIntent = new Intent(JobListerNotificationActivity.this, EditJobActivity.class);
                startActivity(myIntent);

                break;

            case R.id.Applicant:

                Intent Intent1 = new Intent(JobListerNotificationActivity.this, ApplicantActivity.class);
                Intent1.putExtra("JobId", JobId);
                Intent1.putExtra("Amount", AmountAdmin);

                startActivityForResult(Intent1, 2);


                break;

            case R.id.RePostJob:

                AlertDialog.Builder RePost = new AlertDialog.Builder(JobListerNotificationActivity.this);

                CardView RePostview = (CardView) JobListerNotificationActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_repost, null);

                Button btn_close_repost = RePostview.findViewById(R.id.btn_close);

                Button btn_submit_repost = RePostview.findViewById(R.id.btn_submit);
                myCalendar = Calendar.getInstance();
                edt_select_date = RePostview.findViewById(R.id.edt_select_date);

                edt_select_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        datePickerDialog = new DatePickerDialog(JobListerNotificationActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });


                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edt_select_date.setText(sdf.format(myCalendar.getTime()));
                        edt_select_date.setError(null);
                        edt_select_date.setTextColor(ContextCompat.getColor(JobListerNotificationActivity.this, R.color.black));

                        String date = String.valueOf(myCalendar.getTime());
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


                        Log.e("TAG", String.valueOf(myCalendar.getTime()));


                    }


                };

                RePost.setView(RePostview);

                final AlertDialog RePostd = RePost.create();

                RePostd.setCanceledOnTouchOutside(false);
                RePostd.setCancelable(false);
                RePostd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                RePostd.show();

                btn_close_repost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        RePostd.cancel();

                    }
                });


                btn_submit_repost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String status = NetworkUtils.getConnectivityStatus(JobListerNotificationActivity.this);
                        if (status.equals("404")) {
                            Toast.makeText(JobListerNotificationActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {
                            if (validatedate()) {

                                RePostJobApi();
                            }


                        }


                    }
                });


                break;


            case R.id.CancelJob:
                String msg = "You hereby confirm that " + SeekerName + " has not completed " + txt_Title.getText().toString() + " and no payment is due at this time";

                CancelJobPopUp(msg);


                break;

            case R.id.CancelApplication:
                String msgd = "You hereby confirm that " + SeekerName + " has not completed " + txt_Title.getText().toString() + " and no payment is due at this time.";

                CancelJobPopUpNew(msgd);


                break;

            case R.id.CancelJob1:

                CancelJobPopUp("**I hereby certify that the job has not been completed and that no payment is currently due");

                break;

            case R.id.CancelJobNe:
                String msgs = "**You hereby confirm that " + SeekerName + " has not completed the job and no payment is due at this time. Please contact the Job Performer through the Azzida Chat message system on the Job Details screen if Cancellation is not confirmed within 24 hours";

                CancelJobPopUp(msgs);

                break;


            case R.id.Confirmed:

                if (JobComplete) {

                    Intent Confirmed = new Intent(JobListerNotificationActivity.this, JobCompleteActivity.class);
                    Confirmed.putExtra("SeekerId", SeekerId);
                    Confirmed.putExtra("Amount", Amount);
                    Confirmed.putExtra("TipId", TipId);
                    Confirmed.putExtra("JobId", JobId);
                    Confirmed.putExtra("paymentId", paymentId);
                    startActivityForResult(Confirmed, 2);

                }
                break;

        }

    }

    private void CancelJobPopUp(String msg) {


        AlertDialog.Builder b = new AlertDialog.Builder(JobListerNotificationActivity.this);

        CardView view = (CardView) JobListerNotificationActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_cancel, null);

        Button btn_close = view.findViewById(R.id.btn_close);

        Button btn_submit = view.findViewById(R.id.btn_submit);

        TextView txt_cancelText = view.findViewById(R.id.txt_cancelText);

        if (msg.length() > 0) {

            txt_cancelText.setText(msg);

        } else {
            txt_cancelText.setVisibility(View.GONE);
        }

        edt_Cancellation_Reason = view.findViewById(R.id.edt_Cancellation_Reason);

        b.setView(view);

        final AlertDialog d = b.create();

        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d.show();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d.cancel();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = NetworkUtils.getConnectivityStatus(JobListerNotificationActivity.this);
                if (status.equals("404")) {
                    Toast.makeText(JobListerNotificationActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    if (validateReason()) {

/*
                        if (Utils.getDateDiffString(Jobdate, new Date()) < 24) {

                            getSaveCard();

                            d.cancel();

                        } else {

                            CancelJobApi();

                            d.cancel();

                        }
*/
                        CancelJobApi();
                        d.cancel();


                    }


                }


            }
        });


    }

    private void CancelJobPopUpNew(String msg) {

        AlertDialog.Builder b11 = new AlertDialog.Builder(JobListerNotificationActivity.this);

        CardView view11 = (CardView) JobListerNotificationActivity.this.getLayoutInflater().inflate(R.layout.dialog_job_cancel, null);

        Button btn_close1 = view11.findViewById(R.id.btn_close);

        Button btn_submit1 = view11.findViewById(R.id.btn_submit);
        TextView txt_cancelText = view11.findViewById(R.id.txt_cancelText);

        if (msg.length() > 0){

            txt_cancelText.setText(msg);

        }else {
            txt_cancelText.setVisibility(View.GONE);
        }

        edt_Cancellation_Reason = view11.findViewById(R.id.edt_Cancellation_Reason);

        b11.setView(view11);

        final AlertDialog d11 = b11.create();

        d11.setCanceledOnTouchOutside(false);
        d11.setCancelable(false);
        d11.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d11.show();

        btn_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d11.cancel();

            }
        });


        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = NetworkUtils.getConnectivityStatus(JobListerNotificationActivity.this);
                if (status.equals("404")) {
                    Toast.makeText(JobListerNotificationActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                } else {
                    if (validateReason()) {

/*                        if (Utils.getDateDiffString(Jobdate, new Date()) < 24) {

                            getSaveCard();
                            d1.cancel();

                        } else {

                            CancelJobApi();
                            d1.cancel();

                        }*/
                        CancelApplicationApi();
                        d11.cancel();

                    }


                }


            }
        });


    }


    private void ShowPaymentPopUp() {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            try {

                String s_Balance = AppPrefs.getStringKeyvaluePrefs(JobListerNotificationActivity.this, AppPrefs.KEY_Balance);

                String CancelAmount = "";
                double amount = Double.parseDouble(txt_Amount.getText().toString().replace("$", ""));
                double res = (amount * 3) / 100;
                CancelAmount = String.valueOf(res);

                if (res > 3.0) {

                    CancelAmount = String.valueOf(res);

                } else {

                    CancelAmount = "10.00";

                }

                double bal1 = Double.parseDouble(CancelAmount);
                double bal2 = Double.parseDouble(s_Balance);

                if (bal2 > bal1) {

                    String Msg = "Amount debited from Referral Balance: $" + CancelAmount;

                    AmountPay = CancelAmount;
                    AmtRef = CancelAmount;

                    AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
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

                    AlertDialog.Builder b = new AlertDialog.Builder(JobListerNotificationActivity.this, R.style.MyCustomTheme);

                    LinearLayout PaymentView = (LinearLayout) JobListerNotificationActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment, null);

                    TextView Txt_Direct_PAy = PaymentView.findViewById(R.id.Txt_Direct_PAy);
                    RecyclerView recyclerPayment = PaymentView.findViewById(R.id.RecyclerPayment);
                    final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerPayment.setLayoutManager(mLayoutManager);
                    recyclerPayment.setHasFixedSize(true);
                    recyclerPayment.setItemAnimator(new DefaultItemAnimator());
                    recyclerPayment.setAdapter(getSaveCardAdpater);
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

                    String finalCancelAmount = CancelAmount;
                    Txt_Direct_PAy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Card.cancel();
                            Intent myIntent = new Intent(JobListerNotificationActivity.this, PaymentCheckOut.class);
                            myIntent.putExtra("SeekerId", "0");
                            myIntent.putExtra("TotalAmount", finalCancelAmount);
                            myIntent.putExtra("JobId", JobId);
                            myIntent.putExtra("Reason", edt_Cancellation_Reason.getText().toString());
                            myIntent.putExtra("PaymentType", "CancelJob");
                            myIntent.putExtra("Screen", "5");
                            startActivityForResult(myIntent, 5);

                        }
                    });


                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    private void RePostJobApi() {
        final ProgressDialog progressdialog = new ProgressDialog(JobListerNotificationActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_date = String.valueOf(timeInMilliseconds);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<SuccessModel> call = service.RePostJob(JobId, s_userId, s_date);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
                        builder.setMessage("Job Re-Posted Successfully")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(JobListerNotificationActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    public boolean validatedate() {
        boolean valid = true;

        String s_date = edt_select_date.getText().toString();


        if (s_date.isEmpty()) {
            edt_select_date.setError("Enter Date");
            edt_select_date.requestFocus();
            valid = false;
        } else {
            edt_select_date.setError(null);
        }

        return valid;
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


    private void CancelJobApi() {

        final ProgressDialog progressdialog = new ProgressDialog(JobListerNotificationActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Alert");
                        alert.show();
                    }

                } else {
                    // Server Problem
                    Toast.makeText(JobListerNotificationActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void CancelApplicationApi() {

        final ProgressDialog progressdialog = new ProgressDialog(JobListerNotificationActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<SuccessModel> call = service.CancelSeekerJobApplication(s_userId, JobId);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Alert");
                        alert.show();
                    }

                } else {
                    // Server Problem
                    Toast.makeText(JobListerNotificationActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(JobListerNotificationActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void getSaveCard() {

        String s_userId = AppPrefs.getStringKeyvaluePrefs(JobListerNotificationActivity.this, AppPrefs.KEY_User_ID);
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

                            getSaveCardAdpater = new GetSaveCardAdpater(JobListerNotificationActivity.this, new GetSaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    try {

                                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(JobListerNotificationActivity.this, AppPrefs.KEY_Balance);

                                        String CancelAmount = "";
                                        double amount = Double.parseDouble(txt_Amount.getText().toString().replace("$", ""));
                                        double res = (amount * 3) / 100;
                                        CancelAmount = String.valueOf(res);

                                        if (res > 3.0) {

                                            CancelAmount = String.valueOf(res);

                                        } else {

                                            CancelAmount = "10.00";

                                        }

                                        double bal1 = Double.parseDouble(CancelAmount);
                                        double bal2 = Double.parseDouble(s_Balance);
                                        double tt = bal1 + bal2;
                                        double Total = 0.0;

                                        DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                        String formatted = formatter.format(bal1);

                                        String Msg = "";

                                        if (s_Balance.equalsIgnoreCase("0")) {

                                            Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                            Msg = Msg + "\n" + "Amount to be paid: $" + formatted;
                                            AmountPay = CancelAmount;
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


                                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
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

                            ShowPaymentPopUp();

                        }


                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {

                ShowPaymentPopUp();

            }
        });
    }

    private void createPayment(String customerId) {

        final ProgressDialog progressdialog = new ProgressDialog(JobListerNotificationActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, "0", AmtRef, customerId, AmountPay, "", "CancelJob", "", Config.Token_Used);
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

                                    AppPrefs.setStringKeyvaluePrefs(JobListerNotificationActivity.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    Card.cancel();
                                    CancelJobApi();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(JobListerNotificationActivity.this);
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
                    Toast.makeText(JobListerNotificationActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }


}
