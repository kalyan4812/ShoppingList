package com.saikalyandaroju.shoppinglist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;
import com.saikalyandaroju.shoppinglist.repository.GlobalShoppingRepository;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseListener;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class FakeShoppingRepositoryAndroidTest implements GlobalShoppingRepository {


    private List<ShoppingItem> list = new ArrayList<>();

    private MutableLiveData<List<ShoppingItem>> observableList = new MutableLiveData<List<ShoppingItem>>(list);

    private MutableLiveData<Float> observablePrice = new MutableLiveData<Float>();
    private boolean shouldReturnNetworkError = false;

    private void refreshLiveData(){
        observableList.postValue(list);
        observablePrice.postValue(getMyTotalPrice());
    }

    @Override
    public Single<Long> insertShoppingItem(ShoppingItem shoppingItem) {

        list.add(shoppingItem);
        refreshLiveData();
        return null;
    }

    @Override
    public Single<Integer> deleteItem(ShoppingItem shoppingItem) {
        list.remove(shoppingItem);
        refreshLiveData();
        return null;
    }

    @Override
    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return observableList;
    }

    @Override
    public LiveData<Float> getTotalPrice() {
        return observablePrice;
    }

    private Float getMyTotalPrice() {
        double ans=0;
        for(ShoppingItem shoppingItem:list){
            ans=ans+shoppingItem.getPrice();
        }
        return (float)ans;
    }

    @Override
    public void searchForImage(String searchQuery, String apikey, ResponseListener<ImageResponse> responseListener) {
         if(shouldReturnNetworkError){
             responseListener.onResponse(new ApiResponse<>(ResponseStatus.ERROR, null, null));
         }
         else{
             ImageResponse imageResponse1=new ImageResponse(null,0,0);
             responseListener.onResponse(new ApiResponse(ResponseStatus.SUCCESS, imageResponse1, null));
         }
    }

}
