package com.saikalyandaroju.shoppinglist.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.saikalyandaroju.shoppinglist.R;
import com.saikalyandaroju.shoppinglist.adapters.ImageAdapter;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResponse;
import com.saikalyandaroju.shoppinglist.data.remote.models.ImageResult;
import com.saikalyandaroju.shoppinglist.ui.viewmodel.ShoppingViewModel;
import com.saikalyandaroju.shoppinglist.utils.network.ApiResponse;
import com.saikalyandaroju.shoppinglist.utils.network.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ImagePickFragment extends Fragment {
    public ImageAdapter imageAdapter;

    @Inject
    public ImagePickFragment(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    ShoppingViewModel shoppingViewModel;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_pick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.etSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                Log.i("check",s);
                if (!s.isEmpty()) {
                 shoppingViewModel.searchForImage(s);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        shoppingViewModel = new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class);


        imageAdapter.setOnClickListenr(new ImageAdapter.ClickListener() {
            @Override
            public void onclick(String url) {
                Navigation.findNavController(view)
                        .popBackStack();
                shoppingViewModel.setImageUrl(url);
            }
        });
        initRecyclerView(view);
        subscribeObservers();
    }

    private void subscribeObservers() {
        shoppingViewModel.get_images().observe(getViewLifecycleOwner(), new Observer<ApiResponse<ImageResponse>>() {
            @Override
            public void onChanged(ApiResponse<ImageResponse> listApiResponse) {
              if(listApiResponse.status== ResponseStatus.SUCCESS){

               List<ImageResult> images=new ArrayList<>();
               images.addAll(listApiResponse.data.getHits());
               List<String> finalimages=new ArrayList<>();

               for(ImageResult imageResult:images){
                  finalimages.add(imageResult.previewURL);
               }
                  Log.i("check",finalimages.toString());
               imageAdapter.images.clear();
               imageAdapter.images.addAll(finalimages);


              }else{
                  Snackbar.make(
                          requireView().getRootView(),
                           "An unknown error occured.",
                          Snackbar.LENGTH_LONG
                        ).show();
              }
            }
        });
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvImages);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(imageAdapter);
    }


}