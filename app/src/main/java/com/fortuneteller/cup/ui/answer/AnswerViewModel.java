package com.fortuneteller.cup.ui.answer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fortuneteller.cup.dataSources.AnswersRepository;
import com.fortuneteller.cup.models.Answer;

import java.util.List;

public class AnswerViewModel extends AndroidViewModel {
    private AnswersRepository repository;
    private LiveData<List<Answer>> answers;

    public AnswerViewModel(@NonNull Application application) {
        super(application);

        repository = new AnswersRepository(application);
        answers = repository.getAnswers();
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

}