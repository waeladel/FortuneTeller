package com.fortuneteller.cup;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;

import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;


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

        // Set the app to use night mode always, because it's scary app
        setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }

    public static Context getContext() {
        return sApplicationContext;
        //return instance.getApplicationContext();
    }

}

