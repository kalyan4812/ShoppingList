package com.saikalyandaroju.shoppinglist.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.saikalyandaroju.shoppinglist.data.local.db.daos.ShoppingDao;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;

@Database(entities = ShoppingItem.class,version = 1)
public abstract class ShoppingDb extends RoomDatabase {
    public abstract ShoppingDao shoppingDao();
}
