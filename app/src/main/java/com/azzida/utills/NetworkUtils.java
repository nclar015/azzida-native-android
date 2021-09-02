package com.azzida.utills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {

    // public static int TYPE_WIFI = 1;
    // public static int TYPE_MOBILE = 2;
    // public static int TYPE_NOT_CONNECTED = 0;

    public static final String WIFI = "wifi";
    public static final String WIMAX = "wimax";
    // mobile
    public static final String MOBILE = "mobile";
    // Android L calls this Cellular, because I have no idea!
    public static final String CELLULAR = "cellular";
    // 2G network types
    public static final String GSM = "gsm";
    public static final String GPRS = "gprs";
    public static final String EDGE = "edge";
    // 3G network types
    public static final String CDMA = "cdma";
    public static final String UMTS = "umts";
    public static final String HSPA = "hspa";
    public static final String HSUPA = "hsupa";
    public static final String HSDPA = "hsdpa";
    public static final String ONEXRTT = "1xrtt";
    public static final String EHRPD = "ehrpd";
    // 4G network types
    public static final String LTE = "lte";
    public static final String UMB = "umb";
    public static final String HSPA_PLUS = "hspa+";
    // return type
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_ETHERNET = "ethernet";
    public static final String TYPE_WIFI = "wifi";
    public static final String TYPE_2G = "2g";
    public static final String TYPE_3G = "3g";
    public static final String TYPE_4G = "4g";
    public static final String TYPE_NONE = "404";
    private static final String LOG_TAG = "NetworkManager";
    private static final String PATTERN_IPV4_ADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    public static int NOT_REACHABLE = 0;
    public static int REACHABLE_VIA_CARRIER_DATA_NETWORK = 1;
    public static int REACHABLE_VIA_WIFI_NETWORK = 2;

    private NetworkUtils() {

    }


    public static String getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        String gettype = TYPE_NONE;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {

            gettype = getType(activeNetwork);
        }
        return gettype;
    }

    private static String getType(NetworkInfo info) {
        if (info != null) {
            String type = info.getTypeName();

            if (type.toLowerCase().equals(WIFI)) {
                return TYPE_WIFI;
            } else if (type.toLowerCase().equals(MOBILE)
                    || type.toLowerCase().equals(CELLULAR)) {
                type = info.getSubtypeName();
                if (type.toLowerCase().equals(GSM)
                        || type.toLowerCase().equals(GPRS)
                        || type.toLowerCase().equals(EDGE)) {
                    return TYPE_2G;
                } else if (type.toLowerCase().startsWith(CDMA)
                        || type.toLowerCase().equals(UMTS)
                        || type.toLowerCase().equals(ONEXRTT)
                        || type.toLowerCase().equals(EHRPD)
                        || type.toLowerCase().equals(HSUPA)
                        || type.toLowerCase().equals(HSDPA)
                        || type.toLowerCase().equals(HSPA)) {
                    return TYPE_3G;
                } else if (type.toLowerCase().equals(LTE)
                        || type.toLowerCase().equals(UMB)
                        || type.toLowerCase().equals(HSPA_PLUS)) {
                    return TYPE_4G;
                }
            }
        } else {
            return TYPE_NONE;
        }
        return TYPE_UNKNOWN;
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo.isConnected();
    }

    /**
     * Register a broadcast receiver for listening network state change. When calling this method,
     * remember to unregister in your onDestroy callback of your activity.
     */
    public static void registerNetworkStateChangeReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }

    /**
     * May return null for some carriers. Note: <uses-permission
     * android:name="android.permission.CHANGE_NETWORK_STATE" /> required！
     */
/*
    public static String getPhoneNumber(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (!TextUtils.isEmpty(mPhoneNumber)) {
            return mPhoneNumber;
        }
        return null;
    }
*/

    /**
     * If there are both internal and external network IP, use this to get the internal network IP
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    /**
     * Get the intranet IP in the WIFI LAN
     *
     * @param context
     * @return
     */
    public static String getInternalIP(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (ipAddress == 0)
            return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    /**
     * Get the IP of the connected Wifi router
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getWifiRouterIp(Context context) {
        final WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        return Formatter.formatIpAddress(dhcp.gateway);
    }

    /**
     * Determine whether the IP is a legal IPv4 address through regular expressions
     *
     * @param ip
     * @return
     */
    public static boolean isValidIPv4Address(String ip) {
        if (!TextUtils.isEmpty(ip) && ip.length() < 16) {
            Pattern pattern = Pattern.compile(PATTERN_IPV4_ADDRESS);
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        }

        return false;
    }

}

/************************************* How to Use ********************************************/

/*
 *
 * String status = NetworkUtil.getConnectivityStatus(getActivity());
 * ShowAlert alert = new ShowAlert(); if (status.equals("404")) {
 * alert.showAlertDialog(getActivity(), "Fail",
 * "Internet Connection is NOT Available", false); }
 */