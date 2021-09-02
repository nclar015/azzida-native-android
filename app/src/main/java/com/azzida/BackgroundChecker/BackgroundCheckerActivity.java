package com.azzida.BackgroundChecker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.azzida.R;
import com.azzida.datePicker.DatePickerPopWin;
import com.azzida.model.CheckerModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.ApiUrls;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utilities;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.shuhart.stepview.StepView;
import com.squareup.okhttp.Credentials;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BackgroundCheckerActivity extends AppCompatActivity {

    StepView stepView;
    ImageView img_back_feed;
    TextView title;
    LinearLayout Btn_Next, btn_Previous;
    EditText edt_firstName, edt_middleName, edt_lastName, edt_email, edt_Social, edt_Zip, edt_phone;
    TextInputLayout textInputLayoutEmail;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)*(\\.[a-z]{2,})$";
    /*String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
/*
    String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";*/
    TextView edt_select_date;
    String daes;
    TextView SubmitForChecker, checkboxtext;
    RelativeLayout R1;
    ScrollView scrollView;
    WebView WebVie;
    CheckBox checkbox;
    private int currentStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_checker);

        initView();

    }


    private void initView() {

        img_back_feed = findViewById(R.id.img_back_feed);

        title = findViewById(R.id.title);

        WebVie = findViewById(R.id.WebVie);

        checkboxtext = findViewById(R.id.checkboxtext);

        checkbox = findViewById(R.id.checkBox1);

        title.setText(R.string.BackgroundChecker);

        img_back_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        stepView = findViewById(R.id.step_view);

        if (currentStep == 0) {

            checkboxtext.setText(getResources().getString(R.string.Disclosure));

        }

        List<String> steps = new ArrayList<>();
        steps.add("Disclosure");
        steps.add("Your \n Rights");
        steps.add("California \n Disclosure");
        steps.add("Authorization");
        steps.add("Form");
        /*for (int i = 0; i < 5; i++) {
            steps.add("Step " + (i + 1));
        }
        steps.set(steps.size() - 1, steps.get(steps.size() - 1) + " last one");*/
        stepView.setSteps(steps);

        R1 = findViewById(R.id.R1);

        scrollView = findViewById(R.id.scrollView);

        edt_firstName = findViewById(R.id.edt_firstName);
        edt_middleName = findViewById(R.id.edt_middleName);
        edt_lastName = findViewById(R.id.edt_lastName);
        edt_email = findViewById(R.id.edt_email);
        edt_firstName = findViewById(R.id.edt_firstName);
        edt_Social = findViewById(R.id.edt_Social);

        edt_Zip = findViewById(R.id.edt_Zip);
        edt_phone = findViewById(R.id.edt_phone);
        edt_select_date = findViewById(R.id.edt_select_date);


        SubmitForChecker = findViewById(R.id.SubmitForChecker);


        WebVie.loadUrl("file:///android_asset/Disclosure.html");

        SubmitForChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*jumpToBackPage();*/

                CheckerApi();

            }
        });
