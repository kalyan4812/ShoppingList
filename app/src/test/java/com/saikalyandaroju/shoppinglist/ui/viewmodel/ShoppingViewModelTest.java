package com.saikalyandaroju.shoppinglist.ui.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.repository.FakeShoppingRepository;
import com.saikalyandaroju.shoppinglist.utils.Constants;
import com.saikalyandaroju.shoppinglist.utils.LiveDataUtil;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;


public class ShoppingViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // tells junit to execute one function after another.

    private ShoppingViewModel shoppingViewModel;

    @Before
    public void setup() {
        shoppingViewModel = new ShoppingViewModel(new FakeShoppingRepository());

    }

    @Test
    public void insert_item_with_empty_filed_return_error() throws Exception {
        shoppingViewModel.insertShoppingItem("name", "", "3.0");
        LiveDataUtil<ApiResponse> liveDataTestUtil = new LiveDataUtil<>();
        ApiResponse insertedItems = liveDataTestUtil.getValue(shoppingViewModel.get_insertShoppingItemStatus());
        assertThat(insertedItems.status).isEqualTo(ResponseStatus.ERROR);

    }

    @Test
    public void name_with_too_long_return_error() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= Constants.MAX_NAME_LENGTH + 1; i++) {
            sb.append("1");
        }
        shoppingViewModel.insertShoppingItem(sb.toString(), "", "3.0");
        LiveDataUtil<ApiResponse> liveDataTestUtil = new LiveDataUtil<>();
        ApiResponse insertedItems = liveDataTestUtil.getValue(shoppingViewModel.get_insertShoppingItemStatus());
        assertThat(insertedItems.status).isEqualTo(ResponseStatus.ERROR);

    }

    @Test
    public void if_all_ok_return_success() throws Exception {

        shoppingViewModel.insertShoppingItem("name", "99", "3.0");
        LiveDataUtil<ApiResponse> liveDataTestUtil = new LiveDataUtil<>();
        ApiResponse insertedItems = liveDataTestUtil.getValue(shoppingViewModel.get_insertShoppingItemStatus());
        assertThat(insertedItems.status).isEqualTo(ResponseStatus.SUCCESS);

    }

    @Test
    public void check_imageurl_empty_after_insertion_return_success() throws Exception {

        shoppingViewModel.insertShoppingItem("name", "99", "3.0");
        LiveDataUtil<ApiResponse> liveDataTestUtil = new LiveDataUtil<>();
        ApiResponse insertedItems = liveDataTestUtil.getValue(shoppingViewModel.get_insertShoppingItemStatus());
        assertThat(insertedItems.status).isEqualTo(ResponseStatus.SUCCESS);

        LiveDataUtil<String> liveDataTestUtils = new LiveDataUtil<>();
        String imageurl = liveDataTestUtils.getValue(shoppingViewModel.get_imageUrl());

        assertThat(imageurl).isEmpty();

    }

}