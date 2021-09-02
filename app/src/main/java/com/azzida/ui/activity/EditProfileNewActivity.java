package com.azzida.ui.activity;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.azzida.R;
import com.azzida.helper.MultiSelectSpinner;
import com.azzida.helper.PermissionsHelper;
import com.azzida.manager.GoogleAuthManager;
import com.azzida.model.EditProfileModel;
import com.azzida.model.GetJobCategoryModel;
import com.azzida.model.ProfileModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.rest.RetrofitUtils;
import com.azzida.utills.NetworkUtils;
import com.azzida.utills.Utils;
import com.facebook.login.LoginManager;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileNewActivity extends AppCompatActivity implements View.OnClickListener, MultiSelectSpinner.OnMultipleItemsSelectedListener {

    private static final int PICK_FROM_GALLERY = 8;
    private static final int PICKUP_FROM_CAMERA = 9;
    TextView title;
    TextView logoutEd;
    ImageView profile_back;
    String JobList = "";
    EditText edt_lastName, edt_firstName, edt_email, edt_skill;
    CircleImageView profile_image;
    SwitchMaterial switch_Email;
    ArrayList<String> items;
    String[] ite;
    ArrayList<String> ListOfJob;
    private String selectedImagePath = "";
    private MultiSelectSpinner multiSelectSpinner;
    private String ImagePath = "";
    private String EmailOpt, password;
    private EditText edt_old_password, edt_new_password, edt_confirm_password;
    private TextInputLayout textInputLayoutOldPassword, textInputLayoutNewPassword, textInputLayoutConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        items = new ArrayList<String>();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getProfile();
            getJobCategory();

        }

        InitView();
    }


    @SuppressLint("SetTextI18n")
    private void InitView() {

        title = findViewById(R.id.title);

        title.setText("Edit Profile");

        logoutEd = findViewById(R.id.logoutEd);
        logoutEd.setVisibility(View.VISIBLE);


        profile_back = findViewById(R.id.profile_back);

        profile_image = findViewById(R.id.profile_image);

        switch_Email = findViewById(R.id.switch_Email);

        switch_Email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EmailOpt = "true";

                } else {

                    EmailOpt = "false";
                }
            }
        });

        edt_lastName = findViewById(R.id.edt_lastName);
        edt_firstName = findViewById(R.id.edt_firstName);
        edt_email = findViewById(R.id.edt_email);
        edt_skill = findViewById(R.id.edt_skill);

        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);

        textInputLayoutOldPassword = findViewById(R.id.textInputLayoutOldPassword);
        textInputLayoutNewPassword = findViewById(R.id.textInputLayoutNewPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        multiSelectSpinner = findViewById(R.id.spinner);
        profile_back.setOnClickListener(this);

        profile_image.setOnClickListener(this);

    }


    private void getProfile() {

        final ProgressDialog progressdialog = new ProgressDialog(EditProfileNewActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_ID);
        Call<ProfileModel> call = service.GetProfile(s_userId);
        call.enqueue(new Callback<ProfileModel>() {


            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    ProfileModel result = response.body();
                    Log.d("fgh", response.toString());
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {
                            if (result.getData().getId() != null) {

                                edt_lastName.setText(result.getData().getLastName());
                                edt_firstName.setText(result.getData().getFirstName());
                                edt_email.setText(result.getData().getUserEmail());
                                edt_skill.setText(result.getData().getSkills());
                                password = result.getData().getUserPassword();

                                String ListJob = result.getData().getJobType();

                                AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());


                                if (ListJob != null && ListJob.length() > 0) {

                                    JobList = ListJob.replace("[", "").replace("]", "").replace(", ", ",");

                                    String[] elements = ListJob.split(",");

                                    List<String> JobLisr = Arrays.asList(elements);

                                    ListOfJob = new ArrayList<String>(JobLisr);

                                }


                                if (result.getData().getProfilePicture() != null) {
                                    if (result.getData().getProfilePicture().length() > 0) {
                                        Picasso.get().load(result.getData().getProfilePicture())
                                                .placeholder(R.drawable.no_profile)
                                                .error(R.drawable.no_profile)
                                                .into(profile_image);
                                    }

                                }

                            } else {

                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
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
                    Toast.makeText(EditProfileNewActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }

    private void getJobCategory() {

        final ProgressDialog progressdialog = new ProgressDialog(EditProfileNewActivity.this);
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
                            items.add("None");
                            for (int i = 0; i < result.getData().size(); i++) {

                                items.add(result.getData().get(i).getCategoryName());


                            }

                            items.size();

                            ite = new String[items.size()];
                            ite = items.toArray(ite);

                            for (String s : ite) {
                                Log.e("string is", s);
                            }

                            multiSelectSpinner.setItems(items);
                            multiSelectSpinner.hasNoneOption(true);
                            multiSelectSpinner.setSelection(new int[]{0});
                            multiSelectSpinner.setListener(EditProfileNewActivity.this);

                            if (ListOfJob != null) {

                                if (ListOfJob.size() > 0) {

                                    multiSelectSpinner.setSelection(ListOfJob);

                                } else {

                                    multiSelectSpinner.setSelection(0);

                                }

                            }
                        }
                    } else {

                        popMessage(result.getMessage(), EditProfileNewActivity.this);

                    }

                } else {
                    // Server Problem
                    Toast.makeText(EditProfileNewActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetJobCategoryModel> call, @NonNull Throwable t) {
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.profile_back:

                if (edt_firstName.getText().length() > 0) {

                    Intent intent = new Intent(EditProfileNewActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
                    builder.setMessage("Add Profile Detail")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                break;


            case R.id.profile_image:

                if (PermissionsHelper.areExplicitPermissionsRequired()) {
                    List<String> required = PermissionsHelper.isAllPremissiongranted(EditProfileNewActivity.this);
                    if (required != null && required.size() > 0) {
                        PermissionsHelper.show(EditProfileNewActivity.this,
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

    private void callGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_GALLERY);
    }

    public void EditProfile(View view) {

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {
            if (validate()) {

                if (edt_old_password.getText().toString().length() > 0) {

                    if (validateOldPasswordEmpty()) {

                        if (validateOldPassword()) {

                            if (validateNewPasswordEmpty()) {

                                if (validatePasswordEmpty()) {

                                    if (validatePassword()) {

                                        setProfile();

                                    }

                                }
                            }


                        }

                    }


                } else {

                    setProfile();

                }

            }
        }
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

            String picturePath0 = cursor.getString(columnIndex);

            profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath0));

            cursor.close();

            if (picturePath0 == null || picturePath0.trim().length() == 0) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(
                            selectedImage);
                    Bitmap finalBitmap = BitmapFactory.decodeStream(imageStream);
                    selectedImagePath = Utils.saveToInternalSorage(finalBitmap);
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
                selectedImagePath = picturePath0;

            }

        } else if (requestCode == PICKUP_FROM_CAMERA && resultCode == RESULT_OK) {

            selectedImagePath = compressImage(ImagePath);

            if (selectedImagePath.length() > 0) {

                File file = new File(selectedImagePath);
                Bitmap thumbnail = BitmapFactory.decodeFile(file.getPath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert thumbnail != null;
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                profile_image.setImageBitmap(thumbnail);

            }


        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (edt_firstName.getText().length() > 0) {

                Intent intent = new Intent(EditProfileNewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
                builder.setMessage("Add Profile Detail")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setProfile() {

        if (selectedImagePath.length() > 0) {


            final ProgressDialog progressdialog = new ProgressDialog(EditProfileNewActivity.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_ID);
            String s_FirstName = edt_firstName.getText().toString();
            String s_LastName = edt_lastName.getText().toString();
            String s_Email = edt_email.getText().toString();
            String s_Skill = edt_skill.getText().toString();
            String s_Password = edt_new_password.getText().toString();

            String filepath = selectedImagePath;

            HashMap<String, String> requestValuePairsMap = new HashMap<>();
            requestValuePairsMap.put("Id", s_userId);
            requestValuePairsMap.put("RoleId", "2");
            requestValuePairsMap.put("FirstName", s_FirstName);
            requestValuePairsMap.put("LastName", s_LastName);
            requestValuePairsMap.put("UserPassword", s_Password);
            requestValuePairsMap.put("UserEmail", s_Email);
            requestValuePairsMap.put("Skills", s_Skill);
            requestValuePairsMap.put("DeviceId", "");
            requestValuePairsMap.put("DeviceType", "Android");
            requestValuePairsMap.put("EmailType", "");
            requestValuePairsMap.put("UserName", "");
            requestValuePairsMap.put("JobType", JobList);
            requestValuePairsMap.put("ReferalCode", "");
            requestValuePairsMap.put("StripeAccId", "");
            Log.e("Edit Profile", requestValuePairsMap.toString());

            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<EditProfileModel> call = service.EditProfile(RetrofitUtils.createMultipartRequest(requestValuePairsMap),
                    RetrofitUtils.createFilePart("Image", filepath, RetrofitUtils.MEDIA_TYPE_IMAGE_ALL));
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


                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
                                    builder.setMessage("Successfully Saved")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    edt_lastName.setText(result.getData().getLastName());
                                                    edt_firstName.setText(result.getData().getFirstName());
                                                    edt_email.setText(result.getData().getUserEmail());
                                                    edt_skill.setText(result.getData().getSkills());

                                                    if (result.getData().getProfilePicture() != null) {

                                                        if (result.getData().getProfilePicture().length() > 0) {
                                                            Picasso.get().load(result.getData().getProfilePicture())
                                                                    .error(R.drawable.no_profile)
                                                                    .into(profile_image);
                                                        }

                                                    }

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_FirstName, String.valueOf(result.getData().getFirstName()));

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_LastName, String.valueOf(result.getData().getLastName()));

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));

                                                    Intent intent = new Intent(EditProfileNewActivity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

                                                    dialogInterface.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
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
                        Toast.makeText(EditProfileNewActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
                    progressdialog.dismiss();
                }
            });


        } else {

            final ProgressDialog progressdialog = new ProgressDialog(EditProfileNewActivity.this);
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCanceledOnTouchOutside(false);
            progressdialog.setCancelable(false);
            progressdialog.show();

            String s_userId = AppPrefs.getStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_ID);
            String s_FirstName = edt_firstName.getText().toString();
            String s_LastName = edt_lastName.getText().toString();
            String s_Email = edt_email.getText().toString();
            String s_Skill = edt_skill.getText().toString();
            String s_Password = edt_new_password.getText().toString();
            String regId = AppPrefs.getStringKeyvaluePrefsNew(EditProfileNewActivity.this, AppPrefs.KEY_DEVICE_ID);


            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<EditProfileModel> call = service.EditProfileNw(s_userId, "2", s_FirstName, s_LastName, s_Password, s_Email,
                    s_Skill, regId, "Android", "", "", "", JobList, "", "");
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


                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
                                    builder.setMessage("Successfully Saved")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    edt_lastName.setText(result.getData().getLastName());
                                                    edt_firstName.setText(result.getData().getFirstName());
                                                    edt_email.setText(result.getData().getUserEmail());
                                                    edt_skill.setText(result.getData().getSkills());

                                                    if (result.getData().getProfilePicture() != null) {

                                                        if (result.getData().getProfilePicture().length() > 0) {
                                                            Picasso.get().load(result.getData().getProfilePicture())
                                                                    .error(R.drawable.no_profile)
                                                                    .into(profile_image);
                                                        }

                                                    }

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_StripeAccId, result.getData().getStripeAccId());


                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_FirstName, String.valueOf(result.getData().getFirstName()));

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_LastName, String.valueOf(result.getData().getLastName()));

                                                    AppPrefs.setStringKeyvaluePrefs(EditProfileNewActivity.this, AppPrefs.KEY_User_ProfileImage, String.valueOf(result.getData().getProfilePicture()));

                                                    Intent intent = new Intent(EditProfileNewActivity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);


                                                    dialogInterface.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                } else {

                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
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
                        Toast.makeText(EditProfileNewActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EditProfileModel> call, @NonNull Throwable t) {
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

        String s_FirstName = edt_firstName.getText().toString();
        String s_LastName = edt_lastName.getText().toString();
        String s_Email = edt_email.getText().toString();

        if (s_FirstName.isEmpty()) {
            edt_firstName.setError("Enter First Name");
            edt_firstName.requestFocus();
            valid = false;
        } else {
            edt_firstName.setError(null);
        }

        if (s_LastName.isEmpty()) {
            edt_lastName.setError("Enter Last Name");
            edt_lastName.requestFocus();
            valid = false;
        } else {
            edt_lastName.setError(null);
        }

        if (s_Email.isEmpty()) {
            edt_email.setError("Enter Email Id");
            edt_email.requestFocus();
            valid = false;
        } else {
            edt_email.setError(null);
        }

        return valid;
    }


    public boolean validatePassword() {
        boolean valid = true;

        String s_NewPassword = edt_new_password.getText().toString();
        String s_ConfirmPassword = edt_confirm_password.getText().toString();

        if (!s_NewPassword.equals(s_ConfirmPassword)) {
            edt_confirm_password.setError("Confrm pass does not match");
            edt_confirm_password.requestFocus();
            valid = false;
        } else {
            edt_confirm_password.setError(null);
        }


        return valid;
    }

    public boolean validatePasswordEmpty() {
        boolean valid = true;

        String s_ConfirmPassword = edt_confirm_password.getText().toString();

        if (s_ConfirmPassword.isEmpty()) {
            edt_confirm_password.setError("Enter Confirm Password");
            edt_confirm_password.requestFocus();
            valid = false;
        } else {
            edt_confirm_password.setError(null);
        }

        return valid;
    }

    public boolean validateOldPasswordEmpty() {
        boolean valid = true;

        String s_OldPassword = edt_old_password.getText().toString();

        if (s_OldPassword.isEmpty()) {
            edt_old_password.setError("Enter Password");
            edt_old_password.requestFocus();
            valid = false;
        } else {
            edt_old_password.setError(null);
        }


        return valid;
    }


    public boolean validateOldPassword() {
        boolean valid = true;

        String s_OldPassword = edt_old_password.getText().toString();

        if (!s_OldPassword.equals(password)) {
            edt_old_password.setError("Password Not Match");
            edt_old_password.requestFocus();
            valid = false;
        } else {
            edt_old_password.setError(null);
        }


        return valid;
    }

    public boolean validateNewPasswordEmpty() {
        boolean valid = true;

        String s_NewPassword = edt_new_password.getText().toString();

        if (s_NewPassword.isEmpty()) {
            edt_new_password.setError("Enter New Password");
            edt_new_password.requestFocus();
            valid = false;
        } else {
            edt_new_password.setError(null);
        }

        return valid;
    }


    public void LogOut(View view) {
        userLogout();
    }


    private void userLogout() {


        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileNewActivity.this);
        builder.setCancelable(false)
                .setTitle("Logout")
                .setMessage("Do you want to Logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();

                        GoogleAuthManager.getInstall(EditProfileNewActivity.this).signOut();
                        AppPrefs.clearPrefsdata(EditProfileNewActivity.this);

                        Intent intent = new Intent(EditProfileNewActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(EditProfileNewActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
//        alert.setTitle("Logout");
        alert.show();

    }


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

        for (int i = 0; i < strings.size(); i++) {
            if (!strings.get(i).equals("None")) {

                JobList = strings.toString().replace("[", "").replace("]", "").replace(", ", ",");


            } else {

                JobList = "";

            }

        }

        if (strings.size() == 0) {

            JobList = "";

        }

    }

}
