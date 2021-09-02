package com.azzida.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.azzida.R;
import com.azzida.adapter.NothingSelectedSpinnerAdapter;
import com.azzida.helper.CustomGooglePlacesSearch;
import com.azzida.helper.PermissionsHelper;
import com.azzida.model.CreateJobModel;
import com.azzida.model.GetJobCategoryModel;
import com.azzida.model.SaveJobImagesModel;
import com.azzida.model.SuccessModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.perfrences.DataManager;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.RetrofitUtils;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFeedActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FROM_GALLERY = 8;
    private static final int PICKUP_FROM_CAMERA = 9;

    private static final int PICK_FROM_GALLERY2 = 10;
    private static final int PICKUP_FROM_CAMERA2 = 11;


    private static final int PICK_FROM_GALLERY3 = 12;
    private static final int PICKUP_FROM_CAMERA3 = 13;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE = 18945;
    ImageView img_back_feed;
    Spinner spinner_How_long, spinner_categorize;
    String How_long, categorize;
    EditText edt_title, edt_amount, edt_work_details;
    TextView edt_select_date, edt_location;
    Calendar myCalendar;
    ImageView img_cam_hide, img_cam_hide2, img_cam_hide3;
    RelativeLayout R1Image, R2Image, R3Image;
    TextView txt_1, txt_2, txt_3;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog datePickerDialog;
    CircleImageView iv_profile_image, iv_profile_image2, iv_profile_image3;
  /*  String[] itemsNew = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"
            , "14", "15", "16", "17", "18", "19", "20"};*/

    /*
        String[] itemsNewTime = new String[]{"Hour(s)", " Day(s)", "Month(s)"};
    */
    String[] itemsNew = new String[]{"Less than an hour", "1-2 hours", "Half a day", "Whole day",
            "Up to a week", "Up to a month", "Not sure"};
    ArrayList<String> items;
    String Address;
    String arrayListProfi = "";
    String Lat, Long;
    String[] ite;
    private ArrayList<String> arrayList;
    private Bitmap finalBitmap, finalBitmap2, finalBitmap3;

    private String selectedImagePath = "", selectedImagePath2 = "", selectedImagePath3 = "";

    private String ImagePath = "", ImagePath2 = "", ImagePath3 = "";

    private long timeInMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);


        items = new ArrayList<String>();

        arrayList = new ArrayList<>();


        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getJobCategory();

        }

        initView();
    }


    private void initView() {

        myCalendar = Calendar.getInstance();

        img_back_feed = findViewById(R.id.img_back_feed);

        img_cam_hide = findViewById(R.id.img_cam_hide);
        img_cam_hide2 = findViewById(R.id.img_cam_hide2);
        img_cam_hide3 = findViewById(R.id.img_cam_hide3);


        R1Image = findViewById(R.id.R1Image);
        R2Image = findViewById(R.id.R2Image);
        R3Image = findViewById(R.id.R3Image);

        txt_1 = findViewById(R.id.txt_1);
        txt_2 = findViewById(R.id.txt_2);
        txt_3 = findViewById(R.id.txt_3);


        edt_title = findViewById(R.id.edt_title);
        edt_select_date = findViewById(R.id.edt_select_date);
        edt_amount = findViewById(R.id.edt_amount);
        edt_location = findViewById(R.id.edt_location);

        edt_work_details = findViewById(R.id.edt_work_details);

        iv_profile_image = findViewById(R.id.iv_profile_image);
        iv_profile_image2 = findViewById(R.id.iv_profile_image2);
        iv_profile_image3 = findViewById(R.id.iv_profile_image3);


        spinner_How_long = findViewById(R.id.spinner_How_long);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemsNew);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select your favorite Planet!");*/
        spinner_How_long.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_row_selected_feed,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        /*spinner_How_long.setAdapter(adapter);*/

        spinner_How_long.setSelection(1);

        spinner_categorize = findViewById(R.id.spinner_categorize);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        spinner_How_long.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);

                How_long = String.valueOf(item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinner_categorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);

                categorize = String.valueOf(item);

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        img_back_feed.setOnClickListener(this);

        edt_select_date.setOnClickListener(this);

        iv_profile_image.setOnClickListener(this);
        iv_profile_image2.setOnClickListener(this);
        iv_profile_image3.setOnClickListener(this);


        edt_location.setOnClickListener(this);
    }


    private void getJobCategory() {

        final ProgressDialog progressdialog = new ProgressDialog(AddFeedActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<GetJobCategoryModel> call = service.GetJobCategory();
        call.enqueue(new Callback<GetJobCategoryModel>() {


            @Override
            public void onResponse(@NonNull Call<GetJobCategoryModel> call, @NonNull Response<GetJobCategoryModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetJobCategoryModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            for (int i = 0; i < result.getData().size(); i++) {


                                items.add(result.getData().get(i).getCategoryName());


                            }

                            items.size();

                            ite = new String[items.size()];
                            ite = items.toArray(ite);

                            for (String s : ite) {
                                Log.e("string is", s);
                            }

                            ArrayAdapter<String> adapterNew = new ArrayAdapter<String>(AddFeedActivity.this,
                                    android.R.layout.simple_spinner_item, items);
                            adapterNew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner_categorize.setAdapter(
                                    new NothingSelectedSpinnerAdapter(
                                            adapterNew,
                                            R.layout.spinner_row_feed,
                                            AddFeedActivity.this));

                        }
                    } else {

                        popMessage(result.getMessage(), AddFeedActivity.this);

                    }

                } else {
                    // Server Problem
                    Toast.makeText(AddFeedActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobCategoryModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back_feed:

                DataManager.getInstance().setAddress("");
                DataManager.getInstance().setLat("");
                DataManager.getInstance().setLong("");
                finish();


                break;

            case R.id.edt_select_date:

                datePickerDialog = new DatePickerDialog(AddFeedActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

                break;

            case R.id.edt_location:


                Intent intent = new Intent(AddFeedActivity.this, CustomGooglePlacesSearch.class);
                intent.putExtra("type", "Job");
              /*  intent.putExtra("cursor", "source");
                intent.putExtra("s_address", serviceSource.getText().toString());*/
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE);


                break;


            case R.id.iv_profile_image:


                if (PermissionsHelper.areExplicitPermissionsRequired()) {
                    List<String> required = PermissionsHelper.isAllPremissiongranted(AddFeedActivity.this);
                    if (required != null && required.size() > 0) {
                        PermissionsHelper.show(AddFeedActivity.this,
                                getResources().getString(R.string.app_name), required);
                    } else {
                        //if (checkOverlayOn()) {
                        selectImage();
                        //  }
                    }

                }
                break;

            case R.id.iv_profile_image2:


                if (PermissionsHelper.areExplicitPermissionsRequired()) {
                    List<String> required = PermissionsHelper.isAllPremissiongranted(AddFeedActivity.this);
                    if (required != null && required.size() > 0) {
                        PermissionsHelper.show(AddFeedActivity.this,
                                getResources().getString(R.string.app_name), required);
                    } else {
                        //if (checkOverlayOn()) {
                        selectImage2();
                        //  }
                    }

                }
                break;

            case R.id.iv_profile_image3:


                if (PermissionsHelper.areExplicitPermissionsRequired()) {
                    List<String> required = PermissionsHelper.isAllPremissiongranted(AddFeedActivity.this);
                    if (required != null && required.size() > 0) {
                        PermissionsHelper.show(AddFeedActivity.this,
                                getResources().getString(R.string.app_name), required);
                    } else {
                        //if (checkOverlayOn()) {
                        selectImage3();
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
                    callCamera(PICKUP_FROM_CAMERA);
                } else if (options[item].equals("Choose from Gallery")) {
                    callGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void selectImage2() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    callCamera(PICKUP_FROM_CAMERA2);
                } else if (options[item].equals("Choose from Gallery")) {
                    callGallery2();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void selectImage3() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    callCamera(PICKUP_FROM_CAMERA3);
                } else if (options[item].equals("Choose from Gallery")) {
                    callGallery3();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


/*
    public void callCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.setType("image/*");
        startActivityForResult(intent, PICKUP_FROM_CAMERA);
    }
*/


    private File createImageFile(int pickupFromCamera) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents

        if (pickupFromCamera == 9) {

            ImagePath = image.getAbsolutePath();

        } else if (pickupFromCamera == 11) {

            ImagePath2 = image.getAbsolutePath();

        } else if (pickupFromCamera == 13) {

            ImagePath3 = image.getAbsolutePath();

        }

        return image;
    }

    private void callCamera(int pickupFromCamera) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(pickupFromCamera);
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

                startActivityForResult(takePictureIntent, pickupFromCamera);
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


    public void callGallery2() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_GALLERY2);
    }


    public void callGallery3() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_GALLERY3);
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_select_date.setText(sdf.format(myCalendar.getTime()));
        edt_select_date.setError(null);
        edt_select_date.setTextColor(ContextCompat.getColor(AddFeedActivity.this, R.color.black));

        timeInMilliseconds = 0;

        SimpleDateFormat sdfs = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.getDefault());//""EEE MMM dd HH:mm:ss z yyyy");
        String nextDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(new Date(myCalendar.getTime().getTime()));

        try {
            java.util.Date mDate = sdfs.parse(nextDate+" "+"23:59:59");//sdf.parse(givenDateString);
            if (mDate != null) {
                timeInMilliseconds = mDate.getTime();
            }


        } catch (Exception e) {
            Toast.makeText(this,"error in date"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

        Log.d("TAG", "Date in milli" + timeInMilliseconds);


        Log.e("TAG", String.valueOf(myCalendar.getTime()));


    }

    public void Post(View view) {
        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            if (validatetitle()) {
                if (validatedate()) {
                    if (validateamount()) {
                        if (validatelocation()) {
                            if (validateworkdetails()) {
                                if (selectedImagePath.length() > 0) {

                                    if (!categorize.equals("null")) {
                                        if (!How_long.equals("null")) {

                                            SaveImage();

                                        } else {

                                            Toast.makeText(this, "Select How long", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {

                                        Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    setCreateJob();

                                }


                            }
                        }
                    }
                }
            }
        }

    }

    private void SaveImage() {
        List<MultipartBody.Part> parts = new ArrayList<>();

        if (arrayList != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < arrayList.size(); i++) {
                parts.add(RetrofitUtils.createFilePart("image", arrayList.get(i), RetrofitUtils.MEDIA_TYPE_IMAGE_ALL));

            }
        }
        final ProgressDialog progressdialog = new ProgressDialog(AddFeedActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SaveJobImagesModel> call = service.SaveJobImages(parts);
        call.enqueue(new Callback<SaveJobImagesModel>() {


            @Override
            public void onResponse(@NonNull Call<SaveJobImagesModel> call, @NonNull Response<SaveJobImagesModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SaveJobImagesModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            String[] mStringArray = new String[result.getData().size()];
                            mStringArray = result.getData().toArray(mStringArray);

                            List<String> langList = new ArrayList<>();
                            for (String item : mStringArray) {
                                langList.add("\"" + item + "\"");
                            }

                            try {
                                JSONArray jsonObject = new JSONArray(langList.toString());
                                arrayListProfi = String.valueOf(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /*arrayListProfi = Arrays.toString(mStringArray);*/


                            setCreateJob();
                        }
                    } else {
                        popMessage(result.getMessage(), AddFeedActivity.this);
                    }

                } else {
                    // Server Problem
                    Toast.makeText(AddFeedActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveJobImagesModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.ORIENTATION};
            assert selectedImage != null;
            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);

            iv_profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            img_cam_hide.setVisibility(View.GONE);
            txt_1.setVisibility(View.GONE);
            if (selectedImagePath2.length() > 0) {
                txt_2.setVisibility(View.GONE);
            } else {

                txt_2.setVisibility(View.VISIBLE);
            }

            R2Image.setVisibility(View.VISIBLE);

            cursor.close();

            if (picturePath == null || picturePath.trim().length() == 0) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(
                            selectedImage);
                    finalBitmap = BitmapFactory.decodeStream(imageStream);
                    selectedImagePath = Utils.saveToInternalSorage(finalBitmap);
                    arrayList.add(Utils.saveToInternalSorage(finalBitmap));
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
                arrayList.add(picturePath);
            }

        } else if (requestCode == PICKUP_FROM_CAMERA && resultCode == RESULT_OK) {


            selectedImagePath = compressImage(ImagePath);
            arrayList.add(selectedImagePath);

            if (selectedImagePath.length() > 0) {

                File file = new File(selectedImagePath);
                Bitmap thumbnail = BitmapFactory.decodeFile(file.getPath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


                iv_profile_image.setImageBitmap(thumbnail);
                img_cam_hide.setVisibility(View.GONE);
                txt_1.setVisibility(View.GONE);
                if (selectedImagePath2.length() > 0) {
                    txt_2.setVisibility(View.GONE);
                } else {

                    txt_2.setVisibility(View.VISIBLE);
                }
                R2Image.setVisibility(View.VISIBLE);

            }


        } else if (requestCode == PICK_FROM_GALLERY2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.ORIENTATION};
            assert selectedImage != null;
            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);

            iv_profile_image2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            img_cam_hide.setVisibility(View.GONE);
            img_cam_hide2.setVisibility(View.GONE);
            txt_1.setVisibility(View.GONE);
            txt_2.setVisibility(View.GONE);
            txt_3.setVisibility(View.GONE);
            R3Image.setVisibility(View.VISIBLE);

            cursor.close();

            if (picturePath == null || picturePath.trim().length() == 0) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(
                            selectedImage);
                    finalBitmap2 = BitmapFactory.decodeStream(imageStream);
                    selectedImagePath2 = Utils.saveToInternalSorage(finalBitmap2);
                    arrayList.add(Utils.saveToInternalSorage(finalBitmap2));
                    finalBitmap2 = Utils.decodeFile(selectedImagePath2);
                    if (finalBitmap2 != null) {
//                        Drawable drawable = new BitmapDrawable(finalBitmap);
//                        image.setImageBitmap(finalBitmap);
//                        Log.e("Tag", "onActivityResult: "+finalBitmap );
//                         String photo = selectedImagePath2;

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                selectedImagePath2 = picturePath;
                arrayList.add(picturePath);
            }

        } else if (requestCode == PICKUP_FROM_CAMERA2 && resultCode == RESULT_OK) {


            selectedImagePath2 = compressImage(ImagePath2);
            arrayList.add(selectedImagePath2);

            if (selectedImagePath2.length() > 0) {

                File file = new File(selectedImagePath2);
                Bitmap thumbnail = BitmapFactory.decodeFile(file.getPath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                iv_profile_image2.setImageBitmap(thumbnail);
                img_cam_hide.setVisibility(View.GONE);
                img_cam_hide2.setVisibility(View.GONE);
                txt_1.setVisibility(View.GONE);
                txt_2.setVisibility(View.GONE);
                txt_3.setVisibility(View.GONE);
                R3Image.setVisibility(View.VISIBLE);


            }


        } else if (requestCode == PICK_FROM_GALLERY3 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.ORIENTATION};
            assert selectedImage != null;
            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);

            iv_profile_image3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            img_cam_hide.setVisibility(View.GONE);
            img_cam_hide2.setVisibility(View.GONE);
            img_cam_hide3.setVisibility(View.GONE);
            txt_1.setVisibility(View.GONE);
            txt_2.setVisibility(View.GONE);
            txt_3.setVisibility(View.GONE);

            cursor.close();

            if (picturePath == null || picturePath.trim().length() == 0) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(
                            selectedImage);
                    finalBitmap3 = BitmapFactory.decodeStream(imageStream);
                    selectedImagePath3 = Utils.saveToInternalSorage(finalBitmap3);
                    arrayList.add(Utils.saveToInternalSorage(finalBitmap3));
                    finalBitmap3 = Utils.decodeFile(selectedImagePath3);
                    if (finalBitmap3 != null) {
//                        Drawable drawable = new BitmapDrawable(finalBitmap);
//                        image.setImageBitmap(finalBitmap);
//                        Log.e("Tag", "onActivityResult: "+finalBitmap );
//                         String photo = selectedImagePath3;

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                selectedImagePath3 = picturePath;
                arrayList.add(picturePath);
                arrayList.size();
            }

        } else if (requestCode == PICKUP_FROM_CAMERA3 && resultCode == RESULT_OK) {

            selectedImagePath3 = compressImage(ImagePath3);
            arrayList.add(selectedImagePath3);

            if (selectedImagePath3.length() > 0) {

                File file = new File(selectedImagePath3);
                Bitmap thumbnail = BitmapFactory.decodeFile(file.getPath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                iv_profile_image3.setImageBitmap(thumbnail);
                img_cam_hide.setVisibility(View.GONE);
                img_cam_hide2.setVisibility(View.GONE);
                img_cam_hide3.setVisibility(View.GONE);
                txt_1.setVisibility(View.GONE);
                txt_2.setVisibility(View.GONE);
                txt_3.setVisibility(View.GONE);


            }


        }


    }


    private void setCreateJob() {

        Utils.showProgressdialog(AddFeedActivity.this, "Please Wait....");

        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        String s_title = edt_title.getText().toString();
        String s_date = String.valueOf(timeInMilliseconds);
        String s_amount = edt_amount.getText().toString();
        String s_location = edt_location.getText().toString();
        String s_work_details = edt_work_details.getText().toString();
        String filepath = selectedImagePath;
/*
        HashMap<String, String> requestValuePairsMap = new HashMap<>();
        requestValuePairsMap.put("Id", "0");
        requestValuePairsMap.put("UserId", s_userId);
        requestValuePairsMap.put("JobTitle", s_title);
        requestValuePairsMap.put("HowLong", How_long);
        requestValuePairsMap.put("Amount", s_amount);
        requestValuePairsMap.put("JobCategory", categorize);
        requestValuePairsMap.put("Location", s_location);
        requestValuePairsMap.put("FromDate", s_date);
        requestValuePairsMap.put("JobDescription", s_work_details);
        requestValuePairsMap.put("Latitude", Lat);
        requestValuePairsMap.put("Longitude", Long);
        Log.e("CreateJob", requestValuePairsMap.toString());

*/
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<CreateJobModel> call = service.CreateJob("0", s_userId, s_title, How_long, s_amount, categorize,
                s_location, s_date, s_work_details, Lat, Long, arrayListProfi);
        /*Call<CreateJobModel> call = service.CreateJob(RetrofitUtils.createMultipartRequest(requestValuePairsMap));*/
        call.enqueue(new Callback<CreateJobModel>() {


            @Override
            public void onResponse(@NonNull Call<CreateJobModel> call, @NonNull Response<CreateJobModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    CreateJobModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                DataManager.getInstance().setAddress("");
                                DataManager.getInstance().setLat("");
                                DataManager.getInstance().setLong("");

                                createAppUrl(AddFeedActivity.this, result.getData().getId().toString(),
                                        AppPrefs.getStringKeyvaluePrefs(AddFeedActivity.this, AppPrefs.KEY_User_ID));


                            }
                        }
                    } else {
                        popMessage(result.getMessage(), AddFeedActivity.this);
                    }

                } else {
                    // Server Problem
                    Toast.makeText(AddFeedActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateJobModel> call, @NonNull Throwable t) {
                Utils.dismissProgressdialog();
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

    @Override
    public void onBackPressed() {
        DataManager.getInstance().setAddress("");
        DataManager.getInstance().setLat("");
        DataManager.getInstance().setLong("");
        super.onBackPressed();
    }

    public boolean validatetitle() {
        boolean valid = true;

        String s_title = edt_title.getText().toString();


        if (s_title.isEmpty()) {
            edt_title.setError("Enter Title");
            edt_title.requestFocus();
            valid = false;
        } else {
            edt_title.setError(null);
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

    public boolean validateamount() {
        boolean valid = true;

        String s_amount = edt_amount.getText().toString();


        if (s_amount.isEmpty()) {
            edt_amount.setError("Enter Amount");
            edt_amount.requestFocus();
            valid = false;
        } else {
            edt_amount.setError(null);
        }

        return valid;
    }

    public boolean validatelocation() {
        boolean valid = true;

        String s_location = edt_location.getText().toString();


        if (s_location.isEmpty()) {
            edt_location.setError("Enter Location");
            edt_location.requestFocus();
            valid = false;
        } else {
            edt_location.setError(null);
        }

        return valid;
    }

    public boolean validateworkdetails() {
        boolean valid = true;

        String s_work_details = edt_work_details.getText().toString();
        if (s_work_details.isEmpty()) {
            edt_work_details.setError("Enter Work Detail");
            edt_work_details.requestFocus();
            valid = false;
        } else {
            edt_work_details.setError(null);
        }
        return valid;
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


    @Override
    protected void onResume() {
        super.onResume();

        Address = DataManager.getInstance().getAddress();
        Lat = DataManager.getInstance().getLat();
        Long = DataManager.getInstance().getLong();


        if (Address != null && Address.length() > 0) {

            edt_location.setText(Address);
            edt_location.setTextColor(ContextCompat.getColor(AddFeedActivity.this, R.color.black));
            edt_location.setError(null);

        }

    }

    public void createAppUrl(final Context context, String jobId, String UserId) {
        Uri deepLink = Uri.parse("https://azzidaapp.page.link?JobId=" + jobId + "&UserId=" + UserId);
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix("https://azzidaapp.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.azzida")
                        .setMinimumVersion(1)
                        .build())
                .setLink(deepLink)
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.Azzida")
                        .setAppStoreId("1534894647")
                        .setMinimumVersion("1.0")
                        .build());
        DynamicLink link = builder.buildDynamicLink();
        Uri shortLink = link.getUri();
        String longDynamicLink = String.valueOf(link.getUri());
        longDynamicLink += '&' + "ofl" + '=' + "http://azzida.com";
        FirebaseDynamicLinks.getInstance().createDynamicLink().setLongLink(Uri.parse(longDynamicLink))
                .buildShortDynamicLink()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            String jobLink = null;
                            try {
                                jobLink = task.getResult().getShortLink().toString();
                            } catch (Exception e) {
                                jobLink = "";
                            }

                            SetAppUrl(jobId, jobLink);

                        } else {

                            SetAppUrl(jobId, "");

                        }
                    }
                });
    }


    private void SetAppUrl(String Jobid, String Applink) {

        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<SuccessModel> call = service.GetAppLink(Jobid, Applink);
        /*Call<CreateJobModel> call = service.CreateJob(RetrofitUtils.createMultipartRequest(requestValuePairsMap));*/
        call.enqueue(new Callback<SuccessModel>() {


            @Override
            public void onResponse(@NonNull Call<SuccessModel> call, @NonNull Response<SuccessModel> response) {
                Utils.dismissProgressdialog();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SuccessModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFeedActivity.this);
                    builder.setMessage("Job posted successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    // Server Problem
                    Toast.makeText(AddFeedActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessModel> call, @NonNull Throwable t) {

                Utils.dismissProgressdialog();

            }


        });
    }

}
