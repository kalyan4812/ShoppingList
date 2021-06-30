package com.saikalyandaroju.shoppinglist.ui.view;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.filters.MediumTest;

import com.bumptech.glide.RequestManager;
import com.saikalyandaroju.shoppinglist.FakeShoppingRepositoryAndroidTest;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.Utils.LiveDataUtilAndroid;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.factory.ShoppingFragmentFactory;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;
import com.saikalyandaroju.shoppinglist.utils.HiltFragmentTestingFunction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MediumTest
@HiltAndroidTest
public class AddShoppingItemFragmentTest {
    @Rule
    public HiltAndroidRule hiltAndroidRule = new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // tells junit to execute one function after another.


    @Before
    public void setup() {
        hiltAndroidRule.inject();
    }

    @Inject
    RequestManager requestManager;
    @Inject
    ShoppingFragmentFactory shoppingFragmentFactory;

    @Test
    public void pressBackButton_popBackStack() {
        NavController navController = mock(NavController.class); // mocking navcontoller class,without implementaion of contents.
        AddShoppingItemFragment shoppingFragment = (AddShoppingItemFragment) HiltFragmentTestingFunction.getLaunchedFragment(new AddShoppingItemFragment(requestManager), shoppingFragmentFactory);
        Navigation.setViewNavController(shoppingFragment.requireView(), navController);

        pressBack();

        //verify navocntroller pop back stack was called.
        verify(navController).popBackStack();
    }

    @Test
    public void selectImage_naviagteToImagePicKFragment() {

        NavController navController = mock(NavController.class); // mocking navcontoller class,without implementaion of contents.
        AddShoppingItemFragment shoppingFragment = (AddShoppingItemFragment) HiltFragmentTestingFunction.getLaunchedFragment(new AddShoppingItemFragment(requestManager), shoppingFragmentFactory);
        Navigation.setViewNavController(shoppingFragment.requireView(), navController);

        onView(withId(R.id.ivShoppingImage)).perform(click());

        //verify
        verify(navController).navigate(R.id.action_addShoppingItemFragment_to_imagePickFragment);
    }

    @Test
    public void pressBackButton_ImageUrlEmpty() {
        NavController navController = mock(NavController.class); // mocking navcontoller class,without implementaion of contents.
        AddShoppingItemFragment shoppingFragment = (AddShoppingItemFragment) HiltFragmentTestingFunction.getLaunchedFragment(new AddShoppingItemFragment(requestManager), shoppingFragmentFactory);
        Navigation.setViewNavController(shoppingFragment.requireView(), navController);

        pressBack();

        //verify navocntroller pop back stack was called.
        verify(navController).popBackStack();
    }

    @Test
    public void clickInsertIntoDb_shoppingItemInsertedIntoDb() throws InterruptedException {
        ShoppingViewModel testviewmodel = new ShoppingViewModel(new FakeShoppingRepositoryAndroidTest());
        AddShoppingItemFragment shoppingFragment = (AddShoppingItemFragment) HiltFragmentTestingFunction.getLaunchedFragment(new AddShoppingItemFragment(requestManager), shoppingFragmentFactory);
        shoppingFragment.shoppingViewModel = testviewmodel;


        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"));
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"));
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"));
        onView(withId(R.id.btnAddShoppingItem)).perform(click());
        LiveDataUtilAndroid<List<ShoppingItem>> shoppingItemLiveDataUtilAndroid = new LiveDataUtilAndroid<>();
        List<ShoppingItem> shoppingItems = shoppingItemLiveDataUtilAndroid.getValue(testviewmodel.getShoppingItems());

        assertThat(shoppingItems.get(0).getName()).isEqualTo("shopping item");
        assertThat(shoppingItems.get(0).getPrice()).isEqualTo(5.5f);
        assertThat(shoppingItems.get(0).getAmount()).isEqualTo(5);

    }
}