package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.BackgroundChecker.BackgroundCheckerActivity;
import com.azzida.R;
import com.azzida.adapter.GetSaveCardAdpater;
import com.azzida.helper.Config;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.GetCheckerReportModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionsActivity extends AppCompatActivity {

    RecyclerView RecyclerPayment;
    private TextView title, title_Card, txt_Status;
    private ImageView profile_back;
    private CardView Card_Background_Check;
    private AlertDialog Option;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;
    private GetSaveCardAdpater getSaveCardAdpater;
    private String Amount, AmountPay, AmtRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        initView();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getSaveCard();

        }
    }


    private void initView() {

        profile_back = findViewById(R.id.profile_back);

        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title = findViewById(R.id.title);
        title_Card = findViewById(R.id.title_Card);
        title.setText(R.string.Option);

        txt_Status = findViewById(R.id.txt_Status);

        txt_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getCheckerReport();

            }
        });

        if (AppPrefs.getBooleanKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_Azzida_Verified)) {

            txt_Status.setVisibility(View.VISIBLE);

        }

        Amount = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_BackgroundCheck);

        title_Card.setText("Background Check Badge Application: $" + Amount);

        Card_Background_Check = findViewById(R.id.Card_Background_Check);

        Card_Background_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!AppPrefs.getBooleanKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_Azzida_Verified)) {


                    if (!DataManager.getInstance().getPayment()) {


                        AlertDialog.Builder b2 = new AlertDialog.Builder(OptionsActivity.this);

                        RelativeLayout view2 = (RelativeLayout) OptionsActivity.this.getLayoutInflater().inflate(R.layout.dialog_eula, null);

                        b2.setView(view2);


                        WebView eula_web_view = view2.findViewById(R.id.eula_web_view);

                        eula_web_view.loadUrl("file:///android_asset/AzzidaVerfified.html");

                        Button bt_close = view2.findViewById(R.id.bt_close);
                        Button bt_pay = view2.findViewById(R.id.bt_pay);


                        AlertDialog d2 = b2.create();

                        d2.setCanceledOnTouchOutside(false);
                        d2.setCancelable(false);
                        d2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        d2.show();

                        bt_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d2.cancel();

                            }
                        });

                        bt_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d2.cancel();

                                if (Amount != null) {


                                    String s_Balance = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_Balance);

                                    double bal1 = Double.parseDouble(Amount);
                                    double bal2 = Double.parseDouble(s_Balance);

                                    if (bal2 > bal1) {

                                        String Msg = "Amount debited from Referral Balance: $" + Amount;

                                        AmountPay = Amount;
                                        AmtRef = Amount;

                                        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
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

                                        AlertDialog.Builder b = new AlertDialog.Builder(OptionsActivity.this, R.style.MyCustomTheme);

                                        LinearLayout PaymentView = (LinearLayout) OptionsActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment, null);

                                        TextView Txt_Direct_PAy = PaymentView.findViewById(R.id.Txt_Direct_PAy);
                                        RecyclerPayment = PaymentView.findViewById(R.id.RecyclerPayment);
                                        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(OptionsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                        RecyclerPayment.setLayoutManager(mLayoutManager);
                                        RecyclerPayment.setHasFixedSize(true);
                                        RecyclerPayment.setItemAnimator(new DefaultItemAnimator());
                                        RecyclerPayment.setAdapter(getSaveCardAdpater);
                                        b.setView(PaymentView);

                                        Option = b.create();

                                        Option.setCanceledOnTouchOutside(true);
                                        Option.setCancelable(true);
                                        Objects.requireNonNull(Option.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                                        WindowManager.LayoutParams wmlp = Option.getWindow().getAttributes();

                                        wmlp.gravity = Gravity.BOTTOM;

                                        Window window = Option.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        Option.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                        Option.show();

                                        Txt_Direct_PAy.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Option.cancel();
                                                Intent myIntent = new Intent(OptionsActivity.this, PaymentCheckOut.class);
                                                myIntent.putExtra("SeekerId", "0");
                                                myIntent.putExtra("TotalAmount", Amount);
                                                myIntent.putExtra("JobId", "0");
                                                myIntent.putExtra("PaymentType", "Checker");
                                                myIntent.putExtra("Screen", "4");
                                                startActivityForResult(myIntent, 2);

                                            }
                                        });

                                    }


                                } else {

                                    Toast.makeText(OptionsActivity.this, "Amount Null", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    } else {

                        Intent myIntent = new Intent(OptionsActivity.this, BackgroundCheckerActivity.class);
                        startActivity(myIntent);

                    }

                } else {


                    Snackbar.make(findViewById(android.R.id.content), "Already verified", Snackbar.LENGTH_SHORT).show();


                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {

            Intent myIntent = new Intent(OptionsActivity.this, BackgroundCheckerActivity.class);
            startActivity(myIntent);
        }
    }

    private void getSaveCard() {
        String s_userId = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_User_ID);
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

                            getSaveCardAdpater = new GetSaveCardAdpater(OptionsActivity.this, new GetSaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    try {

                                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_Balance);

                                        double bal1 = Double.parseDouble(Amount);
                                        double bal2 = Double.parseDouble(s_Balance);
                                        double tt = bal1 + bal2;
                                        double Total = 0.0;

                                        DecimalFormat formatter = new DecimalFormat("#,#,##.00");
                                        String formatted = formatter.format(bal1);

                                        String Msg = "";

                                        if (s_Balance.equalsIgnoreCase("0")) {

                                            Msg = "Do you want to pay with card ending " + arraySaveCard.get(position).getCardNumber() + "?";
                                            Msg = Msg + "\n" + "Amount to be paid: $" + formatted;
                                            AmountPay = Amount;
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

                                        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
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


                           /* if (!(result.getData().size() > 0)) {

                                RecyclerPayment.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                            } else {

                                RecyclerPayment.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);

                            }
*/
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
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
                    Toast.makeText(OptionsActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {

            }
        });
    }


    private void getCheckerReport() {
        Utils.showAlrtDialg(OptionsActivity.this, "");
        String s_userId = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_User_ID);
        String s_candidateId = AppPrefs.getStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_CandidateId);
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetCheckerReportModel> call = service.CheckerReport(s_userId, s_candidateId);
        call.enqueue(new Callback<GetCheckerReportModel>() {


            @Override
            public void onResponse(@NonNull Call<GetCheckerReportModel> call, @NonNull Response<GetCheckerReportModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetCheckerReportModel result = response.body();
                    Log.d("fgh", response.toString());
                    Utils.dismisAlrtDialog();
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            AlertDialog.Builder b1 = new AlertDialog.Builder(OptionsActivity.this);

                            CardView view1 = (CardView) OptionsActivity.this.getLayoutInflater().inflate(R.layout.dialog_status, null);

                            LinearLayout back = view1.findViewById(R.id.back);

                            TextView txt_Pending = view1.findViewById(R.id.txt_Pending);

                            txt_Pending.setClickable(true);
                            txt_Pending.setMovementMethod(LinkMovementMethod.getInstance());
                            String text = "<a href='https://candidate.checkr.com/view#login'> Checkr </a>";
                            txt_Pending.setText(Html.fromHtml(text));

                            LinearLayout L1 = view1.findViewById(R.id.L1);
                            LinearLayout L2 = view1.findViewById(R.id.L2);
                            LinearLayout L3 = view1.findViewById(R.id.L3);
                            LinearLayout L4 = view1.findViewById(R.id.L4);
                            LinearLayout L5 = view1.findViewById(R.id.L5);

                            if (result.getData().getReportStatus().equalsIgnoreCase("clear")) {

                                L5.setVisibility(View.GONE);

                            } else {

                                L5.setVisibility(View.VISIBLE);

                            }

                            if (result.getData().getSsnTraceStatus().equalsIgnoreCase("consider")) {

                                L1.setBackgroundResource(R.drawable.border_fill_pink);

                            } else if (result.getData().getSsnTraceStatus().equalsIgnoreCase("clear")) {

                                L1.setBackgroundResource(R.drawable.border_fill_green);

                            }

                            if (result.getData().getSexOffenderStatus().equalsIgnoreCase("consider")) {

                                L2.setBackgroundResource(R.drawable.border_fill_pink);

                            } else if (result.getData().getSexOffenderStatus().equalsIgnoreCase("clear")) {

                                L2.setBackgroundResource(R.drawable.border_fill_green);

                            }

                            if (result.getData().getGlobalStatus().equalsIgnoreCase("consider")) {

                                L3.setBackgroundResource(R.drawable.border_fill_pink);

                            } else if (result.getData().getGlobalStatus().equalsIgnoreCase("clear")) {

                                L3.setBackgroundResource(R.drawable.border_fill_green);

                            }

                            if (result.getData().getNationalStatus().equalsIgnoreCase("consider")) {

                                L4.setBackgroundResource(R.drawable.border_fill_pink);

                            } else if (result.getData().getNationalStatus().equalsIgnoreCase("clear")) {

                                L4.setBackgroundResource(R.drawable.border_fill_green);

                            }

                            TextView txt_SSN = view1.findViewById(R.id.txt_SSN);
                            TextView txt_Sex = view1.findViewById(R.id.txt_Sex);
                            TextView txt_Global = view1.findViewById(R.id.txt_Global);
                            TextView txt_National = view1.findViewById(R.id.txt_National);

                            txt_SSN.setText(result.getData().getSsnTraceStatus());
                            txt_Sex.setText(result.getData().getSexOffenderStatus());
                            txt_Global.setText(result.getData().getGlobalStatus());
                            txt_National.setText(result.getData().getNationalStatus());


                            b1.setView(view1);

                            final AlertDialog d1 = b1.create();

                            d1.setCanceledOnTouchOutside(false);
                            d1.setCancelable(false);
                            d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            d1.show();

                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    d1.cancel();

                                }
                            });


                        }


                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCheckerReportModel> call, @NonNull Throwable t) {

            }
        });
    }


    private void createPayment(String customerId) {

        final ProgressDialog progressdialog = new ProgressDialog(OptionsActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<CreatePaymentModel> call = service.CreatePayment("0", s_userId, "0", AmtRef, customerId, AmountPay, "", "Checker", "", Config.Token_Used);
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

                                    AppPrefs.setStringKeyvaluePrefs(OptionsActivity.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    DataManager.getInstance().setPayment(true);
                                    Option.cancel();

                                    Intent myIntent = new Intent(OptionsActivity.this, BackgroundCheckerActivity.class);
                                    startActivity(myIntent);

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
                        builder.setMessage(result.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(OptionsActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }


}
