package com.azzida.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.adapter.ChatAdpater;
import com.azzida.model.GetUserChatModel;
import com.azzida.model.GetUserChatModelDatum;
import com.azzida.model.SaveChatModel;
import com.azzida.perfrences.AppPrefs;
import com.azzida.rest.ApiClient;
import com.azzida.rest.ApiService;
import com.azzida.utills.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {


    public final static String BROADCAST_ACTION = "Chat";
    BroadcastReceiver br;
    private RecyclerView recyclerChat;
    private ImageView img_back;
    private String UserId, Profile, Name, JobId;
    private CircleImageView profileimage;
    private TextView fullname;
    private LinearLayout sendbtn;
    private EditText msgedittext;
    private ChatAdpater chatAdpater;
    private ArrayList<GetUserChatModelDatum> arrayChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                UserId = null;
                Profile = null;
                Name = null;
                JobId = null;
            } else {
                UserId = extras.getString("UserId");
                Profile = extras.getString("Profile");
                Name = extras.getString("Name");
                JobId = extras.getString("JobId");
            }
        } else {
            UserId = (String) savedInstanceState.getSerializable("UserId");
            Profile = (String) savedInstanceState.getSerializable("Profile");
            Name = (String) savedInstanceState.getSerializable("Name");
            JobId = (String) savedInstanceState.getSerializable("JobId");
        }

        initView();


        br = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {

                getUserChatTime();

            }
        };


        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
    }

    private void initView() {

        img_back = findViewById(R.id.img_back);

        fullname = findViewById(R.id.fullname);
        profileimage = findViewById(R.id.profileimage);

        if (Name != null && Name.length() > 0) {

            fullname.setText(Name);


        }


        if (Profile != null && Profile.length() > 0) {

            Picasso.get().load(Profile)
                    .placeholder(R.drawable.no_profile)
                    .error(R.drawable.no_profile)
                    .into(profileimage);


        }

        sendbtn = findViewById(R.id.sendbtn);

        msgedittext = findViewById(R.id.msgedittext);

        recyclerChat = findViewById(R.id.recyclerChat);


        final LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerChat.setLayoutManager(mLayoutManager);
        recyclerChat.setHasFixedSize(true);
        recyclerChat.setItemAnimator(new DefaultItemAnimator());

        img_back.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
    }


    private void getUserChat() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<GetUserChatModel> call = service.GetUserChat(s_userId, UserId, JobId);
        call.enqueue(new Callback<GetUserChatModel>() {


            @Override
            public void onResponse(Call<GetUserChatModel> call, Response<GetUserChatModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetUserChatModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arrayChat != null && arrayChat.size() > 0 && chatAdpater != null) {

                                arrayChat.clear();
                                chatAdpater.notifyDataSetChanged();
                            }

                            arrayChat = result.getData();

                            chatAdpater = new ChatAdpater(ChatActivity.this, new ChatAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {


                                }
                            }, arrayChat);

                            recyclerChat.setAdapter(chatAdpater);
                            recyclerChat.smoothScrollToPosition(chatAdpater.getItemCount());

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
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
                    Toast.makeText(ChatActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUserChatModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    private void getUserChatTime() {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        Call<GetUserChatModel> call = service.GetUserChat(s_userId, UserId, JobId);
        call.enqueue(new Callback<GetUserChatModel>() {


            @Override
            public void onResponse(@NonNull Call<GetUserChatModel> call, @NonNull Response<GetUserChatModel> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    GetUserChatModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            if (arrayChat != null && arrayChat.size() > 0 && chatAdpater != null) {

                                arrayChat.clear();
                                chatAdpater.notifyDataSetChanged();
                            }

                            arrayChat = result.getData();

                            chatAdpater = new ChatAdpater(ChatActivity.this, new ChatAdpater.ClickView() {
                                @Override
                                public void clickitem(View view, final int position) {


                                }
                            }, arrayChat);

                            recyclerChat.setAdapter(chatAdpater);
                            recyclerChat.smoothScrollToPosition(chatAdpater.getItemCount());
                            NotificationManagerCompat.from(ChatActivity.this).cancelAll();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
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
                    Toast.makeText(ChatActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUserChatModel> call, @NonNull Throwable t) {
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back:

                finish();


                break;

            case R.id.sendbtn:

                String content = msgedittext.getText().toString().trim();
                if (content.length() > 0) {

                    String status = NetworkUtils.getConnectivityStatus(this);
                    if (status.equals("404")) {
                        Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
                    } else {

                        setChat();

                    }

                }


                break;

        }

    }

    private void setChat() {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setCancelable(false);
        progressdialog.show();
        ApiService service = ApiClient.getClient().create(ApiService.class);
        String s_userId = AppPrefs.getStringKeyvaluePrefs(this, AppPrefs.KEY_User_ID);
        String msg = msgedittext.getText().toString().trim();
        long time = System.currentTimeMillis();
        Call<SaveChatModel> call = service.SaveChat("0", UserId, s_userId, "false", msg, String.valueOf(time), JobId);
        call.enqueue(new Callback<SaveChatModel>() {


            @Override
            public void onResponse(@NonNull Call<SaveChatModel> call, @NonNull Response<SaveChatModel> response) {
                progressdialog.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    SaveChatModel result = response.body();
                    Log.d("fgh", response.toString());
                    assert result != null;
                    if (result.getStatus().equals("1")) {
                        if (result.getData() != null) {

                            msgedittext.setText("");
                            getUserChat();

                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
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
                    Toast.makeText(ChatActivity.this, R.string.ServerUnderMaintenance, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SaveChatModel> call, @NonNull Throwable t) {
                progressdialog.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        String status = NetworkUtils.getConnectivityStatus(this);
        if (status.equals("404")) {
            Toast.makeText(this, R.string.CheckYourInternetConnection, Toast.LENGTH_SHORT).show();
        } else {

            getUserChat();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

}
