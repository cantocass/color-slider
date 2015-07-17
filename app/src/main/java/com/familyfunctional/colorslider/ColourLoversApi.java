package com.familyfunctional.colorslider;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ColourLoversApi {

    @GET("/color/{hexValue}?format=json")
    void singleColour(@Path("hexValue") String hex,  Callback<List<Colour>> callback);
}
