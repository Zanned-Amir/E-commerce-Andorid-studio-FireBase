package com.sph.sbh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.sph.sbh.Adapters.PopularAdapter;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.databinding.ActivityWishBinding;

import java.util.ArrayList;


public class WhishList extends AppCompatActivity {
    ActivityWishBinding binding ;

    FirebaseDatabase database;
    private PopularAdapter adap;
    private ArrayList<ItemsDomain> itemList;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityWishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
         managmentCart = new ManagmentCart(this);



        binding.backBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(WhishList.this,MainActivity.class));

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
                return true;
            }
        });
        initFavList();




    }



    private void initFavList(){
        itemList = managmentCart.getFavoriteList();

        if(itemList.isEmpty()){
            binding.textView6.setVisibility(View.VISIBLE);
            binding.progressBar4.setVisibility(View.GONE);
        }
        else {
            binding.progressBar4.setVisibility(View.VISIBLE);
        }
        binding.whichRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adap = new PopularAdapter(itemList,true,false); // Pass itemList to the adapter
        binding.whichRecycler.setAdapter(adap);
        binding.progressBar4.setVisibility(View.GONE);
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