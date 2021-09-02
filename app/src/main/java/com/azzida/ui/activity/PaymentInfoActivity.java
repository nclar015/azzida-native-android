package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.NothingSelectedSpinnerAdapter;
import com.azzida.adapter.SaveCardAdpater;
import com.azzida.helper.Config;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.model.SaveCardModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.CreditCardUtils;
import com.azzida.utills.NetworkUtils;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.CardUtils;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';
    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    private static int CARD_CVC_TOTAL_SYMBOLS = 3;
    private final String[] month = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    LinearLayout addPayment;
    EditText NameOnCard, CardNumber, CardExpiry, CVV;
    int maxLength = 3;
    TextView title;
    ImageView profile_back;
    ArrayList<String> listOfPattern;
    Card cardToSave;
    private Stripe stripe;
    private PopupWindow mPopupWindow;
    private ProgressDialog dialog;
    private AlertDialog alert;
    private LinearLayout noData;
    private AlertDialog CardSave;
    private RecyclerView RecyclerSaveCard;
    private ArrayList<String> years;
    private String spinner_Month, spinner_Year;
    private String spinner_MonthPosition, spinner_YearPosition;
    private SaveCardAdpater saveCardAdpater;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        PaymentConfiguration.init(getApplicationContext(), Config.Publishable_Key);

        InitView();
    }

    private void InitView() {

        title = findViewById(R.id.title);

        title.setText("Back to Profile");

        profile_back = findViewById(R.id.profile_back);

        noData = findViewById(R.id.noData);

        RecyclerSaveCard = findViewById(R.id.RecyclerSaveCard);

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        RecyclerSaveCard.setLayoutManager(mLayoutManager);
        RecyclerSaveCard.setHasFixedSize(true);
        RecyclerSaveCard.setItemAnimator(new DefaultItemAnimator());


        addPayment = findViewById(R.id.addPayment);


        listOfPattern = new ArrayList<String>();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        profile_back.setOnClickListener(this);
        addPayment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                finish();

                break;

            case R.id.addPayment:

                AlertDialog.Builder b = new AlertDialog.Builder(PaymentInfoActivity.this);

                RelativeLayout view = (RelativeLayout) PaymentInfoActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment_option_add, null);

                b.setView(view);

                ImageView img_close = view.findViewById(R.id.img_close);
                LinearLayout save_card = view.findViewById(R.id.save_card);

                NameOnCard = view.findViewById(R.id.NameOnCard);
                CardNumber = view.findViewById(R.id.CardNumber);
                CardExpiry = view.findViewById(R.id.CardExpiry);
                CVV = view.findViewById(R.id.CVV);

                CardExpiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (CardExpiry.getText().toString().length() > 0 && CardExpiry != null) {

                            String month = CardExpiry.getText().toString().substring(0, 2);
                            String year = CardExpiry.getText().toString().substring(3, 5);

                            spinner_Month = month;

                            spinner_Year = year;

                        }

                        AlertDialog.Builder b2 = new AlertDialog.Builder(PaymentInfoActivity.this);

                        CardView view2 = (CardView) PaymentInfoActivity.this.getLayoutInflater().inflate(R.layout.dialog_date_picker, null);

                        years = new ArrayList<String>();
                        int thisYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
                        for (int i = thisYear; i <= 40; i++) {
                            years.add(Integer.toString(i));
                        }

                        for (int i = 0; i < years.size(); i++) {

                            if (years.get(i).equals(spinner_Year)) {

                                spinner_YearPosition = String.valueOf(i);

                            }

                        }

                        ArrayList<String> HowLIt = new ArrayList<String>(Arrays.asList(month));

                        for (int i = 0; i < HowLIt.size(); i++) {

                            if (HowLIt.get(i).equals(spinner_Month)) {

                                spinner_MonthPosition = String.valueOf(i);

                            }

                        }


                        Spinner spinner_month = view2.findViewById(R.id.spinner_month);
                        Spinner spinner_year = view2.findViewById(R.id.spinner_year);


                        Button btn_close = view2.findViewById(R.id.btn_close);
                        Button btn_submit = view2.findViewById(R.id.btn_submit);


                        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(PaymentInfoActivity.this,
                                android.R.layout.simple_spinner_item, years);
                        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_year.setAdapter(
                                new NothingSelectedSpinnerAdapter(
                                        adapterYear,
                                        R.layout.spinner_row_selected_dispute,
                                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                        PaymentInfoActivity.this));

                        if (spinner_YearPosition != null && !spinner_YearPosition.equals("null") && spinner_YearPosition.length() > 0) {

                            spinner_year.setSelection(Integer.parseInt(spinner_YearPosition) + 1);

                        }

                        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(PaymentInfoActivity.this,
                                android.R.layout.simple_spinner_item, month);
                        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner_month.setAdapter(
                                new NothingSelectedSpinnerAdapter(
                                        adapterMonth,
                                        R.layout.spinner_row_selected_dispute,
                                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                        PaymentInfoActivity.this));

                        if (spinner_MonthPosition != null && !spinner_MonthPosition.equals("null") && spinner_MonthPosition.length() > 0) {

                            spinner_month.setSelection(Integer.parseInt(spinner_MonthPosition) + 1);

                        }


                        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Object item = parent.getItemAtPosition(position);

                                spinner_Month = String.valueOf(item);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                Object item = parent.getItemAtPosition(position);

                                spinner_Year = String.valueOf(item);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                        b2.setView(view2);

                        final AlertDialog d2 = b2.create();

                        d2.setCanceledOnTouchOutside(false);
                        d2.setCancelable(false);
                        d2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        d2.show();

                        btn_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d2.cancel();
                            }
                        });


                        btn_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (spinner_Month.length() > 0 && spinner_Month != null && !spinner_Month.equals("null")) {

                                    if (spinner_Year.length() > 0 && spinner_Year != null && !spinner_Year.equals("null")) {

                                        CardExpiry.setText(spinner_Month + spinner_Year);
                                        d2.cancel();

                                    } else {

                                        Toast.makeText(PaymentInfoActivity.this, "Select Year", Toast.LENGTH_SHORT).show();

                                    }
                                } else {

                                    Toast.makeText(PaymentInfoActivity.this, "Select Month", Toast.LENGTH_SHORT).show();


                                }

                            }
                        });


                    }
                });

                CardNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (!isInputCorrectl(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
                            s.replace(0, s.length(), concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
                        }

                        String ccNum = s.toString().replaceAll("\\s|-", "");
                        for (String p : listOfPattern) {
                            if (ccNum.matches(p)) {
                                Log.d("DEBUG", "afterTextChanged : " + p);
                                if (p.equals("^3[47][0-9]{5,}$")) {
                                    CVV.getText().clear();
                                    maxLength = 4;
                                    CARD_CVC_TOTAL_SYMBOLS = 4;
                                    InputFilter[] fArray = new InputFilter[1];
                                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                                    CVV.setFilters(fArray);
                                } else {
                                    CVV.getText().clear();
                                    CARD_CVC_TOTAL_SYMBOLS = 3;
                                    maxLength = 3;
                                    InputFilter[] fArray = new InputFilter[1];
                                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                                    CVV.setFilters(fArray);

                                }
                                break;
                            }
                        }

                    }
                });


                CardExpiry.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (!isInputCorrectl(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                            s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
                        }

                    }
                });


                CVV.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
                            s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
                        }

                    }
                });




