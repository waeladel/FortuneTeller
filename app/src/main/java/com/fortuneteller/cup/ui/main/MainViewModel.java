package com.fortuneteller.cup.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.fortuneteller.cup.Interface.DatabaseUserCallback;
import com.fortuneteller.cup.dataSources.UserRepository;
import com.fortuneteller.cup.models.User;

public class MainViewModel extends AndroidViewModel {
    private UserRepository repository;
    private User user;
    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
    }

    public void getUser(int userId, DatabaseUserCallback callback) {
        if(user == null){
            repository.getUser(userId, callback);
        }
    }
}