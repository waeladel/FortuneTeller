package com.fortuneteller.cup.dataSources;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.room.Database;

import com.fortuneteller.cup.Interface.DatabaseUserCallback;
import com.fortuneteller.cup.Interface.UserDao;
import com.fortuneteller.cup.models.User;

public  class UserRepository {
    private UserDao userDao;
    private User user;

    public UserRepository(Context context) {
        CupDatabase database = CupDatabase.getInstance(context);
        userDao = database.userDao();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }
    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }
    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }
    public void deleteAll() {
        new DeleteAllAsyncTask(userDao).execute();
    }
    public void getUser(int userId, final DatabaseUserCallback callback) {
        new GetUserAsyncTask(userDao, callback).execute(userId);
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... user) {
            userDao.insert(user[0]);
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... user) {
            userDao.update(user[0]);
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... user) {
            userDao.delete(user[0]);
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class DeleteAllAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private DeleteAllAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... user) {
            userDao.deleteAll();
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class GetUserAsyncTask extends AsyncTask<Integer, Void, User> {
        private UserDao userDao;
        private DatabaseUserCallback callback;
        private GetUserAsyncTask(UserDao userDao, DatabaseUserCallback callback) {
            this.userDao = userDao;
            this.callback = callback;
        }
        @Override
        protected User doInBackground(Integer... userId) {
            return userDao.getUser(userId[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            callback.onCallback(user);
        }

    }

}
