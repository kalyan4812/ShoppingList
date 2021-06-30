package com.saikalyandaroju.shoppinglist.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.saikalyandaroju.shoppinglist.BuildConfig;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;
import com.saikalyandaroju.shoppinglist.repository.GlobalShoppingRepository;
import com.saikalyandaroju.shoppinglist.utils.Constants;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseListener;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;
import com.saikalyandaroju.shoppinglist.utils.network.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ShoppingViewModel extends ViewModel {
    private final GlobalShoppingRepository globalShoppingRepository;

    private LiveData<List<ShoppingItem>> shoppingItems;
    private LiveData<Float> totalPrice;
    private MutableLiveData<ApiResponse<ImageResponse>> _images = new MutableLiveData<>();
    private MutableLiveData<String> _imageUrl = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> _insertShoppingItemStatus = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> loadingstatus = new SingleLiveEvent<>();
    private SavedStateHandle savedStateHandle;

    @Inject
    public ShoppingViewModel(GlobalShoppingRepository globalShoppingRepository) {
        this.globalShoppingRepository = globalShoppingRepository;
        shoppingItems = globalShoppingRepository.getAllShoppingItems();
        totalPrice = globalShoppingRepository.getTotalPrice();
        this.savedStateHandle=savedStateHandle;

    }

    public void setImageUrl(String url) {
        _imageUrl.postValue(url);
    }

    public void deleteShoppingItem(ShoppingItem shoppingItem) {
        globalShoppingRepository.deleteItem(shoppingItem).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("check",integer+"");
            }
        });
    }

    public void insertShoppingItemIntoDb(ShoppingItem shoppingItem) {
        globalShoppingRepository.insertShoppingItem(shoppingItem).subscribeOn(Schedulers.io());


    }

    public void insertShoppingItem(String name, String amount, String price) {
        if (name.isEmpty() || amount.isEmpty() || price.isEmpty()) {
            _insertShoppingItemStatus.postValue(new ApiResponse(ResponseStatus.ERROR, "Fields are Empty", null));
            return;
        }
        if (name.length() > Constants.MAX_NAME_LENGTH || price.length() > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(new ApiResponse(ResponseStatus.ERROR, "Length is too long", null));
            return;
        }
        int amounts = 0;
        try {
            amounts = Integer.valueOf(amount);
        } catch (Exception e) {
            _insertShoppingItemStatus.postValue(new ApiResponse(ResponseStatus.ERROR, "Please enter valid amount", e));
            return;
        }
        ShoppingItem shoppingItem = new ShoppingItem(name, (_imageUrl.getValue() == null) ? "" : _imageUrl.getValue(), amounts, Float.parseFloat(price));
        insertShoppingItemIntoDb(shoppingItem);

        setImageUrl("");
        _insertShoppingItemStatus.postValue(new ApiResponse(ResponseStatus.SUCCESS, "Insertion succes", null));
    }

    public void searchForImage(String imageQuery) {
        if (imageQuery.isEmpty()) {
            return;
        }
        globalShoppingRepository.searchForImage(imageQuery, BuildConfig.API_KEY, new ResponseListener<ImageResponse>() {
            @Override
            public void onStart() {
                loadingstatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingstatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<ImageResponse> apiResponse) {
              loadingstatus.setValue(false);
              if(apiResponse!=null && apiResponse.data!=null){
                  _images.postValue(apiResponse);
                  Log.i("check",apiResponse.data.toString());

              }
              else{
                  Log.i("check",apiResponse.errorDescription);
              }
            }
        });
    }



    public LiveData<List<ShoppingItem>> getShoppingItems() {
        return shoppingItems;
    }

    public LiveData<Float> getTotalPrice() {
        return totalPrice;
    }

    public MutableLiveData<ApiResponse<ImageResponse>> get_images() {
        return _images;
    }

    public MutableLiveData<String> get_imageUrl() {
        return _imageUrl;
    }

    public MutableLiveData<ApiResponse> get_insertShoppingItemStatus() {
        return _insertShoppingItemStatus;
    }
}
