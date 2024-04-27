package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sph.sbh.Adapters.CartAdapter;
import com.sph.sbh.Helper.ChangeNumberItemsListener;
import com.sph.sbh.Helper.ManagmentCart;
import com.sph.sbh.Model.ItemsDomain;
import com.sph.sbh.Model.Modif;
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
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCoupon();
            }
        });

        calculatorCart(0.0);
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
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart(0.0)));
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


                    ArrayList<Modif> modifList = new ArrayList<>();

                    for (ItemsDomain item : listCart2) {
                        String title = item.getTitle();
                        String count = String.valueOf(item.getNumberinCart());
                        Modif modif = new Modif(title, count);
                        modifList.add(modif);
                    }

                    Order order = new Order(orderId, timestamp, modifList);

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


    private void calculatorCart(double discount) {
        double percentTax = 0.02;
        double delivery = 0.02;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * (1 - discount) * 100.0) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText(itemTotal + "DT");
        binding.taxTxt.setText(tax + "DT");
        binding.deliverytTxt.setText(delivery + "DT");
        binding.totalTxt.setText(total + "DT");
    }

    private boolean couponApplied = false;

    private void applyCoupon() {
        String couponCode = binding.textView18.getText().toString().trim();

        // Check if coupon has already been applied
        if (couponApplied) {
            Toast.makeText(CartActivity.this, "Coupon already applied", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference().child("coupons").child(couponCode);
        couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(CartActivity.this, "Coupon applied successfully", Toast.LENGTH_SHORT).show();


                    couponApplied = true;


                    calculatorCart(0.2);
                } else {

                    Toast.makeText(CartActivity.this, "Invalid coupon code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(CartActivity.this, "Error applying coupon", Toast.LENGTH_SHORT).show();
            }
        });
    }

}