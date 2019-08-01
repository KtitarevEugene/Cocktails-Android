package ru.ektitarev.cocktails.ui.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.ektitarev.cocktails.mvp.models.CategoriesListModel;

public class CategoriesViewModel extends ViewModel {

    private MutableLiveData<CategoriesListModel> categoriesLiveData;
    private int categoryIndex;

    public CategoriesViewModel() {
        categoriesLiveData = new MutableLiveData<>();
    }

    public void setCategoriesLiveData(CategoriesListModel categoriesListModel) {
        categoriesLiveData.postValue(categoriesListModel);
    }

    public LiveData<CategoriesListModel> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
