package com.saikalyandaroju.shoppinglist.data.remote;

import com.saikalyandaroju.shoppinglist.BuildConfig;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSource {

    @GET("/api/")
    Observable<ImageResponse> searchForImage(@Query("q") String searchQuery, @Query("key") String apikey);

}
