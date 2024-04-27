package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sph.sbh.Adapters.CategoryAdapter;
import com.sph.sbh.Adapters.PopularAdapter;
import com.sph.sbh.Adapters.SliderAdapter;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.CategoryDomain;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.Model.SliderItems;
import com.sph.sbh.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private Button log_out,load;
    EditText data1 ,data2;
    Context context;
    private FirebaseAuth auth;
    private ActivityMainBinding binding;
    private PopularAdapter adap;
    private ArrayList<ItemsDomain> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()==null){
            Toast.makeText(MainActivity.this, "You are not logged in. Redirecting to login page.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, StartPage.class));

            finish();
        }

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        int itemCount = ManagmentCart.getCount(this);
        binding.search.clearFocus();
        binding.searchRecycler.setVisibility(View.GONE);




        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filerList(newText);

                if (newText.isEmpty()) {
                    // Show other content and hide search results
                    binding.searchRecycler.setVisibility(View.GONE);
                    binding.box6.setVisibility(View.VISIBLE);
                    binding.box2.setVisibility(View.VISIBLE);
                    binding.box3.setVisibility(View.VISIBLE);
                    binding.box4.setVisibility(View.VISIBLE);
                    binding.box5.setVisibility(View.VISIBLE);
                } else {
                    // Hide other content and show search results
                    binding.searchRecycler.setVisibility(View.VISIBLE);
                    binding.box6.setVisibility(View.GONE);
                    binding.box2.setVisibility(View.GONE);
                    binding.box3.setVisibility(View.GONE);
                    binding.box4.setVisibility(View.GONE);
                    binding.box5.setVisibility(View.GONE);
                }

                return true;
            }
        });
        product();

        updateCartNotification();

        initRunner();
        initCategory();
        initPopular();
        buttonNavigation();





    }

    private void filerList(String newText) {
        ArrayList<ItemsDomain> filteredList = new ArrayList<>();

        for(ItemsDomain item: itemList){
            if(item.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"No data found",Toast.LENGTH_SHORT).show();
        }else{
            adap.setFiltredList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartNotification();
    }

    private void updateCartNotification() {

        int itemCount = ManagmentCart.getCount(this);


        if (itemCount == 0) {

            binding.notfiy.setVisibility(View.GONE);
            binding.notfiyCount.setVisibility(View.GONE);
        } else {

            binding.notfiy.setVisibility(View.VISIBLE);
            binding.notfiyCount.setVisibility(View.VISIBLE);

            binding.notfiyCount.setText(String.valueOf(itemCount));
        }
    }

    private void buttonNavigation() {

        binding.whish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WhishList.class));
            }
        });
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });

        binding.ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Profile.class));
            }
        });
        binding.explorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ExplorerActivity.class));
            }
        });

    }
    private void product() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Items");

        itemList = new ArrayList<>(); // Initialize itemList here

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                itemList.add(item); // Add item to itemList
                            }
                        } catch (Exception e) {
                            Log.e("Firebase", "Error converting data", e);
                        }
                    }

                    // Update UI with the list of items
                    if (!itemList.isEmpty()) {
                        binding.searchRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        adap = new PopularAdapter(itemList);
                        binding.searchRecycler.setAdapter(adap); // Set adapter with itemList
                    } else {
                        Log.e("Firebase", "No items found");
                    }

                } else {
                    Log.e("Firebase", "No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }


    private void initPopular() {
        DatabaseReference myref = database.getReference("Popular");
        binding.progressBar3.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                items.add(item);
                            }
                        } catch (Exception e) {
                            Log.e("Firebase", "Error converting data", e);
                        }
                    }

                    // Update UI with the list of items
                    if (!items.isEmpty()) {
                        binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerviewPopular.setAdapter(new PopularAdapter(items,false,true));
                    } else {
                        Log.e("Firebase", "No items found");
                    }

                    // Hide progress bar
                    binding.progressBar3.setVisibility(View.GONE);
                } else {
                    Log.e("Firebase", "No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });
    }


    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBar2.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CategoryDomain category = dataSnapshot.getValue(CategoryDomain.class);
                        if (category != null) {
                            items.add(category);
                        }
                    }
                    if (!items.isEmpty()) {

                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
                    } else {

                    }
                }
                binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "Error retrieving data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void initRunner(){
        DatabaseReference MyRef=database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        MyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                        for(DataSnapshot issue:snapshot.getChildren()){
                            items.add(issue.getValue(SliderItems.class));

                        }
                        banners(items);
                        binding.progressBarBanner.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
private void banners(ArrayList<SliderItems> items){
    binding.viewPageSlider.setAdapter(new SliderAdapter(items,binding.viewPageSlider));
    binding.viewPageSlider.setClipToOutline(false);
    binding.viewPageSlider.setClipChildren(false);
    binding.viewPageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
    compositePageTransformer.addTransformer(new MarginPageTransformer(40));

    binding.viewPageSlider.setPageTransformer(compositePageTransformer);
    }

}

