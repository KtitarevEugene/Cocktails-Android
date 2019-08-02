package ru.ektitarev.cocktails.mvp.views;

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.ektitarev.cocktails.mvp.models.CategoriesListModel;
import ru.ektitarev.cocktails.mvp.models.CategoryModel;
import ru.ektitarev.cocktails.mvp.models.DrinksListModel;

public interface DrinksView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void setAllCategories(CategoriesListModel categoriesListModel);

    void addLoadedDrinks(CategoryModel category, DrinksListModel drinksListModel, int index);

    @StateStrategyType(SingleStateStrategy.class)
    void clearState();
}
