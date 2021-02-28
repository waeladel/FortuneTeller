package com.fortuneteller.cup;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import java.io.File;


/**
 * Created by hp on 26/12/2017.
 */

public class App extends Application {

    private static Context sApplicationContext;
    private final static String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {

        super.onCreate();
        sApplicationContext = getApplicationContext();

    }

    public static Context getContext() {
        return sApplicationContext;
        //return instance.getApplicationContext();
    }

}

