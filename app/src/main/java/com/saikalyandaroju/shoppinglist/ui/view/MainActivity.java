package com.saikalyandaroju.shoppinglist.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;

import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.factory.ShoppingFragmentFactory;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Inject
    ShoppingFragmentFactory shoppingFragmentFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportFragmentManager().setFragmentFactory(shoppingFragmentFactory);
        setContentView(R.layout.activity_main);


        if(getSupportFragmentManager().getFragmentFactory()==null){
            Log.i("check","Fragment factory is null");
        }
        else {
            getSupportFragmentManager().getFragmentFactory().instantiate(this.getClassLoader(), ShoppingFragment.class.getName());
        }

        //navController= Navigation.findNavController(this,R.id.navHostFragment);




       // getSupportFragmentManager().getFragmentFactory().beginTransaction().replace(R.id.navHostFragment,ShoppingFragment.class,"")
         //      .commit();
    }
}