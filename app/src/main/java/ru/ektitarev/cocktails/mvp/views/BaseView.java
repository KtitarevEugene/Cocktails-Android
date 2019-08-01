package ru.ektitarev.cocktails.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by ektitarev on 10/01/2019.
 *
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseView extends MvpView {
    void showProgressBar(boolean visible);
    void showErrorMessage();
}
