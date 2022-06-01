package com.eebbk.ovumserver;

import android.os.Parcel;
import android.os.Parcelable;

public class ViolationFileInfo implements Parcelable {
    private String name;

    protected ViolationFileInfo(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViolationFileInfo> CREATOR = new Creator<ViolationFileInfo>() {
        @Override
        public ViolationFileInfo createFromParcel(Parcel in) {
            return new ViolationFileInfo(in);
        }

        @Override
        public ViolationFileInfo[] newArray(int size) {
            return new ViolationFileInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
