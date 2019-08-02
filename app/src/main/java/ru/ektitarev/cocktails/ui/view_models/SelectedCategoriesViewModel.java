package ru.ektitarev.cocktails.ui.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.ektitarev.cocktails.ui.adapters.FiltersAdapter;

public class SelectedCategoriesViewModel extends ViewModel {

    private MutableLiveData<List<FiltersAdapter.FilterItem>> selectedCategories;
    private int categoryIndex;

    public SelectedCategoriesViewModel() {

        selectedCategories = new MutableLiveData<>();
    }

    public void setSelectedCategories(List<FiltersAdapter.FilterItem> selectedCategories) {
        this.selectedCategories.postValue(selectedCategories);
    }

    public LiveData<List<FiltersAdapter.FilterItem>> getSelectedCategories() {
        return selectedCategories;
    }

    public int findNextSelectedCategoryPos (int startPos) {
        List<FiltersAdapter.FilterItem> categories = selectedCategories.getValue();
        if (categories != null) {
            for (int i = startPos; i < categories.size(); i++) {
                if (categories.get(i).isSelected) {
                    return i;
                }
            }
        }

        return -1;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }
}
