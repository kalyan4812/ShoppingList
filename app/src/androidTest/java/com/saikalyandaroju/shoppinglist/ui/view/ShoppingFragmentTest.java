package com.saikalyandaroju.shoppinglist.ui.view;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.MediumTest;

import com.google.android.material.navigation.NavigationView;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.Utils.LiveDataUtilAndroid;
import com.saikalyandaroju.shoppinglist.adapters.ShoppingItemAdapter;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@MediumTest
@HiltAndroidTest
public class ShoppingFragmentTest {

    @Rule
    public HiltAndroidRule hiltAndroidRule=new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // tells junit to execute one function after another.



    @Before
    public void setup(){
        hiltAndroidRule.inject();
    }

    @Inject
    TestShoppingFragmentFactory shoppingFragmentFactory;
    @Inject
    ShoppingItemAdapter shoppingItemAdapter;

    @Test
    public void swipeItem_deleteItemFromDb() throws InterruptedException {
        ShoppingItem shoppingItem=new ShoppingItem("TEST","",1,1f);
        ShoppingViewModel testviewmodel;
        ShoppingFragment shoppingFragment= (ShoppingFragment) HiltFragmentTestingFunction.getLaunchedFragment(new ShoppingFragment(shoppingItemAdapter,null),shoppingFragmentFactory);
        testviewmodel=shoppingFragment.shoppingViewModel;


        shoppingFragment.shoppingViewModel.insertShoppingItemIntoDb(shoppingItem);


        onView(withId(R.id.rvShoppingItems)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
        LiveDataUtilAndroid<List<ShoppingItem>> shoppingItemLiveDataUtilAndroid = new LiveDataUtilAndroid<>();
        List<ShoppingItem> shoppingItems = shoppingItemLiveDataUtilAndroid.getValue(testviewmodel.getShoppingItems());
        assertThat(shoppingItems).isEmpty();
    }

    @Test
    public  void  clickOnAddShoppingItem_navigate_to_AddShoppingFragment(){
        NavController navController=mock(NavController.class); // mocking navcontoller class,without implementaion of contents.
        ShoppingFragment shoppingFragment= (ShoppingFragment) HiltFragmentTestingFunction.getLaunchedFragment(new ShoppingFragment(shoppingItemAdapter,null),shoppingFragmentFactory);
        Navigation.setViewNavController(shoppingFragment.requireView(),navController);

        onView(withId(R.id.fabAddShoppingItem)).perform(click());

        //verify
        verify(navController).navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment);



    }

}