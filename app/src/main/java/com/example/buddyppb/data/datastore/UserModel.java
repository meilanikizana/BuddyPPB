package com.example.buddyppb.data.datastore;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String name;
    private boolean isFirstTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public UserModel() {
    }

    private UserModel(Parcel in) {
        this.name = in.readString();
        this.isFirstTime = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isFirstTime ? (byte) 1 : (byte) 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

}
