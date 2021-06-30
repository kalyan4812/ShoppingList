package com.saikalyandaroju.shoppinglist.ui.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.bumptech.glide.RequestManager;
import com.saikalyandaroju.shoppinglist.FakeShoppingRepositoryAndroidTest;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.adapters.ShoppingItemAdapter;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;

import javax.inject.Inject;

public class TestShoppingFragmentFactory extends FragmentFactory {

    private ImageAdapter imageAdapter;
    private RequestManager requestManager;
    private ShoppingItemAdapter shoppingItemAdapter;

    @Inject
    public TestShoppingFragmentFactory(ImageAdapter imageAdapter, RequestManager requestManager, ShoppingItemAdapter shoppingItemAdapter) {
        this.imageAdapter = imageAdapter;
        this.requestManager = requestManager;
        this.shoppingItemAdapter = shoppingItemAdapter;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {

        Fragment f = null;
        switch (className) {
            case "com.saikalyandaroju.shoppinglist.ui.view.ImagePickFragment":
                f = new ImagePickFragment(imageAdapter);
                break;

            case "com.saikalyandaroju.shoppinglist.ui.view.ShoppingFragment":
                f = new ShoppingFragment(shoppingItemAdapter,new ShoppingViewModel(new FakeShoppingRepositoryAndroidTest()));
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
