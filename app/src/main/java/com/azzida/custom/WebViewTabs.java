package com.azzida.custom;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.azzida.R;

public class WebViewTabs {

    private static final int TOOLBAR_SHARE_ITEM_ID = 1;

    public static void openTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        enableUrlBarHiding(builder);
        setToolbarColor(context, builder);
        setSecondaryToolbarColor(context, builder);
        setCloseButtonIcon(context, builder);
        setShowTitle(builder);
/*
        setAnimations(context, builder);
*/
        setShareActionButton(context, builder, url);
        addToolbarShareItem(context, builder, url);
        addShareMenuItem(builder);
        addCopyMenuItem(context, builder);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    /* Enables the url bar to hide as the user scrolls down on the page */
    private static void enableUrlBarHiding(CustomTabsIntent.Builder builder) {
        builder.enableUrlBarHiding();
    }

    /* Sets the toolbar color */
    private static void setToolbarColor(Context context, CustomTabsIntent.Builder builder) {
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    /* Sets the secondary toolbar color */
    private static void setSecondaryToolbarColor(Context context, CustomTabsIntent.Builder builder) {
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    /* Sets the Close button icon for the custom tab */
    private static void setCloseButtonIcon(Context context, CustomTabsIntent.Builder builder) {
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_input_add));
    }

    /* Sets whether the title should be shown in the custom tab */
    private static void setShowTitle(CustomTabsIntent.Builder builder) {
        builder.setShowTitle(true);
    }

    /* Sets animations */
   /* private static void setAnimations(Context context, CustomTabsIntent.Builder builder) {
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
    }*/

    /* Sets share action button that is displayed in the Toolbar */
    private static void setShareActionButton(Context context, CustomTabsIntent.Builder builder, String url) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_share);
        String label = "Share via";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(icon, label, pendingIntent);
    }

    /* Adds share item that is displayed in the secondary Toolbar */
    private static void addToolbarShareItem(Context context, CustomTabsIntent.Builder builder, String url) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_share);
        String label = "Share via";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addToolbarItem(TOOLBAR_SHARE_ITEM_ID, icon, label, pendingIntent);

    }

    /* Adds a default share item to the menu */
    private static void addShareMenuItem(CustomTabsIntent.Builder builder) {
        builder.addDefaultShareMenuItem();
    }

    /* Adds a copy item to the menu */
    private static void addCopyMenuItem(Context context, CustomTabsIntent.Builder builder) {
        String label = "Copy";
        Intent intent = new Intent(context, CopyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addMenuItem(label, pendingIntent);
    }

    public static class CopyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getDataString();

            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Link", url);
            clipboardManager.setPrimaryClip(data);

            Toast.makeText(context, "Copied " + url, Toast.LENGTH_SHORT).show();
        }
    }
}
