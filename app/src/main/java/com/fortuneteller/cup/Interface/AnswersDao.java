package com.fortuneteller.cup.Interface;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fortuneteller.cup.models.Answers;
import com.fortuneteller.cup.models.User;

import java.util.List;

@Dao
public interface AnswersDao {

    @Insert
    void insert(Answers answer);

    @Update
    void update(Answers answer);

    @Delete
    void delete(Answers answer);

    @Query("DELETE FROM Answers")
    void deleteAll();

    @Query("SELECT * FROM Answers WHERE id = :answerID  LIMIT 1")
    Answers getAnswer(int answerID);

    @Query("SELECT * FROM Answers ORDER BY id DESC")
    //List<Answers> getAnswers();
    LiveData<List<Answers>> getAnswers(); //if i want to return a live data to observe the changes on the fly

}
