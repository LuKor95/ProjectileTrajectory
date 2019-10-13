package com.example.projectiletrajectory;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Coordinates implements Parcelable {

    private double time;
    private double x;
    private double y;

    Coordinates(double time, double x, double y) {
        this.time = time;
        this.x = x;
        this.y = y;
    }

    private Coordinates(Parcel in) {
        time = in.readDouble();
        x = in.readDouble();
        y = in.readDouble();
    }

    public double getTime() {
        return time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(time);
        dest.writeDouble(x);
        dest.writeDouble(y);
    }

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };

    @Override
    @NonNull
    public String toString(){
        return "Time: " + this.time + ", X-axis: " + this.x + ", Y-axis: " + this.y;
    }
}
