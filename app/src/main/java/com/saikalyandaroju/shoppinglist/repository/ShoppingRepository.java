package com.saikalyandaroju.shoppinglist.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.saikalyandaroju.shoppinglist.data.local.db.daos.ShoppingDao;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.data.remote.ApiSource;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseListener;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShoppingRepository implements GlobalShoppingRepository {
    private final ShoppingDao shoppingDao;
    private final ApiSource apiSource;

    @Inject
    public ShoppingRepository(ShoppingDao shoppingDao, ApiSource apiSource) {
        this.shoppingDao = shoppingDao;
        this.apiSource = apiSource;
    }

    @Override
    public Single<Long> insertShoppingItem(ShoppingItem shoppingItem) {
        return shoppingDao.insertShoppingItem(shoppingItem);
    }

    @Override
    public Single<Integer> deleteItem(ShoppingItem shoppingItem) {
        return shoppingDao.deleteItem(shoppingItem);
    }

    @Override
    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return shoppingDao.getAllShoppingItems();
    }

    @Override
    public LiveData<Float> getTotalPrice() {
        return shoppingDao.getTotalPrice();
    }

    @Override
    public void searchForImage(String searchQuery, String apikey, ResponseListener<ImageResponse> responseListener) {
        performRequest(apiSource.searchForImage(searchQuery, apikey), responseListener);
    }


    private <T> void performRequest(Observable<T> observable, ResponseListener<T> responseListener) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {

                               }

                               @Override
                               public void onNext(@NonNull T t) {
                                   responseListener.onResponse(new ApiResponse(ResponseStatus.SUCCESS, t, null));
                                   //storeInLocalDb(ts);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {
                                   responseListener.onResponse(new ApiResponse<>(ResponseStatus.ERROR, null, e));
                                   Log.i("check", e.getMessage());
                               }

                               @Override
                               public void onComplete() {
                                   responseListener.onFinish();
                               }
                           }
                    );

    }
}
