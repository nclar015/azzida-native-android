package com.azzida.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.BuildConfig;
import com.azzida.R;
import com.azzida.adapter.CategoryAdpater;
import com.azzida.helper.Config;
import com.azzida.helper.CustomGooglePlacesSearch;
import com.azzida.helper.StripeConnect;
import com.azzida.manager.GoogleAuthManager;
import com.azzida.model.GetJobCategoryModel;
import com.azzida.model.GetJobCategoryModelDatum;
import com.azzida.model.GetJobFeeModel;
import com.azzida.model.ReferalBalanceModel;
import com.azzida.model.SignInModel;
import com.azzida.model.SuccessModel;
import com.azzida.model.map.AddressComponent;
import com.azzida.model.map.MapModel;
import com.azzida.model.map.Result;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.ApiUrls;
import com.azzida.ui.fragment.JobSeekerFragment;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int REQUEST_LOCATION = 1;
    private static final int PICKUP_FROM_SETTING = 20;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE = 18945;
    public int ADD_ACCOUNT = 30;
    String AddressLoc;
    String LatT, LongT;
    TextView edt_search_Location;
    FusedLocationProviderClient mFusedLocationClient;
    private FragmentManager fm;
    private LinearLayout locPop;
    private ImageView navigation_menu, filter;
    private LinearLayout profile, frame, Feed, Logout, Login, About, Faq, ShareApp, Stripe, Options;
    /*
        private LinearLayout MyListing, MyJob;
    */
    private TextView Web_Privacy, Web_Fees, Web_Terms;
    private CircleImageView header_profile_image;
    private TextView header_FullName;
    private LinearLayout LL_header_Name;
    private TextView picLocation;
    private String address1, address2, city, state, country, county, PIN;
    private LocationManager Manager;
    private double latitude, longitude;
    private RecyclerView RecyclerCategories;
    private CategoryAdpater categoryAdpater;
    private ArrayList<GetJobCategoryModelDatum> arrayCategory;
    private List<Result> arrayResult;
    private List<AddressComponent> arrayAddressComponent;
    private List<String> type;
    private double latitudeInt, longitudeInt;
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();

            latitude = lat;
            longitude = longi;

            latitudeInt = lat;
            longitudeInt = longi;
            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LAT, String.valueOf(latitudeInt));
            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LONG, String.valueOf(longitudeInt));

            getReverseGeoCoding();
            Log.e("mlat", latitude + " " + longitude);
        }
    };
    private DrawerLayout drawerLayout;
    private PopupWindow mPopupWindow;
    /*
        private CheckBox chk1, chk2, chk3, chk4, chk5, chk6;
    */
    private ArrayList<String> list;
    private SeekBar seekbar_Distance;
    private SwitchCompat switchShowHide;
    private EditText edt_MinPrice, edt_MaxPrice;
    private ImageView linkedin, instagram, facebook;
    private TextView txt_Distance;
    private LinearLayout Apply_Filters, Clear_Filters;

    public static void openFacebookIntent(Context context, String facebookID) {

        String url = "https://www.facebook.com/" + facebookID;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.facebook.katana");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

    }

    public static void openLinkedinIntent(Context context, String linkedinID) {

        String profile_url = "https://www.linkedin.com/company/" + linkedinID;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(profile_url));
            intent.setPackage("com.linkedin.android");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(profile_url)));
        }

    }

    public static void openInstagramIntent(Context context, String instagramID) {

        Uri uri = Uri.parse("http://instagram.com/_u/" + instagramID);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.setPackage("com.instagram.android");

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagramID)));
        }
    }

    public String getFacebookPageURL(Context context, String facebookID) {

        String url = "https://www.facebook.com/" + facebookID;

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + url;
            } else { //older versions of fb app
                return "fb://page/" + facebookID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return url; //normal web url
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayFirebaseRegId();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        Manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        InitView();

    }

    private void displayFirebaseRegId() {

        String regId = AppPrefs.getStringKeyvaluePrefsNew(MainActivity.this, AppPrefs.KEY_DEVICE_ID);

        Log.e("TAG", "Firebase reg id: " + regId);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        if (Manager != null)
                            if (!Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                OnGPS();
                            } else {

                                getLocation();
                            }

                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    }
                }
            }
        }
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

    private void InitView() {

        fm = getSupportFragmentManager();

        RecyclerCategories = findViewById(R.id.RecyclerCategories);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        RecyclerCategories.setLayoutManager(mLayoutManager);
        RecyclerCategories.setHasFixedSize(true);
        RecyclerCategories.setItemAnimator(new DefaultItemAnimator());

        Logout = findViewById(R.id.Logout);
        Login = findViewById(R.id.Login);

        CallHomeScreen();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getJobCategory();
            getJobFee();

            if (!AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                    AppPrefs.KEY_USER_LOGIN_DEMO).
                    equalsIgnoreCase("true")) {

                getReferalBalance();


            }

        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigation_menu = findViewById(R.id.navigation_menu);
        filter = findViewById(R.id.filter);

        profile = findViewById(R.id.profile);
        About = findViewById(R.id.About);
        ShareApp = findViewById(R.id.ShareApp);
        Stripe = findViewById(R.id.Stripe);
        Options = findViewById(R.id.Options);
