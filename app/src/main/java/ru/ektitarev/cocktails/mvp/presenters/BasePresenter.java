package ru.ektitarev.cocktails.mvp.presenters;

import android.content.Context;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import javax.inject.Inject;

/**
 * Created by ektitarev on 10/01/2019.
 *
 */

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {

    @Inject
    Context context;

}
