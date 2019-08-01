package ru.ektitarev.cocktails.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ektitarev.cocktails.mvp.models.CategoriesListModel;
import ru.ektitarev.cocktails.mvp.models.DrinksListModel;

public interface Api {

    @GET("/api/json/v1/1/list.php?c=list")
    Observable<Response<CategoriesListModel>> getCategoriesList();

    @GET("/api/json/v1/1/filter.php")
    Observable<Response<DrinksListModel>> getDrinksList(@Query("c") String category);
}