/*
        Invest = findViewById(R.id.Invest);
*/
        Faq = findViewById(R.id.Faq);

        linkedin = findViewById(R.id.linkedin);
        instagram = findViewById(R.id.instagram);
        facebook = findViewById(R.id.facebook);
/*
        MyListing = findViewById(R.id.MyListing);
        MyJob = findViewById(R.id.MyJob);
*/
        Feed = findViewById(R.id.Feed);


        Web_Privacy = findViewById(R.id.Web_Privacy);
        Web_Fees = findViewById(R.id.Web_Fees);
        Web_Terms = findViewById(R.id.Web_Terms);

        header_profile_image = findViewById(R.id.header_profile_image);
        header_FullName = findViewById(R.id.header_FullName);
        LL_header_Name = findViewById(R.id.LL_header_Name);

        header_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDrawer();
                startActivity(new Intent(MainActivity.this, MyProfileActivity.class));

            }
        });


        LL_header_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDrawer();
                startActivity(new Intent(MainActivity.this, MyProfileActivity.class));


            }
        });


        list = new ArrayList<String>();


/*
        chk1 = findViewById(R.id.checkBox1);
        chk2 = findViewById(R.id.checkBox2);
        chk3 = findViewById(R.id.checkBox3);
        chk4 = findViewById(R.id.checkBox4);
        chk5 = findViewById(R.id.checkBox5);
        chk6 = findViewById(R.id.checkBox6);

*/

        frame = findViewById(R.id.frame);


        txt_Distance = findViewById(R.id.txt_Distance);

        seekbar_Distance = findViewById(R.id.seekbar_Distance);
        switchShowHide = findViewById(R.id.switchShowHide);

        edt_MinPrice = findViewById(R.id.edt_MinPrice);
        edt_MaxPrice = findViewById(R.id.edt_MaxPrice);

        String Min = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMin);
        String Max = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMax);

        if (Min != null && Min.length() > 0) {

            edt_MinPrice.setText(Min);

        }

        if (Max != null && Max.length() > 0) {

            edt_MaxPrice.setText(Max);

        }

        String Dis = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Distance);

        String switc = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch);


        if (switc != null && switc.length() > 0) {

            switchShowHide.setChecked(switc.equalsIgnoreCase("true"));


        }else {

            switchShowHide.setChecked(true);
            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch, String.valueOf(switchShowHide.isChecked()));
        }

        if (Dis != null && Dis.length() > 0) {

                seekbar_Distance.setProgress(Integer.parseInt(Dis));

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Distance, Dis);

                txt_Distance.setText("within" + " " + Dis + " " + "miles");

            } else {

                seekbar_Distance.setProgress(25);

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Distance, String.valueOf(seekbar_Distance.getProgress()));

                txt_Distance.setText("within" + " " + seekbar_Distance.getProgress() + " " + "miles");

        }


        seekbar_Distance.setOnTouchListener(new SeekBar.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        // Disallow Drawer to intercept touch events.
                        // v.getParent().requestDisallowInterceptTouchEvent(true);

                        //ib_navigation.setImageResource(R.mipmap.back_arrow);

                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.END);


                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Allow Drawer to intercept touch events.
                        // v.getParent().requestDisallowInterceptTouchEvent(false);

                        //ib_navigation.setImageResource(R.mipmap.back_arrow);

                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, GravityCompat.END);
                        drawerLayout.closeDrawer(GravityCompat.START);


                        break;
                }

                // Handle SeekBar touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        seekbar_Distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgressDistance = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {


                seekBarProgressDistance = progress;

                txt_Distance.setText("within" + " " + seekBarProgressDistance + " " + "miles");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        Apply_Filters = findViewById(R.id.Apply_Filters);
        Clear_Filters = findViewById(R.id.Clear_Filters);


        picLocation = findViewById(R.id.picLocation);
        locPop = findViewById(R.id.locPop);

        navigation_menu.setOnClickListener(this);
        filter.setOnClickListener(this);

        profile.setOnClickListener(this);
        About.setOnClickListener(this);

        linkedin.setOnClickListener(this);
        instagram.setOnClickListener(this);
        facebook.setOnClickListener(this);
