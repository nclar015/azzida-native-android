package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.GetSaveCardAdpater;
import com.azzida.adapter.NothingSelectedSpinnerAdapter;
import com.azzida.helper.Config;
import com.azzida.helper.PermissionsHelper;
import com.azzida.model.CreatePaymentModel;
import com.azzida.model.DisputeModel;
import com.azzida.model.GetCustomerCardsModel;
import com.azzida.model.GetCustomerCardsModelDatum;
import com.azzida.model.PostAssociateModel;
import com.azzida.model.PostAssociateModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.RetrofitUtils;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisputeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FROM_GALLERY = 8;
    private static final int PICKUP_FROM_CAMERA = 9;
    TextView title;
    ImageView profile_back, img_Attachment;
    LinearLayout Lin_img_Attachment;
    EditText edt_Reason, edt_Describe;
    Spinner spinner_Post_associated;
    String JobSele, JobId, JobAmount, Amount, AmountPay, AmtRef;
    /*
        Spinner  spinner_Contact;
    */
    ArrayList<String> itemsJob;
    CheckBox checkbox;
    TextView checkboxtext;
    RecyclerView RecyclerPayment;
    private Bitmap finalBitmap;
    private String selectedImagePath = "";
    private String ImagePath = "";
    private TextView titleImg;
    private GetSaveCardAdpater getSaveCardAdpater;
    private ArrayList<GetCustomerCardsModelDatum> arraySaveCard;
    private ArrayList<PostAssociateModelDatum> postAssociateModelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute);

        InitView();


        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getJobList();
            getSaveCard();

        }
    }


    private void InitView() {

        itemsJob = new ArrayList<String>();

        title = findViewById(R.id.title);

        title.setText("Back to Profile");

        checkbox = findViewById(R.id.checkBox1);
        checkboxtext = findViewById(R.id.checkboxtext);
        titleImg = findViewById(R.id.titleImg);

        checkboxtext.setText(Html.fromHtml("I have read and agree to the Azzida " +
                "<a href='https://azzida.com/odd_jobs/azzida-terms-of-use/#Disputes'>Dispute Resolution Policy</a>"));
        checkboxtext.setClickable(true);
        checkboxtext.setMovementMethod(LinkMovementMethod.getInstance());

        edt_Reason = findViewById(R.id.edt_Reason);
        edt_Describe = findViewById(R.id.edt_Describe);

        profile_back = findViewById(R.id.profile_back);

        img_Attachment = findViewById(R.id.img_Attachment);
        Lin_img_Attachment = findViewById(R.id.Lin_img_Attachment);

/*

        spinner_Contact = findViewById(R.id.spinner_Contact);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

*/
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select your favorite Planet!");*/
/*
        spinner_Contact.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_selected_dispute,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/
        /*spinner_How_long.setAdapter(adapter);*/


        spinner_Post_associated = findViewById(R.id.spinner_Post_associated);

        spinner_Post_associated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);

                JobSele = String.valueOf(item);
                if (position > 0) {

                    JobId = String.valueOf(postAssociateModelData.get(position - 1).getId());
                    JobAmount = String.valueOf(postAssociateModelData.get(position - 1).getAmount());

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*spinner_How_long.setAdapter(adapter);*/

        profile_back.setOnClickListener(this);
        Lin_img_Attachment.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                finish();

                break;

            case R.id.Lin_img_Attachment:

                if (PermissionsHelper.areExplicitPermissionsRequired()) {
                    List<String> required = PermissionsHelper.isAllPremissiongranted(DisputeActivity.this);
                    if (required != null && required.size() > 0) {
                        PermissionsHelper.show(DisputeActivity.this,
                                getResources().getString(R.string.app_name), required);
                    } else {
                        //if (checkOverlayOn()) {
                        selectImage();
                        //  }
                    }

                }
                break;

        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    callCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    callGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        ImagePath = image.getAbsolutePath();
        return image;
    }

    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("TAG", "Error occurred while creating the File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.azzida.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                ClipData clip = ClipData.newUri(getContentResolver(), "capturedImage", photoUri);
                takePictureIntent.setClipData(clip);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(takePictureIntent, PICKUP_FROM_CAMERA);
            }
        } else {
            Toast.makeText(this, "No Camera app found on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public void callGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.ORIENTATION};
            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);

            img_Attachment.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            cursor.close();

            if (picturePath == null || picturePath.trim().length() == 0) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(
                            selectedImage);
                    finalBitmap = BitmapFactory.decodeStream(imageStream);
                    selectedImagePath = Utils.saveToInternalSorage(finalBitmap);
                    titleImg.setVisibility(View.GONE);
                    finalBitmap = Utils.decodeFile(selectedImagePath);
                    if (finalBitmap != null) {
//                        Drawable drawable = new BitmapDrawable(finalBitmap);
//                        image.setImageBitmap(finalBitmap);
//                        Log.e("Tag", "onActivityResult: "+finalBitmap );
//                         String photo = selectedImagePath;

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                selectedImagePath = picturePath;
                titleImg.setVisibility(View.GONE);

            }

        } else if (requestCode == PICKUP_FROM_CAMERA && resultCode == RESULT_OK) {


            selectedImagePath = compressImage(ImagePath);
            titleImg.setVisibility(View.GONE);


            if (selectedImagePath.length() > 0) {

                File file = new File(selectedImagePath);
                Bitmap thumbnail = BitmapFactory.decodeFile(file.getPath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                img_Attachment.setImageBitmap(thumbnail);

            }


        } else if (resultCode == 2) {

            finish();
        }


    }

    public void Dispute_Submit(View view) {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (validate()) {
                if (checkbox.isChecked()) {
                    try {

                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(DisputeActivity.this, AppPrefs.KEY_Balance);

                        if (JobAmount.equals("5")) {

                            Amount = JobAmount;

                        } else {

                            double amount = Double.parseDouble(JobAmount);
                            double res = (amount * 5) / 100;
                            Amount = String.valueOf(res);

                            if (res > 5.0) {

                                Amount = String.valueOf(res);

                            } else {

                                Amount = "5.00";

                            }

                        }


                        double bal1 = Double.parseDouble(Amount);
                        double bal2 = Double.parseDouble(s_Balance);

                        if (bal2 > bal1) {

                            String Msg = "Amount debited from Referral Balance: $" + Amount;

                            AmountPay = Amount;
                            AmtRef = Amount;

                            AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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

                            AlertDialog.Builder b = new AlertDialog.Builder(DisputeActivity.this, R.style.MyCustomTheme);

                            LinearLayout PaymentView = (LinearLayout) DisputeActivity.this.getLayoutInflater().inflate(R.layout.dialog_payment, null);

                            TextView Txt_Direct_PAy = PaymentView.findViewById(R.id.Txt_Direct_PAy);
                            RecyclerPayment = PaymentView.findViewById(R.id.RecyclerPayment);
                            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                            RecyclerPayment.setLayoutManager(mLayoutManager);
                            RecyclerPayment.setHasFixedSize(true);
                            RecyclerPayment.setItemAnimator(new DefaultItemAnimator());
                            RecyclerPayment.setAdapter(getSaveCardAdpater);
                            b.setView(PaymentView);

                            final AlertDialog d = b.create();

                            d.setCanceledOnTouchOutside(true);
                            d.setCancelable(true);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                            WindowManager.LayoutParams wmlp = d.getWindow().getAttributes();

                            wmlp.gravity = Gravity.BOTTOM;

                            Window window = d.getWindow();
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            d.show();

                            Txt_Direct_PAy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d.cancel();
                                    Intent Dispute_Resolution = new Intent(DisputeActivity.this, PaymentCheckOut.class);
                                    Dispute_Resolution.putExtra("TotalAmount", JobAmount);
                                    Dispute_Resolution.putExtra("JobId", JobId);
                                    Dispute_Resolution.putExtra("PaymentType", "Dispute");
                                    Dispute_Resolution.putExtra("Reason", edt_Reason.getText().toString());
                                    Dispute_Resolution.putExtra("Describe", edt_Describe.getText().toString());
                                    Dispute_Resolution.putExtra("filepath", selectedImagePath);
                                    Dispute_Resolution.putExtra("PostAssociate", JobSele);
                                    Dispute_Resolution.putExtra("Screen", "2");
                                    startActivityForResult(Dispute_Resolution, 2);

                                }
                            });


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(this, "Agree the Policy", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }


    private void getJobList() {
        final ProgressDialog progressdialog = new ProgressDialog(DisputeActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();

        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<PostAssociateModel> call = service.PostAssociate(s_userId);
        call.enqueue(new Callback<PostAssociateModel>() {


            @Override
            public void onResponse(@NonNull Call<PostAssociateModel> call, @NonNull Response<PostAssociateModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    PostAssociateModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            for (int i = 0; i < result.getData().size(); i++) {

                                postAssociateModelData = result.getData();
                                itemsJob.add(result.getData().get(i).getPostassociate());


                            }

                            itemsJob.size();

                            ArrayAdapter<String> adapterNew = new ArrayAdapter<String>(DisputeActivity.this,
                                    android.R.layout.simple_spinner_item, itemsJob);
                            adapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner_Post_associated.setAdapter(
                                    new NothingSelectedSpinnerAdapter(
                                            adapterNew,
                                            R.layout.spinner_row_feed,
                                            DisputeActivity.this));

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                    Toast.makeText(DisputeActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostAssociateModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }

    private void getSaveCard() {
        final ProgressDialog progressdialog = new ProgressDialog(DisputeActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        String s_userId = AppPrefs.getStringKeyvaluePrefs(DisputeActivity.this, AppPrefs.KEY_User_ID);
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

                            if (arraySaveCard != null && arraySaveCard.size() > 0 && getSaveCardAdpater != null) {

                                arraySaveCard.clear();
                                getSaveCardAdpater.notifyDataSetChanged();
                            }

                            arraySaveCard = result.getData();

                            getSaveCardAdpater = new GetSaveCardAdpater(DisputeActivity.this, new GetSaveCardAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {

                                    try {

                                        String s_Balance = AppPrefs.getStringKeyvaluePrefs(DisputeActivity.this, AppPrefs.KEY_Balance);

                                        double bal1 = Double.parseDouble(Amount);
                                        double bal2 = Double.parseDouble(s_Balance);
                                        double tt = bal1 + bal2;
                                        double Total = 0.00;

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

                                                Msg = "Do you want to pay with " + arraySaveCard.get(position).getCardNumber();
                                                Msg = Msg + "\n\n" + "Amount debited from Referral Balance: $" + s_Balance;
                                                Msg = Msg + "\n\n" + "Amount to be paid from Card: $" + new DecimalFormat("#.00").format(Total);
                                                AmountPay = new DecimalFormat("#.00").format(bal1);
                                                AmtRef = s_Balance;
                                            }


                                        }


                                        AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                    Toast.makeText(DisputeActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCustomerCardsModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void createPayment(String customerId) {

        final ProgressDialog progressdialog = new ProgressDialog(DisputeActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

        Call<CreatePaymentModel> call = service.CreatePayment(JobId, s_userId, "0", AmtRef, customerId, AmountPay, "", "Dispute","", Config.Token_Used);
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
                                    AppPrefs.setStringKeyvaluePrefs(DisputeActivity.this, AppPrefs.KEY_Balance, result.getData().getRefBalance().replaceAll("\\.0*$", ""));

                                    setDispute();

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                    Toast.makeText(DisputeActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreatePaymentModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });

    }


    private void setDispute() {

        if (selectedImagePath.length() > 0) {

            final ProgressDialog progressdialog = new ProgressDialog(DisputeActivity.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

            String s_reason = edt_Reason.getText().toString();
            String s_describe = edt_Describe.getText().toString();

            String filepath = selectedImagePath;

            HashMap<String, String> requestValuePairsMap = new HashMap<>();
            requestValuePairsMap.put("Id", "0");
            requestValuePairsMap.put("UserId", s_userId);
            requestValuePairsMap.put("JobId", JobId);
            requestValuePairsMap.put("DisputeReason", s_reason);
            requestValuePairsMap.put("PostAssociate", JobSele);
            requestValuePairsMap.put("Description", s_describe);
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


                                    AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
                                    builder.setMessage("Successfully Raised Dispute")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    img_Attachment.setImageResource(0);
                                                    img_Attachment.setImageResource(R.drawable.addattachment_plusoicon);
                                                    img_Attachment.requestLayout();


                                                    edt_Reason.getText().clear();
                                                    edt_Describe.getText().clear();
                                                    dialogInterface.cancel();

                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                        Toast.makeText(DisputeActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DisputeModel> call, @NonNull Throwable t) {
                    progressdialog.dismiss();
                }
            });


        } else {

            final ProgressDialog progressdialog = new ProgressDialog(DisputeActivity.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);

            String s_reason = edt_Reason.getText().toString();
            String s_describe = edt_Describe.getText().toString();

            String filepath = selectedImagePath;

            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<DisputeModel> call = service.DisputeNew("0", s_userId, JobId, s_reason, JobSele, s_describe, " ");
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


                                    AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
                                    builder.setMessage("Successfully Raised Dispute")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    img_Attachment.setImageResource(0);
                                                    img_Attachment.setImageResource(R.drawable.addattachment_plusoicon);
                                                    img_Attachment.requestLayout();


                                                    edt_Reason.getText().clear();
                                                    edt_Describe.getText().clear();
                                                    dialogInterface.cancel();

                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DisputeActivity.this);
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
                        Toast.makeText(DisputeActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DisputeModel> call, @NonNull Throwable t) {
                    progressdialog.dismiss();
                }
            });


        }


    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public boolean validate() {
        boolean valid = true;

        String s_reason = edt_Reason.getText().toString();
        String s_describe = edt_Describe.getText().toString();


        if (s_reason.isEmpty()) {
            edt_Reason.setError("Enter Reason");
            edt_Reason.requestFocus();
            valid = false;
        } else {
            edt_Reason.setError(null);
        }

        if (s_describe.isEmpty()) {
            edt_Describe.setError("Enter Describe");
            edt_Describe.requestFocus();
            valid = false;
        } else {
            edt_Describe.setError(null);
        }

        return valid;
    }
}
