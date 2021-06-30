package com.saikalyandaroju.shoppinglist.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.adapters.ShoppingItemAdapter;
import com.saikalyandaroju.shoppinglist.data.local.db.models.ShoppingItem;
import com.saikalyandaroju.shoppinglist.factory.ShoppingFragmentFactory;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ShoppingFragment extends Fragment {


    public ShoppingViewModel shoppingViewModel;

    FloatingActionButton addItem;
    NavController navController;
    NavHostFragment navHostFragment;

    public ShoppingItemAdapter shoppingItemAdapter;
    TextView price;

    @Inject
    public ShoppingFragment(ShoppingItemAdapter shoppingItemAdapter, ShoppingViewModel shoppingViewModel) {
        this.shoppingItemAdapter = shoppingItemAdapter;
        this.shoppingViewModel = shoppingViewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        price = view.findViewById(R.id.tvShoppingItemPrice);

        if (shoppingViewModel == null) {
            shoppingViewModel = new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class);
        }
        subscribeToObservers();
        setupRecyclerView(view);
        // navHostFragment=(NavHostFragment)getActivity().findViewById(R.id.navHostFragment);

        //navController = NavHostFragment.findNavController(ShoppingFragment.this);


        addItem = view.findViewById(R.id.fabAddShoppingItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment);
                // navController.navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment);
            }
        });

    }

    private ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAbsoluteAdapterPosition();
            ShoppingItem shoppingItem = shoppingItemAdapter.shoppingItems.get(pos);
            shoppingViewModel.deleteShoppingItem(shoppingItem);
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shoppingViewModel.insertShoppingItemIntoDb(shoppingItem);
                }
            }).show();

        }
    };

    private void subscribeToObservers() {
        shoppingViewModel.getShoppingItems().observe(getViewLifecycleOwner(), new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                Log.i("checksize",shoppingItems.size()+"");
               // shoppingItemAdapter.set(shoppingItems);
                shoppingItemAdapter.shoppingItems.clear();
                shoppingItemAdapter.shoppingItems.addAll(shoppingItems);
                shoppingItemAdapter.notifyDataSetChanged();
            }
        });
        shoppingViewModel.getTotalPrice().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if (aFloat != null) {
                    price.setText("Total Price : " + aFloat);
                }
            }
        });
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvShoppingItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shoppingItemAdapter);

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }
}