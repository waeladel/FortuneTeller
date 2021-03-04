package com.fortuneteller.cup.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fortuneteller.cup.models.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * FROM User WHERE id = :userId  LIMIT 1")
    User getUser(int userId);

}
