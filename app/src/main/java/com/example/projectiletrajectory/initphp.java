package com.example.projectiletrajectory;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface initphp {

    @FormUrlEncoded
    @POST("/coordinates.php")
    void computeCoordinates(
            @Field("speed") int speed,
            @Field("angle") int angle,
            Callback<JsonElement> response
    );
}
