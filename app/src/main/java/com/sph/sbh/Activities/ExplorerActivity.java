package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sph.sbh.Adapters.PopularAdapter;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.databinding.ActivityExplorerBinding;
import com.sph.sbh.databinding.ActivityWishBinding;

import java.util.ArrayList;


public class ExplorerActivity extends AppCompatActivity {
    ActivityExplorerBinding binding ;

    FirebaseDatabase database;
    private PopularAdapter adap;
    private ArrayList<ItemsDomain> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityExplorerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();




        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExplorerActivity.this,MainActivity.class));

            }
        });

        binding.search.clearFocus();
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filerList(newText);

                if (newText.isEmpty()) {


                } else {

                }

                return true;
            }
        });
        initProduct();




    }



    private void initProduct() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Items");

        itemList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            ItemsDomain item = dataSnapshot.getValue(ItemsDomain.class);
                            if (item != null) {
                                itemList.add(item);
                            }
                        } catch (Exception e) {
                            Log.e("Firebase", "Error converting data", e);
                        }
                    }



                    if (!itemList.isEmpty()) {
                        binding.productRecycler.setLayoutManager(new GridLayoutManager(ExplorerActivity.this, 2));
                        adap = new PopularAdapter(itemList);
                        binding.productRecycler.setAdapter(adap);
                        binding.progressBar4.setVisibility(View.GONE);
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
}