package com.example.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    void insert(QuestionDB question);

    @Insert
    void insertAll(List<QuestionDB> questions);

    @Query("SELECT * FROM questiondb WHERE id = :questionId")
    QuestionDB getById(int questionId);

    @Update
    void update(QuestionDB question);

    @Delete
    void delete(QuestionDB question);

    @Query("SELECT * FROM questiondb")
    List<QuestionDB> getAll();
}

