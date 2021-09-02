package com.azzida.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.azzida.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PermissionsHelper {
    public static final int PermissionrequestCode = 1223;
    private static final List<String> permissions = Arrays.asList(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA


    );

    private static final List<String> permission = Arrays.asList(
            "Allow Azzida to Access Storage",
            "Allow Azzida to Access Camera"
    );
    private static final int MAX_PERMISSION_LABEL_LENGTH = 20;
    public static boolean requestrunning = false;

    static List<String> getPermissionConstants(Context context) {
        return permissions;
    }


    private static List<PermissionInfo> getPermissions(Context context, List<String> required) {

        List<PermissionInfo> permissionInfoList = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        for (String permission : required) {
            PermissionInfo pinfo = null;
            try {
                pinfo = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }

            permissionInfoList.add(pinfo);
        }
        return permissionInfoList;
    }

    private static String[] getPermissionNames(Context context, List<String> required) {
        PackageManager pm = context.getPackageManager();
        String[] names = new String[required.size()];
        int i = 0;
        for (PermissionInfo permissionInfo : getPermissions(context, required)) {
            CharSequence label = permissionInfo.loadLabel(pm);
//            if (label.length() > MAX_PERMISSION_LABEL_LENGTH) {
//                label = label.subSequence(0, MAX_PERMISSION_LABEL_LENGTH);
//            }
            names[i] = label.toString();
            i++;
        }
        return names;
    }

    private static boolean[] getPermissionsState(Context context) {
        boolean[] states = new boolean[getPermissionConstants(context).size()];
        int i = 0;
        for (String permission : getPermissionConstants(context)) {
            states[i] = isPermissionGranted(context, permission);
            i++;
        }
        return states;
    }


    public static void show(final Context context, String title, final List<String> required) {
        if (required == null) return;
        if (requestrunning) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, permission);
        builder.setAdapter(adapter, null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestrunning = true;
                ActivityCompat.requestPermissions(scanForActivity(context),
                        required.toArray(new String[required.size()]), PermissionrequestCode);
            }
        });
        builder.setCancelable(false);


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }


    public static boolean isPermissionGranted(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean areExplicitPermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void show(final Context context) {
        show(context, null, null);
    }

    public static List<String> isAllPremissiongranted(Context context) {
        List<String> premissions = getPermissionConstants(context);
        List<String> requiredPremisiion = new ArrayList<String>();
        for (int i = 0; i < premissions.size(); i++) {
            if (!isPermissionGranted(context, premissions.get(i))) {
                requiredPremisiion.add(premissions.get(i));
            }

        }
        return requiredPremisiion;
    }

}