/*


                CardExpiry.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String current = s.toString();
                        if (current.length() == 2 && start == 1) {
                            CardExpiry.setText(current + "/");
                            CardExpiry.setSelection(current.length() + 1);
                        } else if (current.length() == 2 && before == 1) {
                            current = current.substring(0, 1);
                            CardExpiry.setText(current);
                            CardExpiry.setSelection(current.length());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {


                    }
                });
*/


                CardSave = b.create();

                CardSave.setCanceledOnTouchOutside(false);
                CardSave.setCancelable(true);
                CardSave.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                CardSave.show();

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardSave.cancel();
                    }
                });

                save_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateName()) {

                            if (validateCard()) {

                                if (validateExpiry()) {

                                    if (validateCVV()) {

                                        String Nameoncard = NameOnCard.getText().toString();
                                        String CardNum = CardNumber.getText().toString();
                                        String Expiry = CardExpiry.getText().toString();
                                        int month = Integer.parseInt(CardExpiry.getText().toString().substring(0, 2));
                                        int year = Integer.parseInt(CardExpiry.getText().toString().substring(3, 5));
                                        String Cvv = CVV.getText().toString();

                                        boolean cardNumberIsValid =
                                                CardUtils.isValidCardNumber(CardNum);

                                        boolean validateCardExpiryDateFro =
                                                CardExpiry.getText().toString().matches("(?:0[1-9]|1[0-2])/[0-9]{2}");

                                        boolean validateCardExpiryDate =
                                                CreditCardUtils.isValidDate(CardExpiry.getText().toString());

                                        boolean validateCardCvv = Cvv.length() == maxLength;

/*
                        String Nameoncard = "demo";
                        String CardNum = "4000056655665556";
                        int expMonth = 12;
                        int expYear = 2022;
                        String Cvv = "123";*/

                                        String status = NetworkUtils.getConnectivityStatus(PaymentInfoActivity.this);
                                        if (status.equals("404")) {
                                            Toast.makeText(PaymentInfoActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (cardNumberIsValid) {
                                                if (validateCardExpiryDateFro) {

                                                    if (validateCardExpiryDate) {

                                                        if (validateCardCvv) {

                                                            getCardValue(CardNum, month, year, Cvv);

                                                        } else {

                                                            Toast.makeText(PaymentInfoActivity.this, "Enter Valid Card Cvv", Toast.LENGTH_SHORT).show();

                                                        }


                                                    } else {

                                                        Toast.makeText(PaymentInfoActivity.this, "Enter Valid Card Expiry", Toast.LENGTH_SHORT).show();

                                                    }

                                                } else {

                                                    Toast.makeText(PaymentInfoActivity.this, "Enter Valid Card Expiry", Toast.LENGTH_SHORT).show();

                                                }


                                            } else {

                                                Toast.makeText(PaymentInfoActivity.this, "Enter Valid Card Number", Toast.LENGTH_SHORT).show();

                                            }


                                        }


                                    }
                                }


                            }

                        }
                    }
                });

                break;

        }
    }


    private void getCardValue(String cardNum, int expMonth, int expYear, String cvv) {

        spinnerStart(PaymentInfoActivity.this, getResources().getString(R.string.pleasewait));
        cardToSave = Card.create(cardNum, expMonth, expYear, cvv);
/*
        PaymentMethodCreateParams.Card params = cardToSave.toPaymentMethodParamsCard();
*/

        if (!cardToSave.validateCard()) {
            popMessage("", "Invalid Card Data", this);
            spinnerStop();
            return;
        }

        stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());

        stripe.createToken(cardToSave, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(@NonNull Token result) {

                String ty = String.valueOf(result.getType());

                Log.e("card", Objects.requireNonNull(Objects.requireNonNull(result.getCard()).getLast4()));
                Log.e("TAG", ty);
                Log.e("cardtype", String.valueOf(result.getCard().getBrand()));
                Log.e("stripe_token", result.getId());
                spinnerStop();
                callSaveapi(result.getId());

            }

            @Override
            public void onError(@NonNull Exception e) {
                spinnerStop();
                popMessage("", "" + e, PaymentInfoActivity.this);
            }
        });
