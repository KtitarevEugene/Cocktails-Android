package ru.ektitarev.cocktails.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesListModel implements Parcelable
{

    @SerializedName("drinks")
    @Expose
    private List<CategoryModel> categories = null;
    public final static Parcelable.Creator<CategoriesListModel> CREATOR = new Creator<CategoriesListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoriesListModel createFromParcel(Parcel in) {
            return new CategoriesListModel(in);
        }

        public CategoriesListModel[] newArray(int size) {
            return (new CategoriesListModel[size]);
        }

    }
            ;

    protected CategoriesListModel(Parcel in) {
        in.readList(this.categories, (ru.ektitarev.cocktails.mvp.models.CategoryModel.class.getClassLoader()));
    }

    public CategoriesListModel() {
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> drinks) {
        this.categories = drinks;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categories);
    }

    public int describeContents() {
        return 0;
    }

}