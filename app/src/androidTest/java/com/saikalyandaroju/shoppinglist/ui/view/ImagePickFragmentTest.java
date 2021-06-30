package com.saikalyandaroju.shoppinglist.ui.view;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.MediumTest;

import com.saikalyandaroju.shoppinglist.FakeShoppingRepositoryAndroidTest;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.Utils.LiveDataUtilAndroid;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.factory.ShoppingFragmentFactory;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;
import com.saikalyandaroju.shoppinglist.utils.HiltFragmentTestingFunction;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MediumTest
@HiltAndroidTest
public class ImagePickFragmentTest {
    @Rule
    public HiltAndroidRule hiltAndroidRule=new HiltAndroidRule(this);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule(); // tells junit to execute one function after another.


    @Before
    public void setup(){
        hiltAndroidRule.inject();
    }

    @Inject
    ShoppingFragmentFactory shoppingFragmentFactory;


    @Inject
    ImageAdapter imageAdapter;
    @Test
    public void test_clickImage_popBackStackAndSetImageUrl() throws InterruptedException {
        NavController navController=mock(NavController.class); // mocking navcontoller class,without implementaion of contents.
        ImagePickFragment shoppingFragment= (ImagePickFragment) HiltFragmentTestingFunction.getLaunchedFragment(new ImagePickFragment(imageAdapter),shoppingFragmentFactory);
        Navigation.setViewNavController(shoppingFragment.requireView(),navController);
        shoppingFragment.imageAdapter.images.addAll(Arrays.asList("TEST","ABVC","SVCC"));
        ShoppingViewModel testViewModel=new ShoppingViewModel(new FakeShoppingRepositoryAndroidTest());
        shoppingFragment.shoppingViewModel=testViewModel;

        onView(withId(R.id.rvImages)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        verify(navController).popBackStack();

        LiveDataUtilAndroid<String> liveDataTestUtil = new LiveDataUtilAndroid<>();
        String url = liveDataTestUtil.getValue(testViewModel.get_imageUrl());
        assertThat(url).isEqualTo("TEST");


    }
}