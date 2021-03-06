package com.fortuneteller.cup.Interface;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.paging.PositionalDataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fortuneteller.cup.models.Answer;

import java.util.List;

@Dao
public interface AnswersDao {

    @Insert
    void insert(Answer answer);

    @Update
    void update(Answer answer);

    @Delete
    void delete(Answer answer);

    @Query("DELETE FROM Answer")
    void deleteAll();

    @Query("SELECT * FROM Answer WHERE id = :answerID  LIMIT 1")
    Answer getAnswer(int answerID);

    /*@Query("SELECT * FROM Answer ORDER BY id DESC")
    //List<Answer> getAnswers();
    LiveData<List<Answer>> getAnswers(); //if i want to return a live data to observe the changes on the fly*/

    @Query("SELECT * FROM Answer ORDER BY id DESC")
        //List<Answer> getInitialAnswers();
    PagingSource<Integer, Answer> getAnswers();

    /*@Query("SELECT * FROM Answer ORDER BY id DESC LIMIT :limit")
        //List<Answer> getInitialAnswers();
        PagingSource<Long, Answer> getBeforeAnswers(int limit);*/

}
