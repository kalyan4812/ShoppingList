package com.saikalyandaroju.shoppinglist.ui.view;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.google.android.material.snackbar.Snackbar;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AddShoppingItemFragment extends Fragment {


    ShoppingViewModel shoppingViewModel;
    ImageView pickimage;
    NavController navController;
    private RequestManager requestManager;
    Button addItem;
    EditText name, price, amount;

    @Inject
    public AddShoppingItemFragment(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shopping_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shoppingViewModel = new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class);

        navController = Navigation.findNavController(view);
        pickimage = view.findViewById(R.id.ivShoppingImage);
        addItem = view.findViewById(R.id.btnAddShoppingItem);
        name = view.findViewById(R.id.etShoppingItemName);
        price = view.findViewById(R.id.etShoppingItemPrice);
        amount = view.findViewById(R.id.etShoppingItemAmount);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingViewModel.insertShoppingItem(name.getText().toString(), amount.getText().toString(), price.getText().toString());
            }
        });
        pickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_addShoppingItemFragment_to_imagePickFragment);
            }
        });
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // navController =Navigation.findNavController(view);
                shoppingViewModel.setImageUrl("");
                navController.popBackStack();
            }
        });
        subscribeToObservers();
    }

    private void subscribeToObservers() {
        shoppingViewModel.get_imageUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                requestManager.load(s).into(pickimage);
            }
        });
        shoppingViewModel.get_insertShoppingItemStatus().observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                if (apiResponse != null) {
                    if (apiResponse.status == ResponseStatus.SUCCESS) {

                        Snackbar.make(requireView().getRootView(), "Item Added", Snackbar.LENGTH_LONG).show();
                        navController.popBackStack();
                    } else {
                        Snackbar.make(requireView().getRootView(), "Item  Not Added", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    /////
    /*Snackbar snack = Snackbar.make(findViewById(R.id.coordinatorLayout),
            "Your message", Snackbar.LENGTH_LONG);
    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
            snack.getView().getLayoutParams();
params.setMargins(leftMargin, topMargin, rightMargin, bottomBar.height);
snack.getView().setLayoutParams(params);
snack.show();*/
}