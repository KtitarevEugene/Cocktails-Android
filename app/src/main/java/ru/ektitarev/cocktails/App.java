package ru.ektitarev.cocktails;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import ru.ektitarev.cocktails.di.AppComponent;
import ru.ektitarev.cocktails.di.DaggerAppComponent;
import ru.ektitarev.cocktails.di.modules.ContextModule;

public class App extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }
}
