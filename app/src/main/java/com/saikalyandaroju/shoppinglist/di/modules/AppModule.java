package com.saikalyandaroju.shoppinglist.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.adapters.ShoppingItemAdapter;
import com.saikalyandaroju.shoppinglist.data.local.db.ShoppingDb;
import com.saikalyandaroju.shoppinglist.data.local.db.daos.ShoppingDao;
import com.saikalyandaroju.shoppinglist.data.remote.ApiSource;
import com.saikalyandaroju.shoppinglist.repository.GlobalShoppingRepository;
import com.saikalyandaroju.shoppinglist.repository.ShoppingRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.saikalyandaroju.shoppinglist.utils.Constants.BASE_URL;
import static com.saikalyandaroju.shoppinglist.utils.Constants.DATABASE_NAME;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    ShoppingDb provideRoomDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, ShoppingDb.class, DATABASE_NAME).build();
    }


    @Singleton
    @Provides
    ShoppingDao provideShoppingDao(ShoppingDb shoppingDb) {
        return shoppingDb.shoppingDao();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }


    @Singleton
    @Provides
    ApiSource provideApiSource(Retrofit.Builder builder) {
        return builder.build().create(ApiSource.class);
    }

    @Singleton
    @Provides
    GlobalShoppingRepository provideShoppingRepository(ShoppingDao shoppingDao, ApiSource apiSource) {
        return new ShoppingRepository(shoppingDao, apiSource);
    }

    @Singleton
    @Provides
    RequestManager provideGlideInstance(@ApplicationContext Context context) {
        return Glide.with(context).setDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
        );
    }

    @Provides
    ImageAdapter provideImageAdapter(RequestManager requestManager) {
        return new ImageAdapter(requestManager);
    }

    @Provides
    ShoppingItemAdapter provideShoppingItemAdapter(RequestManager requestManager) {
        return new ShoppingItemAdapter(requestManager);
    }
}
