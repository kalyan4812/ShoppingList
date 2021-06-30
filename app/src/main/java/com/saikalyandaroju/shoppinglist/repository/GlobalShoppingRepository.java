package com.saikalyandaroju.shoppinglist.repository;

import androidx.lifecycle.LiveData;

import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Query;

public interface GlobalShoppingRepository {

    //Room
    Single<Long> insertShoppingItem(ShoppingItem shoppingItem);
    Single<Integer> deleteItem(ShoppingItem shoppingItem);
    LiveData<List<ShoppingItem>> getAllShoppingItems();
    LiveData<Float> getTotalPrice();

    //Network
    void searchForImage(@Query("q") String searchQuery, @Query("key") String apikey, ResponseListener<ImageResponse> responseListener);
}
