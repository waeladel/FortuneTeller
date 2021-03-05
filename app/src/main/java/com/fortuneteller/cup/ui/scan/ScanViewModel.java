package com.fortuneteller.cup.ui.scan;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fortuneteller.cup.App;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.dataSources.AnswersRepository;
import com.fortuneteller.cup.models.Answer;

import java.io.File;
import java.util.List;

public class ScanViewModel extends AndroidViewModel {
    private AnswersRepository repository;
    private LiveData<List<Answer>> answers;
    private MediaPlayer mediaPlayer; // To play the theme

    public ScanViewModel(@NonNull Application application) {
        super(application);

        repository = new AnswersRepository(application);
        answers = repository.getAnswers();

        mediaPlayer = new MediaPlayer(); //App.getMediaPlayer();
    }

    public LiveData<List<Answer>> getAnswers(){
        return answers;
    }

    public void insert (Answer answer) {
        repository.insert(answer);
    }

    public void update (Answer answer) {
        repository.update(answer);
    }

    public void delete (Answer answer) {
        repository.delete(answer);
    }

    public void deleteAll () {
        repository.deleteAll();
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