package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.azzida.R;
import com.azzida.model.SignInModel;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {

    EditText edt_Emial;
    TextInputLayout textInputLayoutEmail;
    /*String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";*/
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)*(\\.[a-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);

        edt_Emial = findViewById(R.id.edt_Emial);


    }

    public void ForgotPassword(View view) {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (validateUser()) {

                setLoginUser();


            }
        }


    }


    public void setLoginUser() {
        final ProgressDialog progressdialog = new ProgressDialog(ForgotActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userName = edt_Emial.getText().toString();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignInModel> call = service.ForgotPassword(s_userName);
        call.enqueue(new Callback<SignInModel>() {


            @Override
            public void onResponse(@NonNull Call<SignInModel> call, @NonNull Response<SignInModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SignInModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        Intent myIntent = new Intent(ForgotActivity.this, LoginActivity.class);
                                        startActivity(myIntent);
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle(getString(R.string.Alert));
                        alert.show();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotActivity.this);
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
                    Toast.makeText(ForgotActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {

        Intent myIntent = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }

    public boolean validateUser() {
        boolean valid = true;


        String s_email = edt_Emial.getText().toString();


        if (s_email.isEmpty()) {
            edt_Emial.setError("Enter Email Id");
            edt_Emial.requestFocus();
            valid = false;
        } else {
            edt_Emial.setError(null);
        }
        if (s_email.matches(emailPattern)) {
            edt_Emial.setError(null);
            textInputLayoutEmail.setEndIconVisible(true);
        } else {
            edt_Emial.setError("Email- Id is not valid");
            edt_Emial.requestFocus();
            textInputLayoutEmail.setEndIconVisible(false);

            valid = false;
        }

        return valid;

    }


}
