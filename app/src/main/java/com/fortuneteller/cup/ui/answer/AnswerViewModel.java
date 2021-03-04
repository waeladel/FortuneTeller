package com.fortuneteller.cup.ui.answer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fortuneteller.cup.dataSources.AnswersRepository;
import com.fortuneteller.cup.models.Answers;

import java.util.List;

public class AnswerViewModel extends AndroidViewModel {
    private AnswersRepository repository;
    private LiveData<List<Answers>> answers;

    public AnswerViewModel(@NonNull Application application) {
        super(application);

        repository = new AnswersRepository(application);
        answers = repository.getAnswers();
    }

    public LiveData<List<Answers>> getAnswers(){
        return answers;
    }

}