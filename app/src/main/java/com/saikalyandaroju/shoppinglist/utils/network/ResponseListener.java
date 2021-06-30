package com.saikalyandaroju.shoppinglist.utils.network;

public interface ResponseListener<T> {
    void onStart();

    void onFinish();

    void onResponse(ApiResponse<T> apiResponse);
}
