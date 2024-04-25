package com.sph.sbh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sph.sbh.Adapters.CartAdapter;
import com.sph.sbh.Helper.ChangeNumberItemsListener;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.Model.Order;
import com.sph.sbh.R;
import com.sph.sbh.databinding.ActivityCartBinding;
import com.sph.sbh.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    DatabaseReference databaseReference;
    ArrayList<ItemsDomain> listCart2;
    private double tax;
    private ManagmentCart   managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders");

        managmentCart = new ManagmentCart(this);
        listCart2 = managmentCart.getListCart();

        calculatorCart();
        setVariable();
        initCartList();


    }
    private void initCartList(){
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }
        else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (listCart2 != null && !listCart2.isEmpty()) {

                    String orderId = databaseReference.push().getKey();
                    long timestamp = System.currentTimeMillis();

                    Order order = new Order(orderId, timestamp, listCart2);

                    databaseReference.child(uid).child(orderId).setValue(order);
                    Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                    managmentCart.clearCart();
                }
                else {
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculatorCart() {
        double percentTax=0.02;
        double  delivery = 0.02;
        tax = Math.round((managmentCart.getTotalFee()*percentTax*100.0))/100.0;

        double total =Math.round((managmentCart.getTotalFee()+tax+delivery)*100.0)/100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee()*100)/100;

        binding.totalFeeTxt.setText(itemTotal+"DT");
        binding.taxTxt.setText(tax+"DT");
        binding.deliverytTxt.setText(delivery+"DT");
        binding.totalTxt.setText(total+"DT");

    }
}