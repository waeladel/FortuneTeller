package com.fortuneteller.cup.dataSources;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagingSource;
import androidx.paging.PositionalDataSource;

import com.fortuneteller.cup.Interface.AnswersDao;
import com.fortuneteller.cup.models.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnswersRepository {
    private AnswersDao answersDao;
    //private PagingSource<Integer, Answer> mAnswers;

    public AnswersRepository(Context context) {
        CupDatabase database = CupDatabase.getInstance(context);
        answersDao = database.AnswersDao();
    }

    public PagingSource<Integer, Answer> getAnswers() {
        return answersDao.getAnswers();
    }


    public void insert(Answer answer) {
        new InsertAnswerAsyncTask(answersDao).execute(answer);
    }
    public void update(Answer answer) {
        new UpdateAnswerAsyncTask(answersDao).execute(answer);
    }
    public void delete(Answer answer) {
        new DeleteAnswerAsyncTask(answersDao).execute(answer);
    }
    public void deleteAll() {
        new DeleteAllAsyncTask(answersDao).execute();
    }



    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class InsertAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;
        private InsertAnswerAsyncTask(AnswersDao answersDao) {
            this.answersDao = answersDao;
        }
        @Override
        protected Void doInBackground(Answer... answer) {
            answersDao.insert(answer[0]);
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class UpdateAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;
        private UpdateAnswerAsyncTask(AnswersDao userDao) {
            this.answersDao = answersDao;
        }
        @Override
        protected Void doInBackground(Answer... answer) {
            answersDao.update(answer[0]);
            return null;
        }
    }


    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class DeleteAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;
        private DeleteAnswerAsyncTask(AnswersDao userDao) {
            this.answersDao = answersDao;
        }
        @Override
        protected Void doInBackground(Answer... answer) {
            answersDao.delete(answer[0]);
            return null;
        }
    }

    // Must user AsyncTask because room doesn't allow make operation on the main thread. Only LiveData Room execute it in the background by default
    private static class DeleteAllAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;
        private DeleteAllAsyncTask(AnswersDao userDao) {
            this.answersDao = answersDao;
        }
        @Override
        protected Void doInBackground(Answer... answer) {
            answersDao.deleteAll();
            return null;
        }
    }

}
