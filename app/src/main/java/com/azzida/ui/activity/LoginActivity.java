package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.azzida.R;
import com.azzida.manager.GoogleAuthManager;
import com.azzida.model.EditProfileModel;
import com.azzida.model.SignInModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.ui.notification.JobListerNotificationActivity;
import com.azzida.ui.notification.JobSeekerNotificationActivity;
import com.azzida.utills.NetworkUtils;
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

public class LoginActivity extends AppCompatActivity {

    private static final int FILECHOOSER_RESULTCODE = 2888;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final String TAG = "Fragment";
    EditText edt_password, edt_UserName;
    LoginManager loginManager;
    TextInputLayout textInputLayoutPassword;
    String FirstNameFaceBook, LastNameFaceBook;
    String FirstNameGoogle, LastNameGoogle;
    String FirstNameLo, LastNameLo;
    ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private String Username, Password;
    private String JobId, ScreenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                JobId = null;
                ScreenName = null;
            } else {
                JobId = extras.getString("JobId");
                ScreenName = extras.getString("Screen");

            }
        } else {
            JobId = (String) savedInstanceState.getSerializable("JobId");
            ScreenName = (String) savedInstanceState.getSerializable("Screen");
        }


        initView();
/*        if (AppPrefs.getStringKeyvaluePrefsNew(LoginActivity.this,
                AppPrefs.KEY_USER_IsFirst).
                equalsIgnoreCase("")) {
            *//*showEula();*//*
        }*/
    }

    private void initView() {

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        edt_UserName = findViewById(R.id.edt_UserName);
        edt_password = findViewById(R.id.edt_password);

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


        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestFacebookUserProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {

                        Log.e(TAG, "onCancel: Facebook");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e(TAG, "onError: Facebook " +  error.toString());
                    }
                });


    }

    public void login(View view) {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (validateUser()) {
                if (validatePass()) {
                    setLoginUser();
                }

            }
        }


    }

    public void signUp(View view) {

        Intent myIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(myIntent);

    }

    public void onClickFacebookLogin(View view) {

        loginManager.logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));

    }


    public void onClickGoogleLogin(View view) {
        GoogleAuthManager.getInstall(this).googleSignIn();
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


    public void setLoginUser() {
        final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userName = edt_UserName.getText().toString();
        String s_password = edt_password.getText().toString();
        String regId = AppPrefs.getStringKeyvaluePrefsNew(LoginActivity.this, AppPrefs.KEY_DEVICE_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignInModel> call = service.Login(s_userName, s_password, regId, "Android");
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
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");
                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_CandidateId, result.getData().getCandidateId());


                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ID, String.valueOf(result.getData().getId()));

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_FirstName, String.valueOf(result.getData().getFirstName()));

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_LastName, String.valueOf(result.getData().getLastName()));
                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_RefCode, String.valueOf(result.getData().getRefCode()));

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));

                                AppPrefs.setBooleanKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_Azzida_Verified, result.getData().getAzzidaVerified());


                                if (ScreenName != null && ScreenName.length() > 0) {


                                    setScreenJob();


                                } else {

                                    setScreenMain();

                                }

                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void setScreenMain() {

        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();

    }

    private void setScreenJob() {

        if (ScreenName.equalsIgnoreCase("Poster")) {

            Intent myIntent = new Intent(LoginActivity.this, JobListerNotificationActivity.class);
            myIntent.putExtra("JobId", JobId);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();


        } else {

            Intent myIntent = new Intent(LoginActivity.this, JobSeekerNotificationActivity.class);
            myIntent.putExtra("JobId", JobId);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();

        }

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
            String id = account.getId();
            String email = account.getEmail();
            FirstNameGoogle = account.getGivenName();
            LastNameGoogle = account.getFamilyName();
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

        final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userName = FirstNameLo + " " + LastNameLo;
        String regId = AppPrefs.getStringKeyvaluePrefsNew(LoginActivity.this, AppPrefs.KEY_DEVICE_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignInModel> call = service.FacebookGoogleLogin(email, s_userName, id, regId, "Android", provider);
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
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                if (result.getData().getFirstName() != null) {

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");
                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ID, String.valueOf(result.getData().getId()));

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_CandidateId, result.getData().getCandidateId());

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_FirstName, result.getData().getFirstName());

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_LastName, result.getData().getLastName());

                                    AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));

                                    AppPrefs.setBooleanKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_Azzida_Verified, result.getData().getAzzidaVerified());


                                    if (ScreenName != null && ScreenName.length() > 0) {


                                        setScreenJob();


                                    } else {

                                        setScreenMain();

                                    }

                                } else {


                                    setProfile(FirstNameLo, LastNameLo, ""+ result.getData().getId());


                                }


                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }


    public boolean validateUser() {
        boolean valid = true;

        String s_userName = edt_UserName.getText().toString();

        if (s_userName.isEmpty()) {
            edt_UserName.setError("Enter User Name");
            edt_UserName.requestFocus();
            valid = false;
        } else {
            edt_UserName.setError(null);
        }

        return valid;
    }


    public boolean validatePass() {
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

    public void ForgotPasswor(View view) {
        Intent myIntent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(myIntent);
        finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroyView", "onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("onstop", "onstop");

    }


    private void setProfile(String firstNameLo, String lastNameLo, String s_userId) {


        final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();

        String regId = AppPrefs.getStringKeyvaluePrefsNew(LoginActivity.this, AppPrefs.KEY_DEVICE_ID);


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


                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_StripeAccId,
                                        result.getData().getStripeAccId());


                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_FirstName,
                                        String.valueOf(result.getData().getFirstName()));

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_LastName,
                                        String.valueOf(result.getData().getLastName()));

                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ProfileImage,
                                        String.valueOf(result.getData().getProfilePicture()));


                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_STATUS, "true");
                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_USER_LOGIN_DEMO, "false");
                                AppPrefs.setStringKeyvaluePrefs(LoginActivity.this, AppPrefs.KEY_User_ID,
                                        String.valueOf(result.getData().getId()));

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }

  /*  public void Skiplogin(View view) {

}*/

/*
    private void setLoginUserDemo() {
        final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SignInModel> call = service.Login(Username, Password, "1233", "Android");
        call.enqueue(new Callback<SignInModel>() {

            @Override
            public void onResponse(@NonNull Call<SignInModel> call, @NonNull Response<SignInModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SignInModel result = response.body();
                    Log.d("fgh", response.toString());
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {



                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }
*/


}
