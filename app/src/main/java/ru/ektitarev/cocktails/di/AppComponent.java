package ru.ektitarev.cocktails.di;

import dagger.Component;
import ru.ektitarev.cocktails.di.modules.ApiModule;
import ru.ektitarev.cocktails.di.modules.ContextModule;
import ru.ektitarev.cocktails.mvp.presenters.DrinksPresenter;

@Component(modules = {ApiModule.class, ContextModule.class})
public interface AppComponent {

    void provideDependency(DrinksPresenter drinksPresenter);
}
