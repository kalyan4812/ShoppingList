package com.saikalyandaroju.shoppinglist.factory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.bumptech.glide.RequestManager;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.adapters.ShoppingItemAdapter;
import com.saikalyandaroju.shoppinglist.ui.view.AddShoppingItemFragment;
import com.saikalyandaroju.shoppinglist.ui.view.ImagePickFragment;
import com.saikalyandaroju.shoppinglist.ui.view.ShoppingFragment;

import javax.inject.Inject;

public class ShoppingFragmentFactory extends FragmentFactory {

    private final ImageAdapter imageAdapter;
    private final RequestManager requestManager;
    private final ShoppingItemAdapter shoppingItemAdapter;

    @Inject
    public ShoppingFragmentFactory(ImageAdapter imageAdapter, RequestManager requestManager, ShoppingItemAdapter shoppingItemAdapter) {
        this.imageAdapter = imageAdapter;
        this.requestManager = requestManager;
        this.shoppingItemAdapter = shoppingItemAdapter;
    }


    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {

        Fragment f = null;
        Log.i("check",className);
        switch (className) {
            case "com.saikalyandaroju.shoppinglist.ui.view.ImagePickFragment":
                f = new ImagePickFragment(imageAdapter);
                break;

            case "com.saikalyandaroju.shoppinglist.ui.view.ShoppingFragment":
                f = new ShoppingFragment(shoppingItemAdapter,null);
                break;

            case "com.saikalyandaroju.shoppinglist.ui.view.AddShoppingItemFragment":
                f = new AddShoppingItemFragment(requestManager);
                break;
            default:
                f = super.instantiate(classLoader, className);
                break;

        }
        return f;
    }
}
