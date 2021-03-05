package com.fortuneteller.cup.ui.editprofile;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.fortuneteller.cup.Interface.DatabaseUserCallback;
import com.fortuneteller.cup.dataSources.UserRepository;
import com.fortuneteller.cup.models.User;

public class EditProfileViewModel extends AndroidViewModel {

    private UserRepository repository;
    private User mUser;
    public EditProfileViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
    }

    public void getUser(int userId, DatabaseUserCallback callback) {
        if(mUser == null){
            repository.getUser(userId, callback);
        }else{
            callback.onCallback(mUser);
        }
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public void insert (User user) {
        repository.insert(user);
    }

    public void update (User user) {
        repository.update(user);
    }

    public void delete (User user) {
        repository.delete(user);
    }

    public void deleteAll () {
        repository.deleteAll();
    }

}