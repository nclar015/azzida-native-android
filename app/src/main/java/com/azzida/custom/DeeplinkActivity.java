package com.azzida.custom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.azzida.R;
import com.azzida.perfrences.AppPrefs;
import com.azzida.ui.activity.MainActivity;
import com.azzida.ui.notification.JobSeekerNotificationActivity;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class DeeplinkActivity extends AppCompatActivity {

    String JobId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);

        Utils.showProgressdialog(DeeplinkActivity.this, "Please Wait....");

        try {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
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

            Utils.dismissProgressdialog();

            Intent myIntent = new Intent(DeeplinkActivity.this, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();
            /*e.printStackTrace();*/

        }


    }

    private void doFinish() {


        if (JobId.length() > 0) {

            if (AppPrefs.getStringKeyvaluePrefs(DeeplinkActivity.this,
                    AppPrefs.KEY_USER_LOGIN_STATUS).
                    equalsIgnoreCase("true")) {

                Utils.dismissProgressdialog();
                Intent myIntent = new Intent(DeeplinkActivity.this, JobSeekerNotificationActivity.class);
                myIntent.putExtra("JobId", JobId);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);


            } else {

                String status = NetworkUtils.getConnectivityStatus(this);
                if (status.equals("404")) {
                    this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(DeeplinkActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "true");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_ID, "0");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_FirstName, "Guest");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_LastName, "User");
                    Utils.dismissProgressdialog();

                    Intent myIntent = new Intent(DeeplinkActivity.this, JobSeekerNotificationActivity.class);
                    myIntent.putExtra("JobId", JobId);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                    finish();
                }


            }


        } else {

            if (AppPrefs.getStringKeyvaluePrefs(DeeplinkActivity.this,
                    AppPrefs.KEY_USER_LOGIN_STATUS).
                    equalsIgnoreCase("true")) {

                Utils.dismissProgressdialog();

                Intent myIntent = new Intent(DeeplinkActivity.this, MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish();


            } else {

                String status = NetworkUtils.getConnectivityStatus(this);
                if (status.equals("404")) {
                    this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(DeeplinkActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "true");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_ID, "0");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_FirstName, "Guest");

                    AppPrefs.setStringKeyvaluePrefs(DeeplinkActivity.this, AppPrefs.KEY_User_LastName, "User");

                    Utils.dismissProgressdialog();

                    Intent myIntent = new Intent(DeeplinkActivity.this, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                    finish();
                }


            }


        }


    }


}
