package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.azzida.R;
import com.azzida.manager.GoogleAuthManager;
import com.azzida.model.EditProfileModel;
import com.azzida.model.SignInModel;
import com.azzida.model.SignUpModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.azzida.manager.GoogleAuthManager.RC_SIGN_IN;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_firstName, edt_lastName, edt_email, edt_username, edt_password, edt_referral;
    /*String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";*/
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)*(\\.[a-z]{2,})$";
    TextInputLayout textInputLayoutPassword, textInputLayoutEmail;
    LoginManager loginManager;
    CheckBox checkbox;
    TextView checkboxtext;
    String FirstNameFaceBook, LastNameFaceBook;
    String FirstNameGoogle, LastNameGoogle;
    String FirstNameLo, LastNameLo;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    private void initView() {

        checkbox = findViewById(R.id.checkBox1);
        checkboxtext = findViewById(R.id.checkboxtext);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        checkboxtext.setText(Html.fromHtml("I have read and agree to the Azzida " +
                "<a href='http://azzida.com/odd_jobs/azzida-terms-of-use'>Terms of Use </a>" + " & " +
                "<a href='http://azzida.com/odd_jobs/azzida-privacy-policy'>Privacy Policy</a>"));
        checkboxtext.setClickable(true);
        checkboxtext.setMovementMethod(LinkMovementMethod.getInstance());

        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);

        edt_firstName = findViewById(R.id.edt_firstName);
        edt_lastName = findViewById(R.id.edt_lastName);
        edt_email = findViewById(R.id.edt_email);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_referral = findViewById(R.id.edt_referral);


        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textInputLayoutEmail.setEndIconVisible(true);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestFacebookUserProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });


    }


    public void Login(View view) {


        Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(myIntent);

    }

    public void SignUp(View view) {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (validateFirstName()) {
                if (validateLastName()) {
                    if (validateemail()) {
                        if (validatUsername()) {
                            if (validatpassword()) {
                                if (checkbox.isChecked()) {
                                    setSignUpUser();
                                } else {

                                    Toast.makeText(this, "Agree the Policy", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setSignUpUser() {

        Utils.showProgressdialog(SignUpActivity.this, "Please Wait....");

        String s_FirstName = edt_firstName.getText().toString();
        String s_LastName = edt_lastName.getText().toString();
        String s_email = edt_email.getText().toString();
        String s_Username = edt_username.getText().toString();
        String s_password = edt_password.getText().toString();
        String s_referral = edt_referral.getText().toString();

        String regId = AppPrefs.getStringKeyvaluePrefsNew(SignUpActivity.this, AppPrefs.KEY_DEVICE_ID);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignUpModel> call = service.SignUp("0", "2", s_FirstName, s_LastName, s_password, s_email, ""
                , regId, "Android", "other", s_Username, "", "", s_referral, "");
        call.enqueue(new Callback<SignUpModel>() {


            @Override
            public void onResponse(@NonNull Call<SignUpModel> call, @NonNull Response<SignUpModel> response) {
                Utils.dismissProgressdialog();

                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SignUpModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_SIGNUP_ID, result.getData().getId().toString());


/*
                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ID, String.valueOf(result.getData().getId()));

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_FirstName, String.valueOf(result.getData().getFirstName()));

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_LastName, String.valueOf(result.getData().getLastName()));

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));*/

                                /*Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(myIntent);
                                finish();*/

                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage("We have sent a verification email to your email id, please verify and login to app.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                Intent myIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                startActivity(myIntent);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                    Toast.makeText(SignUpActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignUpModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });
    }


    public boolean validateFirstName() {
        boolean valid = true;

        String s_FirstName = edt_firstName.getText().toString();

        if (s_FirstName.isEmpty()) {
            edt_firstName.setError("Enter First Name");
            edt_firstName.requestFocus();
            valid = false;
        } else {
            edt_firstName.setError(null);
        }

        return valid;
    }

    public boolean validateLastName() {
        boolean valid = true;

        String s_LastName = edt_lastName.getText().toString();


        if (s_LastName.isEmpty()) {
            edt_lastName.setError("Enter Last Name");
            edt_lastName.requestFocus();
            valid = false;
        } else {
            edt_lastName.setError(null);
        }
        return valid;
    }

    public boolean validateemail() {
        boolean valid = true;


        String s_email = edt_email.getText().toString();


        if (s_email.isEmpty()) {
            edt_email.setError("Enter Email Id");
            edt_email.requestFocus();
            valid = false;
        } else {
            edt_email.setError(null);
        }
        if (s_email.matches(emailPattern)) {
            edt_email.setError(null);
            textInputLayoutEmail.setEndIconVisible(true);
        } else {
            edt_email.setError("Email- Id is not valid");
            edt_email.requestFocus();
            textInputLayoutEmail.setEndIconVisible(false);

            valid = false;
        }

        return valid;
    }

    public boolean validatUsername() {
        boolean valid = true;

        String s_Username = edt_username.getText().toString();


        if (s_Username.isEmpty()) {
            edt_username.setError("Enter User Name");
            edt_username.requestFocus();
            valid = false;
        } else {
            edt_username.setError(null);
        }


        return valid;
    }

    public boolean validatpassword() {
        boolean valid = true;


        String s_password = edt_password.getText().toString();

        if (s_password.isEmpty()) {
            edt_password.setError("Enter Password");
            textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
            edt_password.requestFocus();
            valid = false;
        } else {
            edt_password.setError(null);
            textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
        }
        return valid;
    }

    public void onClickGoogleSignup(View view) {

        if (checkbox.isChecked()) {

            GoogleAuthManager.getInstall(this).googleSignIn();

        } else {

            Toast.makeText(this, "Agree the Policy", Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickFacebookSignup(View view) {
        if (checkbox.isChecked()) {

            loginManager.logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));

        } else {

            Toast.makeText(this, "Agree the Policy", Toast.LENGTH_SHORT).show();

        }
    }


    private void requestFacebookUserProfile(final LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            try {
                                String email = object.getString("email");

                                FirstNameFaceBook = object.getString("first_name");
                                LastNameFaceBook = object.getString("last_name");
                                String id = object.getString("id");
                                AccessToken token = loginResult.getAccessToken();
                                Log.d("long", "email: " + email + " token: " + token.getToken());

                                loginExternal(email, id, "facebook");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,location");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            onGoogleActivityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void onGoogleActivityResult(int requestCode, int resultCode, Intent data) {
        // result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            String token = account.getIdToken();
            String email = account.getEmail();
            FirstNameGoogle = account.getGivenName();
            LastNameGoogle = account.getFamilyName();
            String id = account.getId();
            loginExternal(email, id, "google");
        }
    }


    private void loginExternal(String email, String id, String provider) {

        if (provider.equals("google")) {

            FirstNameLo = FirstNameGoogle;
            LastNameLo = LastNameGoogle;

        } else {

            FirstNameLo = FirstNameFaceBook;
            LastNameLo = LastNameFaceBook;

        }

        Utils.showProgressdialog(SignUpActivity.this, "Please Wait....");

        String s_userName = FirstNameLo + " " + LastNameLo;
        String regId = AppPrefs.getStringKeyvaluePrefsNew(SignUpActivity.this, AppPrefs.KEY_DEVICE_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignInModel> call = service.FacebookGoogleLogin(email, s_userName, id, regId, "Android", provider);
        call.enqueue(new Callback<SignInModel>() {

            @Override
            public void onResponse(@NonNull Call<SignInModel> call, @NonNull Response<SignInModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SignInModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                if (result.getData().getFirstName() != null) {

                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");
                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");

                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ID, String.valueOf(result.getData().getId()));

                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_FirstName, result.getData().getFirstName());

                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_LastName, result.getData().getLastName());

                                    AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));


                                    AppPrefs.setBooleanKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_Azzida_Verified, result.getData().getAzzidaVerified());

                                    Intent myIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(myIntent);
                                    finish();

                                } else {

                                    setProfile(FirstNameLo, LastNameLo, ""+ result.getData().getId());

                                }


                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                    Toast.makeText(SignUpActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
            }
        });


    }

    private void setProfile(String firstNameLo, String lastNameLo, String s_userId) {


        final ProgressDialog progressdialog = new ProgressDialog(SignUpActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();

        String regId = AppPrefs.getStringKeyvaluePrefsNew(SignUpActivity.this, AppPrefs.KEY_DEVICE_ID);


        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<EditProfileModel> call = service.EditProfileNw(s_userId, "2", firstNameLo,
                lastNameLo, "", "", "", regId, "Android", "",
                "", "", "", "", "");
        call.enqueue(new Callback<EditProfileModel>() {

            @Override
            public void onResponse(@NonNull Call<EditProfileModel> call, @NonNull Response<EditProfileModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    EditProfileModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {


                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_StripeAccId,
                                        result.getData().getStripeAccId());


                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_FirstName,
                                        String.valueOf(result.getData().getFirstName()));

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_LastName,
                                        String.valueOf(result.getData().getLastName()));

                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ProfileImage,
                                        String.valueOf(result.getData().getProfilePicture()));


                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");
                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");
                                AppPrefs.setStringKeyvaluePrefs(SignUpActivity.this, AppPrefs.KEY_User_ID,
                                        String.valueOf(result.getData().getId()));

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                    Toast.makeText(SignUpActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }



}
