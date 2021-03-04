package com.fortuneteller.cup.dataSources;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fortuneteller.cup.Interface.AnswersDao;
import com.fortuneteller.cup.Interface.UserDao;
import com.fortuneteller.cup.models.Answers;
import com.fortuneteller.cup.models.User;

@Database(entities = {User.class, Answers.class}, version = 1)
public abstract class CupDatabase extends RoomDatabase {

    // To create a singleton of this database
    private static CupDatabase instance;

    public abstract UserDao userDao();
    public abstract AnswersDao AnswersDao();

    // synchronized so that only one thread is able to connect to the database singleton
    // So if two threads are trying to access this database, only one can access it at a time
    public static synchronized CupDatabase getInstance(Context  context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), CupDatabase.class, "cup_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
