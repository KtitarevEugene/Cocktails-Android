package ru.ektitarev.cocktails.mvp.presenters;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.ektitarev.cocktails.App;
import ru.ektitarev.cocktails.api.Api;
import ru.ektitarev.cocktails.mvp.models.CategoryModel;
import ru.ektitarev.cocktails.mvp.views.DrinksView;

@InjectViewState
public class DrinksPresenter extends BasePresenter<DrinksView> {

    private Disposable disposable;

    @Inject
    Api api;

    public DrinksPresenter() {
        App.getAppComponent().provideDependency(this);

        Log.d(this.getClass().getSimpleName(), "DrinksPresenter");
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadAllCategories();
    }

    public void loadAllCategories() {

        disposable = api.getCategoriesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d ->  getViewState().showProgressBar(true))
                .doOnError(t -> getViewState().showProgressBar(false))
                .doOnComplete(() -> getViewState().showProgressBar(false))
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        getViewState().setAllCategories(response.body());
                    } else {
                        getViewState().showErrorMessage();
                    }

                    disposable = null;
                    loadDrinks(response.body().getCategories().get(0), 0);

                }, throwable -> {
                    getViewState().showErrorMessage();
                    disposable = null;
                });
    }

    public void loadDrinks(CategoryModel category, int index) {

        if (disposable == null) {
            disposable = api.getDrinksList(category.getStrCategory())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> getViewState().showProgressBar(true))
                    .doOnError(t -> getViewState().showProgressBar(false))
                    .doOnComplete(() -> getViewState().showProgressBar(false))
                    .subscribe(response -> {
                        if (response.isSuccessful()) {
                            getViewState().addLoadedDrinks(category, response.body(), index);
                        } else {
                            getViewState().showErrorMessage();
                        }

                        disposable = null;
                    }, throwable -> {
                        getViewState().showErrorMessage();
                        disposable = null;
                    });
        }
    }

    public void clearState() {
        getViewState().clearState();
    }
}
