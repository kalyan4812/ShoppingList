package com.saikalyandaroju.shoppinglist.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.saikalyandaroju.shoppinglist.data.local.db.ShoppingDb;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class TestAppModule {

    @Provides
    @Named("test_db")
    ShoppingDb provideInMemoryDb(@ApplicationContext Context context){
        return Room.inMemoryDatabaseBuilder(
                context,
                ShoppingDb.class
        ).allowMainThreadQueries().build();
    }
}
