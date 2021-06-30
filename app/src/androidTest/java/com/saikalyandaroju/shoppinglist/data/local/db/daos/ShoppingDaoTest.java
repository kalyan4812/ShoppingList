package com.saikalyandaroju.shoppinglist.data.local.db.daos;


import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.saikalyandaroju.shoppinglist.Utils.LiveDataTestUtil;
import com.saikalyandaroju.shoppinglist.data.local.db.ShoppingDb;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.ui.view.ShoppingFragment;
import com.saikalyandaroju.shoppinglist.utils.HiltFragmentTestingFunction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.InstallIn;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

///@RunWith(AndroidJUnit4.class) repalce with hilt test runner
@HiltAndroidTest


@SmallTest
public class ShoppingDaoTest {

    private static final double DELTA = 1e-15;

    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // tells junit to execute one function after another.


    // system under test
    //  private ShoppingDb shoppingDb; removing private because we have to inject it using hilt

    @Inject
    @Named("test_db")
    ShoppingDb shoppingDb;


    public ShoppingDao getShoppingDao() {
        return shoppingDb.shoppingDao();
    }

    @Before
    public void init() {
       /* shoppingDb = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ShoppingDb.class
        ).allowMainThreadQueries().build();*/


        hiltAndroidRule.inject(); // makes all fields intialize which are annotated with inject.
    }

    @After
    public void finish() {
        shoppingDb.close();
    }


    @Test
    public void insertReadDelete() throws Exception {
        ShoppingItem shoppingItem = new ShoppingItem("name", "url", 1, 1f);
        // insert
        getShoppingDao().insertShoppingItem(shoppingItem).blockingGet();  // wait until inserted

        // LiveDataTestUtil Class waits until we recieve data ,if it takes more than 2 sec it throws time out exception.

        // read
        LiveDataTestUtil<List<ShoppingItem>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<ShoppingItem> insertedItems = liveDataTestUtil.getValue(getShoppingDao().getAllShoppingItems());

        assertNotNull(insertedItems);
        assertEquals(shoppingItem.getName(), insertedItems.get(0).getName());
        assertEquals(shoppingItem.getImageUrl(), insertedItems.get(0).getImageUrl());
        assertEquals(shoppingItem.getAmount(), insertedItems.get(0).getAmount());
        assertEquals(shoppingItem.getPrice(), insertedItems.get(0).getPrice(), DELTA);
        shoppingItem.setId(insertedItems.get(0).getId());
        assertEquals(shoppingItem.getId(), insertedItems.get(0).getId());


        // delete
        getShoppingDao().deleteItem(shoppingItem).blockingGet();

        // confirm the database is empty
        insertedItems = liveDataTestUtil.getValue(getShoppingDao().getAllShoppingItems());
        assertEquals(0, insertedItems.size());

    }

    /*
       Insert Item with null title, throw exception
    */
    @Test(expected = SQLiteConstraintException.class)
    public void insert_nullTitle_throwSQLiteConstraintException() throws Exception {

        final ShoppingItem shoppingItem = new ShoppingItem("name", "url", 1, 1f);
        shoppingItem.setName(null);

        // insert
        getShoppingDao().insertShoppingItem(shoppingItem).blockingGet();
    }

    @Test
    public void observeTotalPriceSum() throws Exception {
        ShoppingItem shoppingItem1 = new ShoppingItem("name", "url", 2, 10f);
        ShoppingItem shoppingItem2 = new ShoppingItem("name", "url", 4, 5.5f);
        ShoppingItem shoppingItem3 = new ShoppingItem("name", "url", 0, 100f);

        getShoppingDao().insertShoppingItem(shoppingItem1).blockingGet();
        getShoppingDao().insertShoppingItem(shoppingItem2).blockingGet();
        getShoppingDao().insertShoppingItem(shoppingItem3).blockingGet();

        LiveDataTestUtil<Float> liveDataTestUtil = new LiveDataTestUtil<>();
        Float price = liveDataTestUtil.getValue(getShoppingDao().getTotalPrice());

        assertThat(price).isEqualTo(2 * 10f + 4 * 5.5f + 0 * 100f);


    }

}
