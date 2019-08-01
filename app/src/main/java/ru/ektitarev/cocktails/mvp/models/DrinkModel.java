package ru.ektitarev.cocktails.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrinkModel implements Parcelable
{

    @SerializedName("idDrink")
    @Expose
    private String idDrink;

    @SerializedName("strDrink")
    @Expose
    private String strDrink;

    @SerializedName("strDrinkThumb")
    @Expose
    private String strDrinkThumb;
    public final static Creator<DrinkModel> CREATOR = new Creator<DrinkModel>() {

        @SuppressWarnings({
            "unchecked"
        })
        public DrinkModel createFromParcel(Parcel in) {
            return new DrinkModel(in);
        }

        public DrinkModel[] newArray(int size) {
            return (new DrinkModel[size]);
        }

    };

    protected DrinkModel(Parcel in) {
        this.idDrink = ((String) in.readValue((String.class.getClassLoader())));
        this.strDrink = ((String) in.readValue((String.class.getClassLoader())));
        this.strDrinkThumb = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DrinkModel() {
    }

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(idDrink);
        dest.writeValue(strDrink);
        dest.writeValue(strDrinkThumb);
    }

    public int describeContents() {
        return  0;
    }

}
