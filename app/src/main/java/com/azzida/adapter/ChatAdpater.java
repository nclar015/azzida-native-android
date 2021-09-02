package com.azzida.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azzida.R;
import com.azzida.model.GetUserChatModelDatum;
import com.azzida.perfrences.AppPrefs;
import com.azzida.ui.activity.ChatActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdpater extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<GetUserChatModelDatum> getUserChatModels;
    private Context _ctx;
    private ClickView clickView;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;

    public ChatAdpater(Context _ctx, ClickView clickView, ArrayList<GetUserChatModelDatum> getUserChatModels) {
        this._ctx = _ctx;
        this.getUserChatModels = getUserChatModels;
        this.clickView = clickView;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(_ctx).inflate(R.layout.chat_item_rcv, parent, false);
            return new ItemMessageFriendHolder(view);
//            rc_item_message_friend
        } else if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(_ctx).inflate(R.layout.chat_item_sent, parent, false);
            return new ItemMessageUserHolder(view);
//            rc_item_message_user
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof ItemMessageFriendHolder) {

                ((ItemMessageFriendHolder) holder).txtContent.setText(getUserChatModels.get(position).getUserMessage());

                String time = String.valueOf(DateFormat.format("EEE, d MMM yyyy", Long.parseLong(getUserChatModels.get(position).getMessageDateTime())));

/*
                String time = new SimpleDateFormat("EEE, d MMM yyyy").format(getUserChatModels.get(position).getMessageDateTime());
*/
                String today = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(new Date(System.currentTimeMillis()));

                if (today.equals(time)) {
                    ((ItemMessageFriendHolder) holder).txtTime.setText(String.valueOf(DateFormat.format("hh:mm a", Long.parseLong(getUserChatModels.get(position).getMessageDateTime()))));
                } else {
                    ((ItemMessageFriendHolder) holder).txtTime.setText(String.valueOf(DateFormat.format("MMM d", Long.parseLong(getUserChatModels.get(position).getMessageDateTime()))));
                }

                if (getUserChatModels.get(position).getSenderProfilePic().length() > 0) {
                    Picasso.get().load(getUserChatModels.get(position).getSenderProfilePic())
                            .placeholder(R.drawable.no_profile)
                            .error(R.drawable.no_profile)
                            .into(((ItemMessageFriendHolder) holder).avataResever);
                }

            } else if (holder instanceof ItemMessageUserHolder) {

                ((ItemMessageUserHolder) holder).txtContent.setText(getUserChatModels.get(position).getUserMessage());

                String time = String.valueOf(DateFormat.format("EEE, d MMM yyyy", Long.parseLong(getUserChatModels.get(position).getMessageDateTime())));

/*
                String time = new SimpleDateFormat("EEE, d MMM yyyy").format(getUserChatModels.get(position).getMessageDateTime());
*/
                String today = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(new Date(System.currentTimeMillis()));

                if (today.equals(time)) {
                    ((ItemMessageUserHolder) holder).txtTime.setText(String.valueOf(DateFormat.format("hh:mm a", Long.parseLong(getUserChatModels.get(position).getMessageDateTime()))));
                } else {
                    ((ItemMessageUserHolder) holder).txtTime.setText(String.valueOf(DateFormat.format("MMM d", Long.parseLong(getUserChatModels.get(position).getMessageDateTime()))));
                }

                if (getUserChatModels.get(position).getReceiverProfilePic().length() > 0) {
                    Picasso.get().load(getUserChatModels.get(position).getSenderProfilePic())
                            .placeholder(R.drawable.no_profile)
                            .error(R.drawable.no_profile)
                            .into(((ItemMessageUserHolder) holder).avataUser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*viewHolder.card_view_MyListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickView.clickitem(view, position);
            }
        });*/
    }

    @Override
    public int getItemViewType(int position) {
        int s_userId = Integer.parseInt(AppPrefs.getStringKeyvaluePrefs(_ctx, AppPrefs.KEY_User_ID));

        return getUserChatModels.get(position).getSenderId().equals(s_userId) ? VIEW_TYPE_USER_MESSAGE : VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return getUserChatModels == null ? 0 : getUserChatModels.size();
    }




    public void updatedata(ArrayList<GetUserChatModelDatum> getUserChatModels) {
        this.getUserChatModels = getUserChatModels;
        notifyDataSetChanged();


    }


    public interface ClickView {
        void clickitem(View view, int position);
    }


    public class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public TextView txtContent, txtTime;
        public CircleImageView avataUser;

        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            avataUser = (CircleImageView) itemView.findViewById(R.id.imageView2);
        }
    }

    public class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public TextView txtContent, txtTime;
        public CircleImageView avataResever;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            avataResever = (CircleImageView) itemView.findViewById(R.id.imageView3);
        }
    }
}
