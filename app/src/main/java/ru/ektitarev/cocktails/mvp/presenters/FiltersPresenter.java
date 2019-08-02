package ru.ektitarev.cocktails.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.ektitarev.cocktails.App;
import ru.ektitarev.cocktails.api.Api;
import ru.ektitarev.cocktails.mvp.views.FiltersView;

@InjectViewState
public class FiltersPresenter extends BasePresenter<FiltersView> {

    @Inject
    Api api;

    public FiltersPresenter() {

        App.getAppComponent().provideDependency(this);
    }
}