/*
        MyListing.setOnClickListener(this);
        MyJob.setOnClickListener(this);
*/
        Feed.setOnClickListener(this);
        Logout.setOnClickListener(this);
        Login.setOnClickListener(this);

        Web_Privacy.setOnClickListener(this);
        Web_Fees.setOnClickListener(this);
        Web_Terms.setOnClickListener(this);

        locPop.setOnClickListener(this);
        ShareApp.setOnClickListener(this);
        Stripe.setOnClickListener(this);
        Options.setOnClickListener(this);
/*
        Invest.setOnClickListener(this);
*/
        Faq.setOnClickListener(this);


        Apply_Filters.setOnClickListener(this);
        Clear_Filters.setOnClickListener(this);

    }

    private void getJobFee() {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetJobFeeModel> call = service.GetJobFee();
        call.enqueue(new Callback<GetJobFeeModel>() {


            @Override
            public void onResponse(@NonNull Call<GetJobFeeModel> call, @NonNull Response<GetJobFeeModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetJobFeeModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_BackgroundCheck, result.getData().getBackgroundCheck());
                            if (result.getData().getCancelJobFee() == null && result.getData().getCancelJobFee().equals("null")) {

                                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_CancelJobFee, "0");

                            } else {

                                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_CancelJobFee, result.getData().getCancelJobFee());


                            }


                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobFeeModel> call, @NonNull Throwable t) {

            }
        });
    }


    private void getReferalBalance() {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ID);
        Call<ReferalBalanceModel> call = service.GetReferalBalance(s_userId);
        call.enqueue(new Callback<ReferalBalanceModel>() {

            @Override
            public void onResponse(@NonNull Call<ReferalBalanceModel> call, @NonNull Response<ReferalBalanceModel> response) {

                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ReferalBalanceModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Balance, result.getData().getAmount().replaceAll("\\.0*$", ""));


                        }
                    } else {

                        AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Balance, "0");

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ReferalBalanceModel> call, @NonNull Throwable t) {
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKUP_FROM_SETTING) {

            Manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (Manager != null)
                if (!Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {

                    getLocation();

                }

        } else if (resultCode == ADD_ACCOUNT) {

            RetrieveStripeAccount(DataManager.getInstance().getStripeCode(), AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ID), "");

        }


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                latitudeInt = lat;
                longitudeInt = longi;

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LAT, String.valueOf(latitudeInt));
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LONG, String.valueOf(longitudeInt));
                getReverseGeoCoding();
