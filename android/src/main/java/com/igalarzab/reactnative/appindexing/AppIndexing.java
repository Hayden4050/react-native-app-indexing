package com.igalarzab.reactnative.appindexing;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;

public class AppIndexing extends ReactContextBaseJavaModule {

    private static final String LOGTAG = "ReactNativeAppIndexing";

    private Boolean started = false;
    private String mTitle;
    private String mUrl;

    public AppIndexing(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "AppIndexing";
    }

    @ReactMethod
    public void instantViewAction(String title, String url) {
        if (started) {
            Log.e(LOGTAG, "Starting instant view action before ending the active one");
            stopViewAction();
        }

        setAction(title, url);
        FirebaseUserActions.getInstance().end(getAction());
    }

    @ReactMethod
    public void startViewAction(String title, String url) {
        if (started) {
            Log.e(LOGTAG, "Starting new view action before ending the active one");
            stopViewAction();
        }

        setAction(title, url);

        FirebaseUserActions.getInstance().start(getAction());

        Log.d(LOGTAG, "Started view action with the URL: " + url);
        started = true;
    }

    @ReactMethod
    public void stopViewAction() {
        if (!started) {
            Log.w(LOGTAG, "There is no active view action");
            return;
        }

        FirebaseUserActions.getInstance().end(getAction());

        Log.d(LOGTAG, "Ended view action");
        started = false;
    }

    private Action getAction() {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                        .setObject(mTitle, mUrl)
                        .build();
    }

    private void setAction(String title, String url) {
        this.mTitle = title;
        this.mUrl = url;
    }
}
