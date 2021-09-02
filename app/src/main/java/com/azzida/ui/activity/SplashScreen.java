package com.azzida.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.azzida.BuildConfig;
import com.azzida.R;
import com.azzida.common.ExceptionHandler;
import com.azzida.perfrences.AppPrefs;
import com.azzida.ui.notification.ChatNotificationActivity;
import com.azzida.ui.notification.JobListerNotificationActivity;
import com.azzida.ui.notification.JobSeekerNotificationActivity;
import com.azzida.utills.NetworkUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * The Class SplashScreen.
 */
public class SplashScreen extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;
    AlertDialog.Builder b;
    private boolean isRunning;
    private String JobId = "";
    private String UserId = "";
    private String Link = "";
    private int CallValue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        displayFirebaseRegId();
        startSplash();

    }

    /**
     * Start splash.
     */
    private void startSplash() {

        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    long start = System.currentTimeMillis();


                    long left = 3000 - (System.currentTimeMillis() - start);
                    if (left > 100)
                        Thread.sleep(left);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    String crash = getIntent()
                            .getStringExtra(ExceptionHandler.CRASH_REPORT);
                    if (crash != null) {
                        showCrashDialog(crash);
                    } else {
                        permission();
                    }
                }
            }
        }).start();
    }

    /**
     * Do finish.
     */
    private synchronized void doFinish() {

        if (isRunning) {
            isRunning = false;

            String status = NetworkUtils.getConnectivityStatus(this);
            if (status.equals("404")) {
                this.runOnUiThread(new Runnable() {
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                        builder.setMessage(getResources().getString(R.string.CheckYourInternetConnection))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();

                    }
                });

            } else {


                if (JobId.length() > 0) {

                    if (AppPrefs.getStringKeyvaluePrefs(SplashScreen.this,
                            AppPrefs.KEY_USER_LOGIN_STATUS).
                            equalsIgnoreCase("true")) {

                     /*   Intent myIntent = new Intent(SplashScreen.this, JobSeekerNotificationActivity.class);
                        myIntent.putExtra("JobId", JobId);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();*/

                        if (Link.length() > 0) {

                            String ScreenName = Link.replace("?", "");

                            if (ScreenName.equalsIgnoreCase("Poster")) {

                                Intent myIntent = new Intent(SplashScreen.this, JobListerNotificationActivity.class);
                                myIntent.putExtra("JobId", JobId);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                                finish();


                            } else if (ScreenName.equalsIgnoreCase("Chat")) {

                                Intent myIntent = new Intent(SplashScreen.this, ChatNotificationActivity.class);
                                myIntent.putExtra("JobId", JobId);
                                myIntent.putExtra("NewApi", "true");
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                                finish();

                            } else if (ScreenName.equalsIgnoreCase("Message")) {

                                Intent myIntent = new Intent(SplashScreen.this, ChatNotificationActivity.class);
                                myIntent.putExtra("JobId", JobId);
                                myIntent.putExtra("NewApi", "true");
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                                finish();

                            } else {

                                Intent myIntent = new Intent(SplashScreen.this, JobSeekerNotificationActivity.class);
                                myIntent.putExtra("JobId", JobId);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                                finish();
                            }

                        } else {

                            Intent myIntent = new Intent(SplashScreen.this, JobSeekerNotificationActivity.class);
                            myIntent.putExtra("JobId", JobId);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();


                        }

/*

                        if (UserId.equals(AppPrefs.getStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_ID))){

                            Intent myIntent = new Intent(SplashScreen.this, JobListerNotificationActivity.class);
                            myIntent.putExtra("JobId", JobId);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();


                        }else {

                            Intent myIntent = new Intent(SplashScreen.this, JobSeekerNotificationActivity.class);
                            myIntent.putExtra("JobId", JobId);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();


                        }
*/


                    } else {

/*
                        AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_USER_LOGIN_DEMO, "true");

                        AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_ID, "0");

                        AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_FirstName, "Guest");

                        AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_LastName, "User");

                        Intent myIntent = new Intent(SplashScreen.this, JobSeekerNotificationActivity.class);
                        myIntent.putExtra("JobId", JobId);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();
*/
                        String ScreenName1 = Link.replace("?", "");

                        Intent myIntent = new Intent(SplashScreen.this, LoginActivity.class);
                        myIntent.putExtra("JobId", JobId);
                        myIntent.putExtra("Screen", ScreenName1);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();


                    }


                } else {

                    if (AppPrefs.getStringKeyvaluePrefs(SplashScreen.this,
                            AppPrefs.KEY_USER_LOGIN_STATUS).
                            equalsIgnoreCase("true")) {

                        Intent myIntent = new Intent(SplashScreen.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();


                    } else {

                        if (status.equals("404")) {
                            this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(SplashScreen.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_USER_LOGIN_DEMO, "true");

                            AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_ID, "0");

                            AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_FirstName, "Guest");

                            AppPrefs.setStringKeyvaluePrefs(SplashScreen.this, AppPrefs.KEY_User_LastName, "User");

                            Intent myIntent = new Intent(SplashScreen.this, MainActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            finish();
                        }


                    }


                }


            }

        }
    }


    private void displayFirebaseRegId() {

        String regId = AppPrefs.getStringKeyvaluePrefsNew(SplashScreen.this, AppPrefs.KEY_DEVICE_ID);

        Log.e("TAG", "Firebase reg id: " + regId);

    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isRunning = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showCrashDialog(final String report) {
        b = new AlertDialog.Builder(this);
        b.setTitle("App Crashed");
        b.setMessage(
                "Oops! The app crashed in \n\n"
                        + "\nModel:" + Build.MODEL
                        + "\nVERSION NAME:" + BuildConfig.VERSION_NAME
                        + "\nVERSION CODE:" + BuildConfig.VERSION_CODE
                        + "\nManufacturer: " + Build.MANUFACTURER
                        + "\nProduct: " + Build.PRODUCT
                        + "\nVersion:" + Build.VERSION.SDK_INT
                        + "\n\ndue to below reason:\n\n" + report
        );
        DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/html");
                    i.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{"info@logictrixtech.com"});
                    i.putExtra(Intent.EXTRA_TEXT, "Oops! The app crashed in \n\n"
                            + "\nModel:" + Build.MODEL
                            + "\nVERSION NAME:" + BuildConfig.VERSION_NAME
                            + "\nVERSION CODE:" + BuildConfig.VERSION_CODE
                            + "\nManufacturer: " + Build.MANUFACTURER
                            + "\nProduct: " + Build.PRODUCT
                            + "\nVersion:" + Build.VERSION.SDK_INT
                            + "\n\ndue to below reason:\n\n" + report);
                    i.putExtra(Intent.EXTRA_SUBJECT, "App Crashed");
                    SplashScreen.this.startActivity(Intent.createChooser(i, "Send Mail via:"));
                    SplashScreen.this.finish();
                } else {
                    getData();
                }
                dialog.dismiss();
            }
        };
        b.setCancelable(false);
        b.setPositiveButton("Send Report", ocl);
        b.setNegativeButton("Restart", ocl);
        SplashScreen.this.runOnUiThread(new Runnable() {
            public void run() {
                b.create().show();
            }
        });

    }


    private void getData() {
        try {

            String linkS = "";
            if (String.valueOf(getIntent().getData()).contains("?")) {

                Link = String.valueOf(getIntent().getData()).substring(String.valueOf(getIntent().getData()).indexOf("?"));
                linkS = String.valueOf(getIntent().getData()).replace(Link, "");

            } else {

                if (getIntent().getData() == null) {

                    linkS = String.valueOf(getIntent());

                } else {

                    linkS = String.valueOf(getIntent().getData());
                }


            }
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(Uri.parse(linkS))
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                            }
                            if (deepLink != null) {
                                JobId = deepLink.getQueryParameter("JobId");
                                UserId = deepLink.getQueryParameter("UserId");

                            }
                            doFinish();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            doFinish();

                        }
                    });
        } catch (Exception e) {
            doFinish();
            e.printStackTrace();

        }
    }


    private void permission() {

        if (!checkPermission()) {

            requestPermission();

        } else {

            getData();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
                && result4 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA,
                READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean locationCAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean storageWAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (locationAccepted && locationCAccepted && cameraAccepted && storageAccepted && storageWAccepted) {
                    getData();
                    Log.e("TAG", "Permission Granted");

                } else {
                    Log.e("TAG", "Permission Denied");

                    showMessageOKCancel();


                }
            }
        }
    }


    private void showMessageOKCancel() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("You need to allow all the permissions");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (CallValue == 0) {

                                CallValue = 1;

                                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA,
                                                READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_REQUEST_CODE);

                            } else {

                                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(i);
                                finish();

                            }

                        }
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }

}