package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.azzida.R;
import com.azzida.adapter.NothingSelectedSpinnerAdapter;
import com.azzida.helper.Config;
import com.azzida.model.ApplicationAcceptedModel;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.DisputeModel;
import com.azzida.model.SuccessModel;
import com.azzida.model.TipModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.RetrofitUtils;
import com.azzida.utills.CreditCardFormattingTextWatcher;
import com.azzida.utills.CreditCardUtils;
import com.azzida.utills.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.CardUtils;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.azzida.utills.CreditCardUtils.AMEX;
import static com.azzida.utills.CreditCardUtils.DISCOVER;
import static com.azzida.utills.CreditCardUtils.MASTERCARD;
import static com.azzida.utills.CreditCardUtils.NONE;
import static com.azzida.utills.CreditCardUtils.VISA;

public class PaymentCheckOut extends AppCompatActivity {
    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';
    private final String[] month = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    TextView tv_member_name, tv_card_number, tv_validity, tv_cvv;
    ImageView ivType;
    int maxLength = 3;
    FrameLayout fr_card_Front, fr_card_Back;
    EditText NameOnCard, CardNumber, CardExpiry, CVV;
    TextView txt;
    private String Rating, Amount, TipAmount, TotalAmount, JobId, SeekerId, PaymentType, Screen, AmountPay, paymentId, TipId, PromoCode;
    private ProgressDialog dialog;
    private AlertDialog alert;
    private String Reason, Describe, filepath, PostAssociate, Amt;
    private ArrayList<String> years;
    private String spinner_Month, spinner_Year;
    private String spinner_MonthPosition, spinner_YearPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_checkout);
        PaymentConfiguration.init(getApplicationContext(), Config.Publishable_Key);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Rating = null;
                Amount = null;
                TipAmount = null;
                TotalAmount = null;
                SeekerId = null;
                PaymentType = null;
                JobId = null;
                Screen = null;
                paymentId = null;

                Reason = null;
                Describe = null;
                filepath = null;
                PostAssociate = null;
                PromoCode = null;

            } else {
                Rating = extras.getString("Rating");
                Amount = extras.getString("Amount");
                TipAmount = extras.getString("TipAmount");
                TotalAmount = extras.getString("TotalAmount");
                SeekerId = extras.getString("SeekerId");
                JobId = extras.getString("JobId");
                Screen = extras.getString("Screen");
                PaymentType = extras.getString("PaymentType");
                paymentId = extras.getString("paymentId");
                PromoCode = extras.getString("PromoCode");

                Reason = extras.getString("Reason");
                Describe = extras.getString("Describe");
                filepath = extras.getString("filepath");
                PostAssociate = extras.getString("PostAssociate");
            }
        } else {
            Rating = (String) savedInstanceState.getSerializable("Rating");
            Amount = (String) savedInstanceState.getSerializable("Amount");
            paymentId = (String) savedInstanceState.getSerializable("paymentId");
            TipAmount = (String) savedInstanceState.getSerializable("TipAmount");
            TotalAmount = (String) savedInstanceState.getSerializable("TotalAmount");
            PromoCode = (String) savedInstanceState.getSerializable("PromoCode");
            SeekerId = (String) savedInstanceState.getSerializable("SeekerId");
            Screen = (String) savedInstanceState.getSerializable("Screen");
            JobId = (String) savedInstanceState.getSerializable("JobId");
            PaymentType = (String) savedInstanceState.getSerializable("PaymentType");

            Reason = (String) savedInstanceState.getSerializable("Reason");
            Describe = (String) savedInstanceState.getSerializable("Describe");
            filepath = (String) savedInstanceState.getSerializable("filepath");
            PostAssociate = (String) savedInstanceState.getSerializable("PostAssociate");
        }


        InitView();
    }

    private void InitView() {

   /*     ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
        for (int i = thisYear; i <= 40; i++) {
            years.add(Integer.toString(i));
        }

        Log.e("TAG", "Year List: " + years );*/

        fr_card_Front = findViewById(R.id.fr_card_Front);
        fr_card_Back = findViewById(R.id.fr_card_Back);

        TipId = "0";

        if (PromoCode != null) {

            if (PromoCode.length() > 0) {

                PromoCode = PromoCode;

            } else {

                PromoCode = "";

            }

        }else {

            PromoCode = "";

        }


        txt = findViewById(R.id.txt);
        if (Screen.equalsIgnoreCase("0")){
            txt.setVisibility(View.VISIBLE);
        }else {
            txt.setVisibility(View.GONE);
        }
        tv_member_name = findViewById(R.id.tv_member_name);
        tv_card_number = findViewById(R.id.tv_card_number);
        tv_validity = findViewById(R.id.tv_validity);
        tv_cvv = findViewById(R.id.tv_cvv);

        ivType = findViewById(R.id.ivType);

        NameOnCard = findViewById(R.id.NameOnCard);
        CardNumber = findViewById(R.id.CardNumber);
        CardExpiry = findViewById(R.id.CardExpiry);
        CVV = findViewById(R.id.CVV);


        CardExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CardExpiry.getText().toString().length() > 0 && CardExpiry != null) {

                    String month = CardExpiry.getText().toString().substring(0, 2);
                    String year = CardExpiry.getText().toString().substring(3, 5);

                    spinner_Month = month;

                    spinner_Year = year;

                }

                AlertDialog.Builder b2 = new AlertDialog.Builder(PaymentCheckOut.this);

                CardView view2 = (CardView) PaymentCheckOut.this.getLayoutInflater().inflate(R.layout.dialog_date_picker, null);

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


                ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(PaymentCheckOut.this,
                        android.R.layout.simple_spinner_item, years);
                adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_year.setAdapter(
                        new NothingSelectedSpinnerAdapter(
                                adapterYear,
                                R.layout.spinner_row_selected_dispute,
                                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                PaymentCheckOut.this));

                if (spinner_YearPosition != null && !spinner_YearPosition.equals("null") && spinner_YearPosition.length() > 0) {

                    spinner_year.setSelection(Integer.parseInt(spinner_YearPosition) + 1);

                }

                ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(PaymentCheckOut.this,
                        android.R.layout.simple_spinner_item, month);
                adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_month.setAdapter(
                        new NothingSelectedSpinnerAdapter(
                                adapterMonth,
                                R.layout.spinner_row_selected_dispute,
                                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                                PaymentCheckOut.this));

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

                                Toast.makeText(PaymentCheckOut.this, "Select Year", Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            Toast.makeText(PaymentCheckOut.this, "Select Month", Toast.LENGTH_SHORT).show();


                        }

                    }
                });


            }
        });


        CardNumber.addTextChangedListener(new CreditCardFormattingTextWatcher(CardNumber, tv_card_number, ivType, new CreditCardFormattingTextWatcher.CreditCardType() {
            @Override
            public void setCardType(int type) {
                Log.d("Card", "setCardType: " + type);

                setCardTypeN(type);

                if (type == 4) {
                    CVV.getText().clear();
                    maxLength = 4;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    CVV.setFilters(fArray);
                } else {
                    CVV.getText().clear();
                    maxLength = 3;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    CVV.setFilters(fArray);

                }
            }
        }));


        CVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (TextUtils.isEmpty(editable.toString().trim()))
                    tv_cvv.setText("XXX");
                else
                    tv_cvv.setText(editable.toString());


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

    public void setCardTypeN(int type) {
        switch (type) {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
                break;

        }


    }


    public void Pay(View view) {

        String cardName = NameOnCard.getText().toString();
        String cardNumber = CardNumber.getText().toString();
        String cardValidity = CardExpiry.getText().toString();
        String cardCVV = CVV.getText().toString();

        boolean cardNumberIsValid =
                CardUtils.isValidCardNumber(cardNumber);

        if (TextUtils.isEmpty(cardName)) {
            Snackbar.make(findViewById(android.R.id.content), "Enter Valid Name", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ", ""))) {
            Snackbar.make(findViewById(android.R.id.content), "Enter Valid card number", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity) || !CreditCardUtils.isValidDate(cardValidity)) {
            Snackbar.make(findViewById(android.R.id.content), "Enter correct validity", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV) || cardCVV.length() < maxLength) {
            Snackbar.make(findViewById(android.R.id.content), "Enter valid security number", Snackbar.LENGTH_SHORT).show();
        } else if (!cardNumberIsValid) {
            Snackbar.make(findViewById(android.R.id.content), "Enter Valid card number", Snackbar.LENGTH_SHORT).show();
        } else {

            int month = Integer.parseInt(CardExpiry.getText().toString().substring(0, 2));
            int year = Integer.parseInt(CardExpiry.getText().toString().substring(3, 5));

            String status = NetworkUtils.getConnectivityStatus(PaymentCheckOut.this);
            String s_Balance = AppPrefs.getStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance);

            switch (Screen) {
                case "0": {
                    if (status.equals("404")) {
                        Toast.makeText(PaymentCheckOut.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                    } else {

                        String Msg = "";

                        if (s_Balance.equalsIgnoreCase("0")) {

                            Msg = "Amount to be paid: $" + TotalAmount;
                            AmountPay = TotalAmount;

                        } else {

                            try {

                                double bal1 = Double.parseDouble(TotalAmount);
                                double bal2 = Double.parseDouble(s_Balance);
                                double tt = bal1 + bal2;
                                double Total = 0.00;

                                DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                String formatted = formatter.format(bal1);

                                if (bal1 > bal2) {

                                    Total = bal1 - bal2;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);

                                } else {

                                    Total = bal2 - bal1;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                        builder.setMessage(Msg)
                                .setCancelable(false)

                                .setPositiveButton(Html.fromHtml("<font color='#3e77bb'>Yes</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        getCardValue(cardNumber, month, year, cardCVV);
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


                    }


                    break;
                }
                case "1": {

                    if (status.equals("404")) {
                        Toast.makeText(PaymentCheckOut.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                    } else {


                        String Msg = "";

                        if (s_Balance.equalsIgnoreCase("0")) {

                            Msg = "Amount to be paid: $" + TipAmount;
                            AmountPay = TipAmount;

                        } else {

                            try {

                                double bal1 = Double.parseDouble(TipAmount);
                                double bal2 = Double.parseDouble(s_Balance);
                                double tt = bal1 + bal2;
                                double Total = 0.00;

                                DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                String formatted = formatter.format(bal1);


                                if (bal1 > bal2) {

                                    Total = bal1 - bal2;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);

                                } else {

                                    Total = bal2 - bal1;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                        builder.setMessage(Msg)
                                .setCancelable(false)

                                .setPositiveButton(Html.fromHtml("<font color='#3e77bb'>Yes</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        getCardValue(cardNumber, month, year, cardCVV);
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


                    }


                    break;
                }
                case "4":

                case "5": {

                    if (status.equals("404")) {

                        Toast.makeText(PaymentCheckOut.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();

                    } else {


                        String s_Balan = AppPrefs.getStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance);

                        String Msg = "";

                        if (s_Balan.equalsIgnoreCase("0")) {

                            Msg = "Amount to be paid: $" + TotalAmount;
                            AmountPay = TotalAmount;

                        } else {

                            try {

                                double bal1 = Double.parseDouble(TotalAmount);
                                double bal2 = Double.parseDouble(s_Balance);
                                double Total = 0.00;

                                DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                String formatted = formatter.format(bal1);

                                if (bal1 > bal2) {

                                    Total = bal1 - bal2;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);

                                } else {

                                    Total = bal2 - bal1;
                                    Msg = "Total Payment: $" + formatted;
                                    Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                    Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                    AmountPay = new DecimalFormat("#.00").format(bal1);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                        builder.setMessage(Msg)
                                .setCancelable(false)

                                .setPositiveButton(Html.fromHtml("<font color='#3e77bb'>Yes</font>"), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        getCardValue(cardNumber, month, year, cardCVV);
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


                    }


                    break;
                }

                default: {

                    if (PaymentType.equals("payment")) {

                        if (status.equals("404")) {
                            Snackbar.make(findViewById(android.R.id.content), R.string.CheckYourInternetConnection, Snackbar.LENGTH_SHORT).show();
                        } else {

                            getCardValue(cardNumber, month, year, cardCVV);

                        }

                    } else {

                        if (status.equals("404")) {
                            Toast.makeText(PaymentCheckOut.this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                        } else {

                            getCardValueNew(cardNumber, month, year, cardCVV);

                        }
                    }


                    break;
                }
            }


        }


    }

    public void Cancel(View view) {

        finish();
    }


    private void getCardValue(String cardNum, int expMonth, int expYear, String cvv) {

        spinnerStart(PaymentCheckOut.this, getResources().getString(R.string.pleasewait));
        Card cardToSave = Card.create(cardNum, expMonth, expYear, cvv);
/*
        PaymentMethodCreateParams.Card params = cardToSave.toPaymentMethodParamsCard();
*/

        if (!cardToSave.validateCard()) {
            popMessage("", "Invalid Card Data", this);
            spinnerStop();
            return;
        }

        Stripe stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());

        stripe.createToken(cardToSave, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(@NonNull Token result) {

                String ty = String.valueOf(result.getType());

                Log.e("card", Objects.requireNonNull(Objects.requireNonNull(result.getCard()).getLast4()));
                Log.e("TAG", ty);
                Log.e("cardtype", String.valueOf(result.getCard().getBrand()));
                Log.e("stripe_token", result.getId());
                spinnerStop();
                createPayment(result.getId());

            }

            @Override
            public void onError(@NonNull Exception e) {
                spinnerStop();
                popMessage("", "" + e, PaymentCheckOut.this);
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

    private void getCardValueNew(String cardNum, int expMonth, int expYear, String cvv) {

        spinnerStart(PaymentCheckOut.this, getResources().getString(R.string.pleasewait));
        Card cardToSave = Card.create(cardNum, expMonth, expYear, cvv);
/*
        PaymentMethodCreateParams.Card params = cardToSave.toPaymentMethodParamsCard();
*/

        if (!cardToSave.validateCard()) {
            popMessage("", "Invalid Card Data", this);
            spinnerStop();
            return;
        }

        Stripe stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());

        stripe.createToken(cardToSave, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(@NonNull Token result) {

                String ty = String.valueOf(result.getType());

                Log.e("card", Objects.requireNonNull(Objects.requireNonNull(result.getCard()).getLast4()));
                Log.e("TAG", ty);
                Log.e("cardtype", String.valueOf(result.getCard().getBrand()));
                Log.e("stripe_token", result.getId());
                spinnerStop();
                createPaymentNew(result.getId());

            }

            @Override
            public void onError(@NonNull Exception e) {
                spinnerStop();
                popMessage("", "" + e, PaymentCheckOut.this);
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


    private void createPayment(String Token) {


        final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        String s_Balance = AppPrefs.getStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance);
        String sTotal = "";
        if (PaymentType.equalsIgnoreCase("Tip")) {

            sTotal = TipAmount;


        } else if (PaymentType.equalsIgnoreCase("payment")) {

            sTotal = AmountPay;

        } else if (PaymentType.equalsIgnoreCase("Checker")) {

            sTotal = AmountPay;

        } else if (PaymentType.equalsIgnoreCase("CancelJob")) {

            sTotal = AmountPay;

        }

        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, SeekerId, s_Balance, "", sTotal, Token, PaymentType, PromoCode, Config.Token_Used);
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
                                    AppPrefs.setStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    switch (Screen) {
                                        case "0":

                                            ApplicationAccepted();


                                            break;
                                        case "1":

                                            setTip();

                                            break;
                                        case "4":

                                            setResult(2);
                                            DataManager.getInstance().setPayment(true);
                                            finish();

                                            break;

                                        case "5":

                                            CancelJobApi();


                                            break;
                                    }


                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                    Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    private void CancelJobApi() {

        final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Call<SuccessModel> call = service.CancelJob(JobId, Reason, s_userId, ts);
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

                        setResult(5);
                        finish();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                    Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void ApplicationAccepted() {
        final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<ApplicationAcceptedModel> call = service.ApplicationAccepted(JobId, SeekerId, s_userId, "true");
        call.enqueue(new Callback<ApplicationAcceptedModel>() {


            @Override
            public void onResponse(@NonNull Call<ApplicationAcceptedModel> call, @NonNull Response<ApplicationAcceptedModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ApplicationAcceptedModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (result.getData().getId() != null) {

                                setResult(2);
                                finish();

                            } else {

                            }

                        }
                    } else {

                        popMessage("", result.getMessage(), PaymentCheckOut.this);

                    }

                } else {
                    // Server Problem
                    Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicationAcceptedModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void createPaymentNew(String Token) {


        final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        if (TotalAmount.equals("5")) {

            Amt = TotalAmount;

        } else {

            double amount = Double.parseDouble(TotalAmount);
            double res = (amount * 5) / 100;
            Amt = String.valueOf(res);

            if (res > 5.0) {

                Amt = String.valueOf(res);

            } else {

                Amt = "5";

            }

        }
        String s_Balance = AppPrefs.getStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance);
        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, "0", s_Balance, "", Amt, Token, PaymentType, PromoCode, Config.Token_Used);
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
                                    AppPrefs.setStringKeyvaluePrefs(PaymentCheckOut.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    setDispute();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                    Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    private void setDispute() {

        if (filepath.length() > 0) {


            final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

            HashMap<String, String> requestValuePairsMap = new HashMap<>();
            requestValuePairsMap.put("Id", "0");
            requestValuePairsMap.put("UserId", s_userId);
            requestValuePairsMap.put("JobId", JobId);
            requestValuePairsMap.put("DisputeReason", Reason);
            requestValuePairsMap.put("PostAssociate", PostAssociate);
            requestValuePairsMap.put("Description", Describe);
            Log.e("Dispute", requestValuePairsMap.toString());
            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<DisputeModel> call = service.Dispute(RetrofitUtils.createMultipartRequest(requestValuePairsMap),
                    RetrofitUtils.createFilePart("image", filepath, RetrofitUtils.MEDIA_TYPE_IMAGE_ALL));
            call.enqueue(new Callback<DisputeModel>() {


                @Override
                public void onResponse(@NonNull Call<DisputeModel> call, @NonNull Response<DisputeModel> response) {
                    progressdialog.dismiss();
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        DisputeModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("1")) {
                            if (result.getData() != null) {
                                if (result.getData().getId() != null) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                                    builder.setMessage("Successfully Raised Dispute")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    setResult(2);
                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                        Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DisputeModel> call, @NonNull Throwable t) {
                    progressdialog.dismiss();
                }
            });

        } else {

            final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<DisputeModel> call = service.DisputeNew("0", s_userId, JobId, Reason, PostAssociate, Describe, "");
            call.enqueue(new Callback<DisputeModel>() {


                @Override
                public void onResponse(@NonNull Call<DisputeModel> call, @NonNull Response<DisputeModel> response) {
                    progressdialog.dismiss();
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        DisputeModel result = response.body();
                        Log.d("fgh", response.toString());
                        assert result != null;
                        if (result.getStatus().equals("1")) {
                            if (result.getData() != null) {
                                if (result.getData().getId() != null) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                                    builder.setMessage("Successfully Raised Dispute")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    setResult(2);
                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                        Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DisputeModel> call, @NonNull Throwable t) {
                    progressdialog.dismiss();
                }
            });


        }


    }


    private void setTip() {

        final ProgressDialog progressdialog = new ProgressDialog(PaymentCheckOut.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<TipModel> call = service.Tip("0", s_userId, JobId, TipAmount, TotalAmount, SeekerId, Rating, paymentId);
        call.enqueue(new Callback<TipModel>() {


            @Override
            public void onResponse(@NonNull Call<TipModel> call, @NonNull Response<TipModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    TipModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
                                builder.setMessage("Payment Success")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                setResult(2);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setTitle(getString(R.string.Alert));
                                alert.show();


                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentCheckOut.this);
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
                    Toast.makeText(PaymentCheckOut.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TipModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

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


}
