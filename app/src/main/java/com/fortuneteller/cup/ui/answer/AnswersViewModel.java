package com.fortuneteller.cup.ui.answer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.fortuneteller.cup.dataSources.AnswersRepository;
import com.fortuneteller.cup.models.Answer;

import kotlinx.coroutines.CoroutineScope;

public class AnswersViewModel extends AndroidViewModel {
    private AnswersRepository repository;
    //private LiveData<List<Answer>> answers;
    private PagingConfig config;
    private LiveData<PagingData<Answer>> itemPagedList;
    private Pager<Integer, Answer> pager;

    public AnswersViewModel(@NonNull Application application) {
        super(application);

        repository = new AnswersRepository(application);
        ///answers = repository.getAnswers();

        config = new PagingConfig(/* pageSize = */ 20);

        //itemPagedList = new LivePagedListBuilder<>(mDataFactory, config).build();

        // CoroutineScope helper provided by the lifecycle-viewmodel-ktx artifact.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        // expose a reactive stream of PagingData objects from a PagingSource.
        // The Paging library supports using several stream types, including Flow, LiveData, and the Flowable and Observable types from RxJava
        pager = new Pager(
                config,
                null, // initialKey,
                null,
                () -> repository.getAnswers());
        // Can get the PagingSource<Integer, Answer> from Dao directly, but this is mor organized to get all things from repository

        // The Pager object calls the load() method from the PagingSource object, providing it with
        // the LoadParams object and receiving the LoadResult object in return.

        // a stream of paged data from the PagingSource implementation
        itemPagedList = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public LiveData<PagingData<Answer>> getAnswers(){
        return itemPagedList;
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