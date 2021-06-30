package com.saikalyandaroju.shoppinglist.data.local.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertShoppingItem(ShoppingItem shoppingItem);

    @Delete
    Single<Integer> deleteItem(ShoppingItem shoppingItem);

    @Query("SELECT * FROM shopping_items")
    LiveData<List<ShoppingItem>> getAllShoppingItems();

    @Query("SELECT SUM(price*amount) FROM shopping_items")
    LiveData<Float> getTotalPrice();
}
