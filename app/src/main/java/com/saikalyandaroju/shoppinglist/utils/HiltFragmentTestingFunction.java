package com.saikalyandaroju.shoppinglist.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.saikalyandaroju.shoppinglist.HiltTestActivity;

public class HiltFragmentTestingFunction {

    static Fragment reqfrag;

    public static  <T extends Fragment> void launchFragmentInHiltContainer(Bundle fragmentArgs, int themeResId, FragmentFactory fragmentFactory, T action) {

        themeResId = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme;
        Intent mainActivityIntent = Intent.makeMainActivity(new ComponentName(ApplicationProvider.getApplicationContext(), HiltTestActivity.class))
                .putExtra(FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY, themeResId);


        ActivityScenario<HiltTestActivity> scenario = ActivityScenario.launch(HiltTestActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<HiltTestActivity>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void perform(HiltTestActivity activity) {
                activity.getSupportFragmentManager().setFragmentFactory(fragmentFactory);
                Fragment f = (Fragment) action;
                Fragment fragment =
                        activity.getSupportFragmentManager().getFragmentFactory().instantiate(Preconditions.checkNotNull(f.getClass().getClassLoader()), f.getClass().getName());
                fragment.setArguments(fragmentArgs);
                activity.getSupportFragmentManager().beginTransaction().add(
                        android.R.id.content, fragment, ""
                ).commitNow();


                reqfrag = ((T) fragment);


            }
        });
    }

    public static Fragment getLaunchedFragment(Fragment fragment,FragmentFactory fragmentFactory) {
        if (reqfrag == null) {
            launchFragmentInHiltContainer(null, androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme, fragmentFactory, fragment);
            return reqfrag;
        }
        return reqfrag;

    }

}
