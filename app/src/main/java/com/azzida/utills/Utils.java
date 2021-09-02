package com.azzida.utills;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.azzida.BuildConfig;
import com.azzida.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class Utils {

    private static final long DURATION = 500;
    private static final String jobLink = "";
    public static ProgressDialog dialog;
    private static SpotsDialog spotsDialog;


    public static void replaceFrg(FragmentActivity ctx, Fragment frag,
                                  boolean addToBackStack) {

        FragmentManager fm = ctx.getSupportFragmentManager();
        int addedFrgCount = fm.getBackStackEntryCount();
        FragmentTransaction ft = fm
                .beginTransaction();
//        if (addedFrgCount > 0)
//            ft.setCustomAnimations(R.animator.slide_in_left,
//                    R.animator.slide_out_right, R.animator.slide_in_right,
//       R.animator.slide_out_left);

        ft.replace(R.id.frag_container, frag, frag.getClass().getName());
        if (addToBackStack)
            ft.addToBackStack(frag.getClass().getName());
        ft.commit();
    }

    public static void removeFrg(FragmentActivity ctx, Fragment frag) {

        FragmentManager fm = ctx.getSupportFragmentManager();
        FragmentTransaction ft = fm
                .beginTransaction();

        ft.remove(frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    public static void printLog(String message) {

        Log.d("Utils", message);

    }


    public static void showProgressdialog(Context ctx, String msg) {
        dialog = new ProgressDialog(ctx);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.show();


    }

    public static void dismissProgressdialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("TAG", String.valueOf(e));
        }
    }


   /* public static void ErrorDialogmessge(Context context, String Message){


        new ErrorDilaog(context,
                Message, new CustomDialogListner() {
            @Override
            public void positiveButtonClick(Dialog dialog, View v) {

                dialog.dismiss();
            }

            @Override
            public void negativeButtonClick(Dialog dialog, View v) {

            }
        }).show();
    }*/

    public static void showAlrtDialg(Context context, String msg) {
        spotsDialog = new SpotsDialog(context, msg, R.style.Custom_white);
        spotsDialog.setCancelable(false);
        spotsDialog.show();
    }


    public static void dismisAlrtDialog() {
        if (spotsDialog != null)
            spotsDialog.dismiss();


    }

    public static void ShowToast(Context context, String Message) {

        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }


    public static synchronized boolean checkValidString(String data) {
        return data != null && !data.isEmpty();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceRTLIfSupported(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.getWindow().getDecorView().
                    setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceLTRIfSupported(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.getWindow().getDecorView().
                    setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }


    /*chanages language */
    public static void chagesLocatiolization(Activity activity, String language) {


        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = activity.getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());


    }


    /*get local langaugae*/

    public static String userLanguage() {
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar")) {
            return "ar";
        }
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("ur")) {
            return "ur";
        } else {
            return "en";
        }
    }

    public static void createDynamicLink_Advanced(final Context context, String jobId, String UserId, String Description) {
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
                        .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Azzida")
                                .setDescription(Description)
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
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Azzida");
                            String shareMessage = Description + "\n\n";
                            shareMessage = shareMessage + jobLink;
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
                        }
                    }
                });
    }


    /**
     * to set listView height at run time
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
        if (listAdapter == null) {
// pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

/*

    public static void saveDevicesToken(Context context, String token) {

        AppPrefs.setStringKeyvaluePrefs(context, AppPrefs.KEY_DEVICES_TOKEN, token);


    }

*/

    public static String saveToInternalSorage(Bitmap bitmapImage) {

        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Azzida");
        if (!folder.exists()) {
            folder.mkdir();
        }
        int pathnew = (int) System.currentTimeMillis();

        File mypath = new File(folder, pathnew + ".jpg");

        FileOutputStream fos = null;
        try {
            mypath.createNewFile();
            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to
            // the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder.getAbsolutePath() + "/" + pathnew + ".jpg";
    }


    public static Bitmap decodeFile(String path) {
        Bitmap image = null;
        try {
            if (path != null && path.trim().length() != 0) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(path), null, o);
                // The new size we want to scale to
                final int REQUIRED_SIZE = 1000;
                // Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                        && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;

                // Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                image = BitmapFactory.decodeStream(new FileInputStream(path),
                        null, o2);
            }
            if (image != null) {
                FileOutputStream f = new FileOutputStream(path);
                int quality = 100;
                try {
                    image.compress(Bitmap.CompressFormat.JPEG, quality, f);
                    f.close();
                    return image;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
/*
    public static boolean checkUserLogin(Context context) {
        boolean userstatus = false;

        if (AppPrefs.getStringKeyvaluePrefs(context,
                AppPrefs.KEY_USER_LOGIN_STATUS).
                equalsIgnoreCase("true")) {
            userstatus = true;
        } else {
            userstatus = false;
        }
        return userstatus;

    }

    */

    public static void rateApp(Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        //Copy App URL from Google Play Store.
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));

        context.startActivity(intent);
    }

    public static long getDateDiffString(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
/*
        long oneDay = 1000 * 60 * 60 * 24;
*/
        long oneDay = 1000 * 60 * 60;
        long delta = (timeTwo - timeOne) / oneDay;

        if (delta > 0) {
            return delta;
        } else {
            delta *= -1;
            return delta;
        }
    }

}