/*


        stripe.createPaymentMethod(PaymentMethodCreateParams.create(params), new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                String paymentMethodId = result.id;
                // Send paymentMethodId to your server for the next steps
            }

            @Override
            public void onError(@NonNull Exception e) {
                // Display the error to the user
            }
        });

*/

    }

    private void getSaveCard() {
        final ProgressDialog progressdialog = new ProgressDialog(PaymentInfoActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(PaymentInfoActivity.this, AppPrefs.KEY_User_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetCustomerCardsModel> call = service.GetCustomerCards(s_userId);
        call.enqueue(new Callback<GetCustomerCardsModel>() {


            @Override
            public void onResponse(@NonNull Call<GetCustomerCardsModel> call, @NonNull Response<GetCustomerCardsModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetCustomerCardsModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arraySaveCard != null && arraySaveCard.size() > 0 && saveCardAdpater != null) {

                                arraySaveCard.clear();
                                saveCardAdpater.notifyDataSetChanged();
                            }

                            arraySaveCard = result.getData();

                            saveCardAdpater = new SaveCardAdpater(PaymentInfoActivity.this, new SaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentInfoActivity.this);
                                    builder.setMessage("Do You Want To Remove Card")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    deleteCardApi(arraySaveCard.get(position).getId());
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle(getString(R.string.Alert));
                                    alert.show();


                                }
                            }, arraySaveCard);

                            RecyclerSaveCard.setAdapter(saveCardAdpater);

                            if (!(result.getData().size() > 0)) {

                                RecyclerSaveCard.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                RecyclerSaveCard.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);

                            }

                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentInfoActivity.this);
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
                    Toast.makeText(PaymentInfoActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void deleteCardApi(Integer id) {
        final ProgressDialog progressdialog = new ProgressDialog(PaymentInfoActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SuccessModel> call = service.DeleteCard(String.valueOf(id));
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

                        String status = NetworkUtils.getConnectivityStatus(PaymentInfoActivity.this);
                        if (status.equals("404")) {
                            Toast.makeText(PaymentInfoActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            getSaveCard();

                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentInfoActivity.this);
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
                    Toast.makeText(PaymentInfoActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    private void callSaveapi(String id) {
        final ProgressDialog progressdialog = new ProgressDialog(PaymentInfoActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(PaymentInfoActivity.this, AppPrefs.KEY_User_ID);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SaveCardModel> call = service.SaveCard(id, s_userId, Config.Token_Used);
        call.enqueue(new Callback<SaveCardModel>() {


            @Override
            public void onResponse(@NonNull Call<SaveCardModel> call, @NonNull Response<SaveCardModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SaveCardModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {

                        CardSave.cancel();
                        String status = NetworkUtils.getConnectivityStatus(PaymentInfoActivity.this);
                        if (status.equals("404")) {
                            Toast.makeText(PaymentInfoActivity.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            getSaveCard();

                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentInfoActivity.this);
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
                    Toast.makeText(PaymentInfoActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveCardModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }


    private boolean isInputCorrectl(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }


    public boolean validateCard() {
        boolean valid = true;

        String s_FirstName = CardNumber.getText().toString();

        if (s_FirstName.isEmpty()) {
            CardNumber.setError("Enter Card Number");
            CardNumber.requestFocus();
            valid = false;
        } else {
            CardNumber.setError(null);
        }

        return valid;
    }

    public boolean validateName() {
        boolean valid = true;

        String s_LastName = NameOnCard.getText().toString();


        if (s_LastName.isEmpty()) {
            NameOnCard.setError("Enter Name");
            NameOnCard.requestFocus();
            valid = false;
        } else {
            NameOnCard.setError(null);
        }
        return valid;
    }

    public boolean validateExpiry() {
        boolean valid = true;

        String s_CardExpiry = CardExpiry.getText().toString();


        if (s_CardExpiry.isEmpty()) {
            CardExpiry.setError("Enter Expiry");
            CardExpiry.requestFocus();
            valid = false;
        } else {
            CardExpiry.setError(null);
        }
        return valid;
    }

    public boolean validateCVV() {
        boolean valid = true;

        String s_LastName = CVV.getText().toString();


        if (s_LastName.isEmpty()) {
            CVV.setError("Enter CVV");
            CVV.requestFocus();
            valid = false;
        } else {
            CVV.setError(null);
        }
        return valid;
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }


    public void spinnerStart(Context context, String text) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = ProgressDialog.show(context, "", text, true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void spinnerStop() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void popMessage(String titleMsg, String errorMsg,
                           Context context) {
        if (alert != null && alert.isShowing()) {
            alert.setMessage(errorMsg);
            return;
        }
        // pop error message
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleMsg).setMessage(errorMsg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getSaveCard();

        }
    }
}
