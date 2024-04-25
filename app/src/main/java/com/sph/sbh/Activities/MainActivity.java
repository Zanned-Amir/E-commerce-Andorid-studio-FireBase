package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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
import com.sph.sbh.R;
import com.sph.sbh.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private Button log_out,load;
    EditText data1 ,data2;
    Context context;
    private FirebaseAuth auth;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // log_out = findViewById(R.id.log_out);

        /* log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Log.d("Firebase", "out ");
                // Navigate to LoginActivity after logout
                startActivity(new Intent(MainActivity.this, StartPage.class));
                finish(); // Optional: finish current activity to prevent back navigation
            }
        });
        */
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()==null){
            Toast.makeText(MainActivity.this, "You are not logged in. Redirecting to login page.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, StartPage.class));

            finish();
        }

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Get the count of items in the cart

        int itemCount = ManagmentCart.getCount(this);

// Check if the cart is empty
        if (itemCount == 0) {
            // Cart is empty, hide the notification and count
            binding.notfiy.setVisibility(View.GONE);
            binding.notfiyCount.setVisibility(View.GONE);
        } else {
            // Cart is not empty, show the notification and count
            binding.notfiy.setVisibility(View.VISIBLE);
            binding.notfiyCount.setVisibility(View.VISIBLE);
            // Optionally, you can update the count if needed
            binding.notfiyCount.setText(String.valueOf(itemCount));
        }




        initRunner();
        initCategory();
        initPopular();
        buttonNavigation();





    }

    private void buttonNavigation() {
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

    }

    private void initPopular() {
        DatabaseReference myref = database.getReference("Items");
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
                        binding.recyclerviewPopular.setAdapter(new PopularAdapter(items));
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
                            // Display a toast for each line of data retrieved
                        }
                    }
                    if (!items.isEmpty()) {
                        // LinearLayoutManager should be set only once, not inside the loop
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
                    } else {
                        // Handle case where no categories are found
                        // For example, display a message indicating no categories found
                        // binding.recyclerViewOfficial.setVisibility(View.GONE);
                    }
                }
                binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                // Display a toast indicating the error
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

