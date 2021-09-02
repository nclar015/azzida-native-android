package com.azzida.helper;

public class Config {

    /**
     * Main configuration of the WebView
     */

    // Domain host without http:// (e.g. "www.example.org")
    public static final String HOST_TEST = "www.dashboard.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_HxAzYfQjaS2BwXRmSm59rbmP7rMERhEV&scope=read_write";
    public static final String HOST = "www.dashboard.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_HxAz1sDRY3JaN5rbE2r0lfoBtiFTOp5m&scope=read_write";

    // Your URL including http:// and www.
    public static final String HOME_URL_TEST = "https://dashboard.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_HxAzYfQjaS2BwXRmSm59rbmP7rMERhEV&scope=read_write";
    public static final String HOME_URL = "https://dashboard.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_HxAz1sDRY3JaN5rbE2r0lfoBtiFTOp5m&scope=read_write";

    // Customized UserAgent for WebView URL requests (leave it empty to use the default Android UserAgent)
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    // Set to true to clear the WebView cache on each app startup and do not use cached versions of your web app/website
    public static final boolean CLEAR_CACHE_ON_STARTUP = true;

    //Set to "true" to use local "assets/index.html" file instead of URL
    public static final boolean USE_LOCAL_HTML_FOLDER = false;

    // for live
    public static final String Token_Used = "live";
    public static final String Publishable_Key = "pk_live_04yYAydEecXUxer0AHOMFZGW00ipY6MY3Q";

    // for test
/*
    public static final String Token_Used = "test";
    public static final String Publishable_Key = "pk_test_51GtwIjJMe2sL43M0Oe33Z9hp4FoPByGtRoe7VhWnbcsdie9QN04U4ro6ATAqQsDNX7zHxgZjvHybXhrhawPNaWsU00WEZ6c8uw";
*/

}