/*

        edt_Social.setText("111-11-2001");
        edt_phone.setText("2035408926");
        edt_email.setText("ltrixteam@gmail.com");
        edt_lastName.setText("Scott");
        edt_firstName.setText("Michael");
        edt_middleName.setText("Gary");
        edt_Zip.setText("06831");

        edt_select_date.setText("1964-03-15");
        edt_select_date.setTextColor(ContextCompat.getColor(BackgroundCheckerActivity.this, R.color.black));

*/
        edt_Social.addTextChangedListener(new TextWatcher() {

            private boolean spaceDeleted;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                CharSequence charDeleted = s.subSequence(start, start + count);
                spaceDeleted = "-".equals(charDeleted.toString());
            }

            public void afterTextChanged(Editable editable) {

                edt_Social.removeTextChangedListener(this);

                int cursorPosition = edt_Social.getSelectionStart();
                String withSpaces = formatText(editable);
                edt_Social.setText(withSpaces);

                edt_Social.setSelection(cursorPosition + (withSpaces.length() - editable.length()));


                if (spaceDeleted) {
                    //  userNameET.setSelection(userNameET.getSelectionStart() - 1);
                    spaceDeleted = false;
                }


                edt_Social.addTextChangedListener(this);
            }

            private String formatText(CharSequence text) {
                StringBuilder formatted = new StringBuilder();
                int count = 0;
                if (text.length() == 3 || text.length() == 6) {
                    if (!spaceDeleted)
                        formatted.append(text + "-");
                    else
                        formatted.append(text);
                } else
                    formatted.append(text);
                return formatted.toString();
            }
        });

        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);


        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int Day = c.get(Calendar.DAY_OF_MONTH);

        String Date = year + "-" + month + "-" + Day;

        edt_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.hideKeypad(BackgroundCheckerActivity.this, BackgroundCheckerActivity.this.getCurrentFocus());

                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(BackgroundCheckerActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                        daes = String.valueOf(year) + '-' + month + '-' + day;

                        edt_select_date.setText(dateDesc);
                        edt_select_date.setError(null);
                        edt_select_date.setTextColor(ContextCompat.getColor(BackgroundCheckerActivity.this, R.color.black));

                    }
                }).textConfirm("CONFIRM") //text of confirm button
                        .textCancel("CANCEL") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                        .minYear(1900) //min year in loop
                        .maxYear(year) // max year in loop
                        .dateChose(Date) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(BackgroundCheckerActivity.this);
            }
        });


        btn_Previous = findViewById(R.id.btn_Previous);

        Btn_Next = findViewById(R.id.Btn_Next);


        Btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkbox.isChecked()) {

                    jumpToNextPage();

                } else {

                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.you_must_agree_to_receipt_of_disclosure_to_continue),
                            Snackbar.LENGTH_SHORT).show();



                }


            }
        });

        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jumpToBackPage();

            }
        });


    }

    public void jumpToNextPage() {

        if (currentStep < stepView.getStepCount() - 1) {
            currentStep++;
            stepView.go(currentStep, true);
        } else {
            stepView.done(true);
        }

        if (currentStep == 1) {

            btn_Previous.setVisibility(View.VISIBLE);
            checkboxtext.setText(getResources().getString(R.string.Rights));
            WebVie.loadUrl("file:///android_asset/Rights.html");

        }

        if (currentStep == 2) {

            checkboxtext.setText(getResources().getString(R.string.California_Disclosure_1));
            WebVie.loadUrl("file:///android_asset/CaliforniaDisclosure.html");
        }

        if (currentStep == 3) {

            checkboxtext.setText(getResources().getString(R.string.Acknowledgment));
            WebVie.loadUrl("file:///android_asset/Authorization.html");
        }

        if (currentStep == 4) {

            R1.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    public void jumpToBackPage() {

        if (currentStep > 0) {
            currentStep--;
        }
        stepView.done(false);
        stepView.go(currentStep, true);

        if (currentStep == 0) {

            btn_Previous.setVisibility(View.INVISIBLE);
            checkboxtext.setText(getResources().getString(R.string.Disclosure));
            WebVie.loadUrl("file:///android_asset/Disclosure.html");


        }

        if (currentStep == 1) {

            checkboxtext.setText(getResources().getString(R.string.Rights));
            WebVie.loadUrl("file:///android_asset/Rights.html");
        }

        if (currentStep == 2) {

            checkboxtext.setText(getResources().getString(R.string.California_Disclosure_1));
            WebVie.loadUrl("file:///android_asset/CaliforniaDisclosure.html");

        }


        if (currentStep == 3) {

            R1.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            checkboxtext.setText(getResources().getString(R.string.Acknowledgment));
            WebVie.loadUrl("file:///android_asset/Authorization.html");
        }
    }


    public void CheckerApi() {
        String status = NetworkUtils.getConnectivityStatus(BackgroundCheckerActivity.this);
        if (status.equals("404")) {
            Toast.makeText(BackgroundCheckerActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            if (validateFirstName()) {

                if (validateLastName()) {

                    if (validateemail()) {

                        if (validatedate()) {

                            if (validateSocial()) {

                                if (validateZip()) {

                                    if (validatePhone()) {

                                        CallCheckerApi();

                                    }

                                }

                            }

                        }

                    }
                }
            }

        }
    }


    private void CallCheckerApi() {

        final ProgressDialog progressdialog = new ProgressDialog(BackgroundCheckerActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getNewlClient().create(ApiService.class);

        String s_lName = edt_lastName.getText().toString();
        String s_mName = edt_middleName.getText().toString();
        String s_fName = edt_firstName.getText().toString();
        String s_date = edt_select_date.getText().toString();
        String s_email = edt_email.getText().toString();
        String s_phone = edt_phone.getText().toString();
        String s_zip = edt_Zip.getText().toString();
        String s_Social = edt_Social.getText().toString();
        boolean no_middle_name;

        no_middle_name = s_mName.length() <= 0;


        String basic = Credentials.basic(ApiUrls.Checkr_Api_Key, "");

        Call<CheckerModel> call = service.Checker(basic, s_fName, s_mName, s_lName, s_email
                , s_phone, s_zip, daes, s_Social, no_middle_name);
        call.enqueue(new Callback<CheckerModel>() {


            @Override
            public void onResponse(@NonNull Call<CheckerModel> call, @NonNull Response<CheckerModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    CheckerModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getError() == null) {

                        DataManager.getInstance().setPayment(false);

                        AppPrefs.setBooleanKeyvaluePrefs(BackgroundCheckerActivity.this, AppPrefs.KEY_Azzida_Verified, true);

                        AppPrefs.setStringKeyvaluePrefs(BackgroundCheckerActivity.this, AppPrefs.KEY_CandidateId, result.getId());

                        CallSaveDataApi(result.getId(), result.getFirstName(), result.getMiddleName(), result.getLastName(),
                                result.getDob(), result.getSsn(), result.getEmail(), result.getPhone(), result.getZipcode(), result.getCreatedAt(),
                                NetworkUtils.getLocalIpAddress());

                    } else {
                        popMessage(result.getError(), BackgroundCheckerActivity.this);
                    }

                } else if (response.code() == 400) {
                    String jsonData = "";
                    try {

                        jsonData = response.errorBody().string();

                        JSONObject result = new JSONObject(jsonData);

                        JSONArray a = result.getJSONArray("error");

                        for (int i = 0; i < a.length(); i++) {
                            Log.d("Type", a.getString(i));
                        }

                        ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0; i < a.length(); i++) {
                            list.add(a.get(i).toString());
                        }

                        if (list.size() > 0) {

                            String listString = "";

                            for (String s : list) {
                                listString += s + "\n";

                            }

                            popMessage(listString, BackgroundCheckerActivity.this);


                        }
                    } catch (Exception e) {
                        try {

                            JSONObject result = new JSONObject(jsonData);

                            String a = result.getString("error");

                            popMessage(a, BackgroundCheckerActivity.this);


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }


                } else {
                    // Server Problem
                    popMessage("Invalid User Credentials", BackgroundCheckerActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckerModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void CallSaveDataApi(String id, String firstName, String middleName, String lastName, String dob, String ssn, String email, String phone, String zipcode, String createdAt, String localIpAddress) {

        final ProgressDialog progressdialog = new ProgressDialog(BackgroundCheckerActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(BackgroundCheckerActivity.this, AppPrefs.KEY_User_ID);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SuccessModel> call = service.UpdateCandidateData(s_userId, id, firstName, lastName, middleName, dob, ssn, email,
                phone, zipcode, createdAt, localIpAddress);
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

                        AlertDialog.Builder b2 = new AlertDialog.Builder(BackgroundCheckerActivity.this);

                        RelativeLayout view2 = (RelativeLayout) BackgroundCheckerActivity.this.getLayoutInflater().inflate(R.layout.dialog_eula, null);

                        b2.setView(view2);


                        WebView eula_web_view = view2.findViewById(R.id.eula_web_view);

                        eula_web_view.loadUrl("file:///android_asset/AzzidaVerfifiedThanks.html");

                        Button bt_close = view2.findViewById(R.id.bt_close);
                        Button bt_pay = view2.findViewById(R.id.bt_pay);

                        bt_pay.setVisibility(View.GONE);

                        AlertDialog d2 = b2.create();

                        d2.setCanceledOnTouchOutside(false);
                        d2.setCancelable(false);
                        d2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        d2.show();

                        bt_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d2.cancel();
                                finish();

                            }
                        });


                    } else {
                        popMessage(result.getMessage(), BackgroundCheckerActivity.this);
                    }

                } else {
                    // Server Problem
                    Toast.makeText(BackgroundCheckerActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });


    }


    public void popMessage(String errorMsg,
                           final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMsg)
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

    public boolean validateSocial() {
        boolean valid = true;

        String s_Social = edt_Social.getText().toString();


        if (s_Social.isEmpty()) {
            edt_Social.setError("Enter SSN");
            edt_Social.requestFocus();
            valid = false;
        } else {
            edt_Social.setError(null);
        }

        return valid;
    }

    public boolean validateZip() {
        boolean valid = true;

        String s_zip = edt_Zip.getText().toString();


        if (s_zip.isEmpty()) {
            edt_Zip.setError("Enter Zip");
            edt_Zip.requestFocus();
            valid = false;
        } else {
            edt_Zip.setError(null);
        }

        return valid;
    }

    public boolean validatePhone() {
        boolean valid = true;

        String s_phone = edt_phone.getText().toString();


        if (s_phone.isEmpty()) {
            edt_phone.setError("Enter Phone");
            edt_phone.requestFocus();
            valid = false;
        } else {
            edt_phone.setError(null);
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

    public boolean validateFirstName() {
        boolean valid = true;

        String s_fName = edt_firstName.getText().toString();


        if (s_fName.isEmpty()) {
            edt_firstName.setError("Enter First Name");
            edt_firstName.requestFocus();
            valid = false;
        } else {
            edt_firstName.setError(null);
        }

        return valid;
    }


/*    public boolean validateMiddleName() {
        boolean valid = true;

        String s_mName = edt_middleName.getText().toString();


        if (s_mName.isEmpty()) {
            edt_middleName.setError("Enter First Name");
            edt_middleName.requestFocus();
            valid = false;
        } else {
            edt_middleName.setError(null);
        }

        return valid;
    }*/

    public boolean validateLastName() {
        boolean valid = true;

        String s_lName = edt_lastName.getText().toString();

        if (s_lName.isEmpty()) {
            edt_lastName.setError("Enter Last Name");
            edt_lastName.requestFocus();
            valid = false;
        } else {
            edt_lastName.setError(null);
        }
        return valid;
    }


}
