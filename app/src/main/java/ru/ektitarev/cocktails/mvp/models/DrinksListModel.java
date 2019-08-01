package ru.ektitarev.cocktails.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinksListModel implements Parcelable
{

    @SerializedName("drinks")
    @Expose
    private List<DrinkModel> drinks = null;

    public final static Parcelable.Creator<DrinksListModel> CREATOR = new Creator<DrinksListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DrinksListModel createFromParcel(Parcel in) {
            return new DrinksListModel(in);
        }

        public DrinksListModel[] newArray(int size) {
            return (new DrinksListModel[size]);
        }

    };

    protected DrinksListModel(Parcel in) {
        in.readList(this.drinks, (ru.ektitarev.cocktails.mvp.models.DrinkModel.class.getClassLoader()));
    }

    public DrinksListModel() {
    }

    public List<DrinkModel> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<DrinkModel> drinks) {
        this.drinks = drinks;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(drinks);
    }

    public int describeContents() {
        return 0;
    }

}