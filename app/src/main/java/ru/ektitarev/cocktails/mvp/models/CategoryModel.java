package ru.ektitarev.cocktails.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel implements Parcelable
{

    @SerializedName("strCategory")
    @Expose
    private String strCategory;

    public final static Parcelable.Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        public CategoryModel[] newArray(int size) {
            return (new CategoryModel[size]);
        }

    };

    protected CategoryModel(Parcel in) {
        this.strCategory = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CategoryModel() {
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(strCategory);
    }

    public int describeContents() {
        return 0;
    }
}