package com.fortuneteller.cup.ui.activity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.fortuneteller.cup.App;
import com.fortuneteller.cup.R;

import java.io.File;

public class MainActivityViewModel extends ViewModel {
    private final static String TAG = MainActivityViewModel.class.getSimpleName();
    private MediaPlayer mediaPlayer; // To play the theme

    public MainActivityViewModel() {
        Log.d(TAG, "MainActivityViewModel: created");
        mediaPlayer = new MediaPlayer(); //App.getMediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 21
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build());
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        }

        // Play music
        playMusic();

    }

    public void playMusic(){
        mediaPlayer.reset();
        mediaPlayer.setLooping(false);
        try {
            Uri uri = Uri.parse("android.resource://"+ App.getContext().getPackageName()+ File.separator + R.raw.the_cup_reader_theme);
            mediaPlayer.setDataSource(App.getContext(), uri);
            //mediaPlayer.create(mContext, R.raw.the_cup_reader_theme);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}