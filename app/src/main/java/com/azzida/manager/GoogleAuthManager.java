package com.azzida.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;


public final class GoogleAuthManager implements GoogleApiClient.OnConnectionFailedListener {
    static Context context;
    private static GoogleAuthManager install = null;
    private GoogleApiClient googleApiClient;
    public static final int RC_SIGN_IN = 001;

    protected GoogleAuthManager(Context context) {
        GoogleAuthManager.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestServerAuthCode("329922309865-r3uoki743pt8fhm52kgmad9a3m4q9kip.apps.googleusercontent.com")
                .requestIdToken("329922309865-r3uoki743pt8fhm52kgmad9a3m4q9kip.apps.googleusercontent.com")
                .build();
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public static GoogleAuthManager getInstall(Context mContext) {
        if (install == null || mContext != GoogleAuthManager.context) {
            install = new GoogleAuthManager(mContext);
        }
        return install;
    }

    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