/*
                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
*/
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

                                latitudeInt = lat;
                                longitudeInt = longi;
                                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LAT, String.valueOf(latitudeInt));
                                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LONG, String.valueOf(longitudeInt));
                                getReverseGeoCoding();
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


    private void getReverseGeoCoding() {

        final String[] Address1 = {""};
        final String[] Address2 = {""};
        final String[] City = {""};
        final String[] State = {""};
        final String[] Country = {""};
        final String[] County = {""};
        final String[] PIN = {""};

        Address1[0] = "";
        Address2[0] = "";
        City[0] = "";
        State[0] = "";
        Country[0] = "";
        County[0] = "";
        PIN[0] = "";

        try {
            ApiService service = ApiClient.getNewClient().create(ApiService.class);
            Call<MapModel> call = service.Map(latitude + "," + longitude, "true", getResources().getString(R.string.google_maps_key));
            call.enqueue(new Callback<MapModel>() {

                @Override
                public void onResponse(@NonNull Call<MapModel> call, @NonNull Response<MapModel> response) {
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        MapModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("OK")) {

                            arrayResult = result.getResults();

                            arrayAddressComponent = arrayResult.get(0).getAddressComponents();


                            for (int i = 0; i < arrayAddressComponent.size(); i++) {

                                String long_name = arrayAddressComponent.get(i).getLongName();

                                type = arrayAddressComponent.get(i).getTypes();

                                String Type = type.get(0);

                                if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                    if (Type.equalsIgnoreCase("street_number")) {
                                        Address1[0] = long_name + " ";
                                    } else if (Type.equalsIgnoreCase("route")) {
                                        Address1[0] = Address1[0] + long_name;
                                    } else if (Type.equalsIgnoreCase("sublocality")) {
                                        Address2[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("locality")) {
                                        // Address2 = Address2 + long_name + ", ";
                                        City[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                        County[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                        State[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("country")) {
                                        Country[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("postal_code")) {
                                        PIN[0] = long_name;
                                    }
                                }

                                // JSONArray mtypes = zero2.getJSONArray("types");
                                // String Type = mtypes.getString(0);
                                // Log.e(Type,long_name);
                            }

                            Log.e("TAG", "onResponse: " + Address1[0] + Address2[0] +
                                    City[0] + State[0] + County[0]);


                            String currentLocation;


                            if (!TextUtils.isEmpty(City[0])) {
                                currentLocation = City[0];

                                if (!TextUtils.isEmpty(State[0]))
                                    currentLocation += "," + State[0];

                                picLocation.setText(currentLocation);

                            } else if (!TextUtils.isEmpty(County[0])) {
                                currentLocation = County[0];

                                if (!TextUtils.isEmpty(State[0]))
                                    currentLocation += "," + State[0];

                                picLocation.setText(currentLocation);

                            } else {

                                if (!TextUtils.isEmpty(State[0])) {

                                    currentLocation = State[0];
                                    picLocation.setText(currentLocation);
                                }


                            }

                        }

                    } else {
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MapModel> call, @NonNull Throwable t) {
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                AppPrefs.KEY_USER_LOGIN_DEMO).
                equalsIgnoreCase("true")) {

            updatLatlong();

        }
    }

    private void updatLatlong() {

        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ID);
        String s_lat = String.valueOf(latitude);
        String s_long = String.valueOf(longitude);
        Call<SignInModel> call = service.UpdateUserLatLong(s_userId, s_lat, s_long);
        call.enqueue(new Callback<SignInModel>() {


            @Override
            public void onResponse(@NonNull Call<SignInModel> call, @NonNull Response<SignInModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SignInModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());


                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
            }
        });

    }

    private void getReverseGeoCodingNew() {

        latitudeInt = Double.parseDouble(LatT);
        longitudeInt = Double.parseDouble(LongT);
        AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LAT, String.valueOf(latitudeInt));
        AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_LONG, String.valueOf(longitudeInt));
 /*       DataManager.getInstance().setAddressMain("");
        DataManager.getInstance().setLatMain("");
        DataManager.getInstance().setLongMain("");*/

        final String[] Address1 = {""};
        final String[] Address2 = {""};
        final String[] City = {""};
        final String[] State = {""};
        final String[] Country = {""};
        final String[] County = {""};
        final String[] PIN = {""};

        Address1[0] = "";
        Address2[0] = "";
        City[0] = "";
        State[0] = "";
        Country[0] = "";
        County[0] = "";
        PIN[0] = "";

        try {
            ApiService service = ApiClient.getNewClient().create(ApiService.class);
            Call<MapModel> call = service.Map(LatT + "," + LongT, "true", getResources().getString(R.string.google_maps_key));
            call.enqueue(new Callback<MapModel>() {

                @Override
                public void onResponse(@NonNull Call<MapModel> call, @NonNull Response<MapModel> response) {
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        MapModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("OK")) {

                            arrayResult = result.getResults();

                            arrayAddressComponent = arrayResult.get(0).getAddressComponents();


                            for (int i = 0; i < arrayAddressComponent.size(); i++) {

                                String long_name = arrayAddressComponent.get(i).getLongName();

                                type = arrayAddressComponent.get(i).getTypes();

                                String Type = type.get(0);

                                if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                    if (Type.equalsIgnoreCase("street_number")) {
                                        Address1[0] = long_name + " ";
                                    } else if (Type.equalsIgnoreCase("route")) {
                                        Address1[0] = Address1[0] + long_name;
                                    } else if (Type.equalsIgnoreCase("sublocality")) {
                                        Address2[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("locality")) {
                                        // Address2 = Address2 + long_name + ", ";
                                        City[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                        County[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                        State[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("country")) {
                                        Country[0] = long_name;
                                    } else if (Type.equalsIgnoreCase("postal_code")) {
                                        PIN[0] = long_name;
                                    }
                                }

                                // JSONArray mtypes = zero2.getJSONArray("types");
                                // String Type = mtypes.getString(0);
                                // Log.e(Type,long_name);
                            }

                            Log.e("TAG", "onResponse: " + Address1[0] + Address2[0] +
                                    City[0] + State[0] + County[0]);


                            String currentLocation;


                            if (!TextUtils.isEmpty(City[0])) {
                                currentLocation = City[0];

                                if (!TextUtils.isEmpty(State[0]))
                                    currentLocation += "," + State[0];

                                picLocation.setText(currentLocation);

                            } else if (!TextUtils.isEmpty(County[0])) {
                                currentLocation = County[0];

                                if (!TextUtils.isEmpty(State[0]))
                                    currentLocation += "," + State[0];

                                picLocation.setText(currentLocation);

                            } else {

                                if (!TextUtils.isEmpty(State[0])) {

                                    currentLocation = State[0];
                                    picLocation.setText(currentLocation);
                                }


                            }

                        }

                    } else {
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MapModel> call, @NonNull Throwable t) {
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.navigation_menu:

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    //ib_navigation.setImageResource(R.mipmap.back_arrow);

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //ib_navigation.setImageResource(R.mipmap.back_arrow);
                    drawerLayout.openDrawer(GravityCompat.START);

                }
                break;


            case R.id.filter:

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    //ib_navigation.setImageResource(R.mipmap.back_arrow);

                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    //ib_navigation.setImageResource(R.mipmap.back_arrow);
                    drawerLayout.openDrawer(GravityCompat.END);

                }
                break;

            case R.id.profile:
                closeDrawer();
                Intent myIntent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(myIntent);


                break;

            case R.id.Options:
                closeDrawer();

                if (AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    Intent OptionsActivityIntent = new Intent(MainActivity.this, OptionsActivity.class);
                    startActivity(OptionsActivityIntent);

                } else {

                    ShowLoginPopUp();


                }

                break;

/*
                case R.id.Invest:
                closeDrawer();

                    Intent myIntentInvest = new Intent(MainActivity.this, WebViewActivity.class);
                    myIntentInvest.putExtra("WebUrl", ApiUrls.URL_Invest);
                    myIntentInvest.putExtra("Title", getResources().getString(R.string.invest));
                    startActivity(myIntentInvest);

                    break;
*/

            case R.id.ShareApp:
                closeDrawer();
                shareIt();

                break;

            case R.id.Stripe:
                closeDrawer();

                if (AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                        AppPrefs.KEY_USER_LOGIN_DEMO).
                        equalsIgnoreCase("false")) {

                    String AccountId = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_StripeAccId);

                    if (!(AccountId.length() > 0)) {

                        ShowStripePopup();

                    } else {

                        Snackbar.make(findViewById(android.R.id.content),
                                getResources().getString(R.string.stripe_account_already_existst),
                                Snackbar.LENGTH_SHORT).show();


                    }


                } else {

                    ShowLoginPopUp();
                }

                break;


            case R.id.linkedin:
                closeDrawer();
                openLinkedinIntent(MainActivity.this, "azzida");

                break;


            case R.id.instagram:
                closeDrawer();
                openInstagramIntent(MainActivity.this, "azzida_app");

                break;


            case R.id.facebook:
                closeDrawer();

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(MainActivity.this, "103603564708395");
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
/*
                openFacebookIntent(MainActivity.this, "103603564708395");
*/

                break;
            case R.id.About:
                closeDrawer();

                Intent myIntentAbout = new Intent(MainActivity.this, WebViewActivity.class);
                myIntentAbout.putExtra("WebUrl", ApiUrls.URL_About);
                myIntentAbout.putExtra("Title", getResources().getString(R.string.about));
                startActivity(myIntentAbout);

         /*       AboutWebViewFragment aboutWebViewFragment = new AboutWebViewFragment();

                Utils.replaceFrg(this, aboutWebViewFragment, false);*/


                break;

            case R.id.Faq:
                closeDrawer();

                Intent myIntentfaq = new Intent(MainActivity.this, WebViewActivity.class);
                myIntentfaq.putExtra("WebUrl", ApiUrls.URL_Faq);
                myIntentfaq.putExtra("Title", getResources().getString(R.string.faq));
                startActivity(myIntentfaq);

         /*       AboutWebViewFragment aboutWebViewFragment = new AboutWebViewFragment();

                Utils.replaceFrg(this, aboutWebViewFragment, false);*/


                break;


            case R.id.Apply_Filters:

                closeDrawer();

               /* for (String str : list) {
                    Log.e("TAG", str);
                }*/


                String idList = list.toString().replace("[", "").replace("]", "");
                Log.e("TAG", idList);

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMin, edt_MinPrice.getText().toString());
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMax, edt_MaxPrice.getText().toString());
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Distance, String.valueOf(seekbar_Distance.getProgress()));
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch, String.valueOf(switchShowHide.isChecked()));

                if (switchShowHide.isChecked()){
                    AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch_Active, "false");

                }else {
                    AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch_Active, "true");

                }



                CallHomeScreen();

                break;

            case R.id.Clear_Filters:

                closeDrawer();
                list.clear();

                seekbar_Distance.setProgress(25);

                switchShowHide.setChecked(true);


                txt_Distance.setText("within" + " " + seekbar_Distance.getProgress() + " " + "miles");

                edt_MinPrice.getText().clear();
                edt_MaxPrice.getText().clear();

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_CategoryList, "");


                SetCategoryAdpater();

                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMin, "");
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_PriceMax, "");
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Distance, String.valueOf(seekbar_Distance.getProgress()));
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch_Active, "false");
                AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Switch, "true");

                DataManager.getInstance().setLatInt("");
                DataManager.getInstance().setLongInt("");

                CallHomeScreen();

                break;


            case R.id.Logout:

                closeDrawer();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false)
                        .setTitle("Logout")
                        .setMessage("Do you want to Logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                LogoutApi();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
//        alert.setTitle("Logout");
                alert.show();

                break;

            case R.id.Login:

                closeDrawer();

                startActivity(new Intent(MainActivity.this,
                        LoginActivity.class));
                finish();

                break;

            case R.id.Feed:

                closeDrawer();

                CallHomeScreen();


                /*Intent JobSeekerActivity = new Intent(MainActivity.this, JobSeekerActivity.class);
                startActivity(JobSeekerActivity);*/
              /*  JobSeekerFragment jobSeekerFragment = new JobSeekerFragment();

                Utils.replaceFrg(this, jobSeekerFragment, true);*/


                break;

            case R.id.Web_Terms:

                closeDrawer();

                Intent myIntentTerms = new Intent(MainActivity.this, WebViewActivity.class);
                myIntentTerms.putExtra("WebUrl", ApiUrls.URL_Terms);
                myIntentTerms.putExtra("Title", getResources().getString(R.string.terms_and_conditions));
                startActivity(myIntentTerms);
                /*TermsWebViewFragment termsWebViewFragment = new TermsWebViewFragment();

                Utils.replaceFrg(this, termsWebViewFragment, false);*/


                break;


            case R.id.Web_Privacy:

                closeDrawer();
                Intent myIntentPrivacy = new Intent(MainActivity.this, WebViewActivity.class);
                myIntentPrivacy.putExtra("WebUrl", ApiUrls.URL_Privacy);
                myIntentPrivacy.putExtra("Title", getResources().getString(R.string.privacy_policy));

                startActivity(myIntentPrivacy);

                /*PrivacyWebViewFragment privacyWebViewFragment = new PrivacyWebViewFragment();

                Utils.replaceFrg(this, privacyWebViewFragment, false);*/


                break;


            case R.id.Web_Fees:

                closeDrawer();

                Intent myIntentFee = new Intent(MainActivity.this, WebViewActivity.class);
                myIntentFee.putExtra("WebUrl", ApiUrls.URL_Fees);
                myIntentFee.putExtra("Title", getResources().getString(R.string.fees_and_charges));
                startActivity(myIntentFee);
               /* FeeWebViewFragment feeWebViewFragment = new FeeWebViewFragment();

                Utils.replaceFrg(this, feeWebViewFragment, false);*/


                break;


            case R.id.locPop:


                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this, R.style.MyCustomTheme);

                LinearLayout view = (LinearLayout) MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_pop_location, null);


                b.setView(view);

                LinearLayout current_location = view.findViewById(R.id.current_location);

                edt_search_Location = view.findViewById(R.id.edt_search_Location);
                if (AddressLoc != null && AddressLoc.length() > 0) {

                    edt_search_Location.setText(AddressLoc);


                }


                AddressLoc = DataManager.getInstance().getAddressMain();

                if (AddressLoc != null) {

                    edt_search_Location.setText(AddressLoc);
                    edt_search_Location.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }

                final AlertDialog d = b.create();

                d.setCanceledOnTouchOutside(true);
                d.setCancelable(true);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                WindowManager.LayoutParams wmlp = d.getWindow().getAttributes();

                wmlp.gravity = Gravity.TOP;
                wmlp.x = 0;   //x position
                wmlp.y = 245;   //y position

                Window window = d.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                d.show();

                current_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Manager != null)
                            if (!Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                OnGPS();
                            } else {

                                getLocation();
                            }
                        DataManager.getInstance().setAddressMain("");
                        DataManager.getInstance().setLatMain("");
                        DataManager.getInstance().setLongMain("");

                        JobSeekerFragment jobSeekerFragment = new JobSeekerFragment();
                        Utils.removeFrg(MainActivity.this, jobSeekerFragment);
                        Utils.replaceFrg(MainActivity.this, jobSeekerFragment, true);

                        d.cancel();
                    }
                });

                edt_search_Location.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        d.cancel();
                        Intent intent = new Intent(MainActivity.this, CustomGooglePlacesSearch.class);
                        intent.putExtra("type", "Main");
                        intent.putExtra("s_address", edt_search_Location.getText().toString());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE);
                    }
                });


                break;


        }

    }

    private void ShowLoginPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.loginrequired))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this,
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

    private void ShowStripePopup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setPositiveButton("Link Existing Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();


                        AlertDialog.Builder Account = new AlertDialog.Builder(MainActivity.this);

                        CardView Accountview = (CardView) MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_account, null);

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

                            }
                        });


                        btn_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (validateAccount(edt_account_no.getText().toString())) {


                                    Accoun.cancel();

                                    RetrieveStripeAccount("", AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ID), edt_account_no.getText().toString());


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
                        Intent myIntent = new Intent(MainActivity.this, StripeConnect.class);
                        startActivityForResult(myIntent, ADD_ACCOUNT);
                    }
                })
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Link Account or Create New Account");
        alert.show();


    }

    private void SetCategoryAdpater() {

        categoryAdpater = new CategoryAdpater(MainActivity.this, new CategoryAdpater.ClickView() {
            @Override
            public void clickitem(View view, final int position) {


            }
        }, arrayCategory);

        RecyclerCategories.setAdapter(categoryAdpater);

    }

    private void CallHomeScreen() {


        if (AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                AppPrefs.KEY_USER_LOGIN_DEMO).
                equalsIgnoreCase("true")) {

            JobSeekerFragment jobSeekerFragment = new JobSeekerFragment();
            Utils.replaceFrg(MainActivity.this, jobSeekerFragment, false);
            Logout.setVisibility(View.GONE);
            Login.setVisibility(View.VISIBLE);

        } else {

            JobSeekerFragment jobSeekerFragment = new JobSeekerFragment();
            Utils.replaceFrg(MainActivity.this, jobSeekerFragment, false);
            Logout.setVisibility(View.VISIBLE);
            Login.setVisibility(View.GONE);

        }


    }


    private void getJobCategory() {

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetJobCategoryModel> call = service.GetJobCategory();
        call.enqueue(new Callback<GetJobCategoryModel>() {


            @Override
            public void onResponse(@NonNull Call<GetJobCategoryModel> call, @NonNull Response<GetJobCategoryModel> response) {

                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetJobCategoryModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arrayCategory != null && arrayCategory.size() > 0 && categoryAdpater != null) {

                                arrayCategory.clear();
                                categoryAdpater.notifyDataSetChanged();
                            }

                            arrayCategory = result.getData();

                            SetCategoryAdpater();


                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobCategoryModel> call, @NonNull Throwable t) {

            }
        });
    }


    private void shareIt() {

        if (AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                AppPrefs.KEY_USER_LOGIN_DEMO).
                equalsIgnoreCase("true")) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Azzida");
                String shareMessage = "Check out this new app for getting stuff done.\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (Exception e) {
                //e.toString();
            }

        } else {


            try {
                String s_RefCode = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_RefCode);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Azzida");
                String shareMessage = "Check out this new app for getting stuff done.\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                shareMessage = shareMessage + "\nUse Code for SignUp " + s_RefCode;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch (Exception e) {
                //e.toString();
            }

        }


    }


    private void LogoutApi() {
        final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ID);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetJobCategoryModel> call = service.Logout(s_userId);
        call.enqueue(new Callback<GetJobCategoryModel>() {


            @Override
            public void onResponse(@NonNull Call<GetJobCategoryModel> call, @NonNull Response<GetJobCategoryModel> response) {
                if (progressdialog != null) {

                    progressdialog.dismiss();

                }

                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetJobCategoryModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        userLogout();

                    } else {
                        userLogout();
                    }

                } else {
                    // Server Problem
                    Toast.makeText(MainActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobCategoryModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void userLogout() {

        LoginManager.getInstance().logOut();

        GoogleAuthManager.getInstall(MainActivity.this).signOut();
        AppPrefs.clearPrefsdata(MainActivity.this);

        Intent intent = new Intent(MainActivity.this,
                LoginActivity.class);

        startActivity(intent);

        finish();

        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawers();

        } else if (drawerLayout.isDrawerOpen(GravityCompat.END)) {

            drawerLayout.closeDrawers();
        } else {

            fm = getSupportFragmentManager();
            int addedFrgCount = fm.getBackStackEntryCount();
            Fragment fragment = fm.findFragmentById(R.id.frag_container);

            onShowQuitDialog();
        }


    }

    private void onShowQuitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setMessage("Do You Want To Quit?");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }

    private void closeDrawer() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawers();

        } else if (drawerLayout.isDrawerOpen(GravityCompat.END)) {

            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String s_firstName = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_FirstName);
        String s_lastName = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_LastName);

        String s_ProfileImage = AppPrefs.getStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_User_ProfileImage);

        if (s_firstName != null && s_firstName.length() > 0) {

            header_FullName.setText(s_firstName + " " + s_lastName);
        } else {

            Intent edit_profile = new Intent(MainActivity.this, EditProfileNewActivity.class);
            startActivity(edit_profile);
        }


        if (s_ProfileImage.length() > 0) {
            Picasso.get().load(s_ProfileImage)
                    .placeholder(R.drawable.no_profile)
                    .error(R.drawable.no_profile)
                    .into(header_profile_image);
        }


        AddressLoc = DataManager.getInstance().getAddressMain();
        LatT = DataManager.getInstance().getLatMain();
        LongT = DataManager.getInstance().getLongMain();

      /*  if (AppPrefs.getStringKeyvaluePrefs(MainActivity.this,
                AppPrefs.KEY_Loc_Chn).
                equalsIgnoreCase("true")) {*/

        if (AddressLoc != null && AddressLoc.length() > 0) {

            /*AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_Loc_Chn, "false");*/


            getReverseGeoCodingNew();

            /*}*/
        }


    }

    private void RetrieveStripeAccount(String code, String SIGNUP_ID, String AccountNo) {

        Utils.showProgressdialog(MainActivity.this, "Please Wait....");

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

                            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_StripeAccId, code);

                        } else {

                            AppPrefs.setStringKeyvaluePrefs(MainActivity.this, AppPrefs.KEY_StripeAccId, AccountNo);

                        }


                        setComplete();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });

    }


    private void setComplete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Account Link Successfully")
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


}
